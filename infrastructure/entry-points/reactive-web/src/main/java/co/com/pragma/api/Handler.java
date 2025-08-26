package co.com.pragma.api;

import co.com.pragma.api.config.dto.CreateUserDTO;
import co.com.pragma.api.config.dto.mapper.UserDTOMapper;
import co.com.pragma.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final Validator validator;
    private final TransactionalOperator transactionalOperator;
    private ResourceBundle bundle = ResourceBundle.getBundle("log4j2");

    public Mono<ServerResponse> listenGETCreateUser(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenGETCreateUser"));
        }
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(request -> log.debug(MessageFormat.format(bundle
                        .getString("log.payload.received"), request)))
                .flatMap(this::validacion)
                .doOnNext(valid -> log.trace(bundle.getString("log.payload.validated")))
                .map(userDTOMapper::toModel)
                .doOnNext(domain -> log.debug(MessageFormat.format(bundle.getString("log.domain.generated"),
                        domain)))
                .flatMap(user -> userUseCase.saveUser(user)
                        .as(transactionalOperator::transactional)
                )
                .map(userDTOMapper::toResponse)
                .doOnSuccess(saved -> log.info(MessageFormat.format(bundle.getString("log.user.created"),
                        saved)))
                .doOnError(error -> log.error(bundle.getString("log.user.create.error"), error))
                .flatMap(saved -> {
                    log.trace(MessageFormat.format(bundle.getString("log.user.http201"), saved));
                    return ServerResponse.status(org.springframework.http.HttpStatus.CREATED).bodyValue(saved);
                })
                .doFinally(signalType -> log.info(
                        MessageFormat.format(bundle.getString("log.method.end"),
                                "listenGETCreateUser", signalType)
                ));
    }

    public Mono<ServerResponse> listenGETGetAllUsers(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenGETGetAllUsers"));
        }
        return userUseCase.findAllUsers()
                .map(userDTOMapper::toResponse)
                .collectList()
                .doOnNext(users -> log.debug(MessageFormat.format(
                        bundle.getString("log.users.retrieved"), users.size())))
                .flatMap(users -> {
                    log.trace(bundle.getString("log.users.http200"));
                    return ServerResponse.ok().bodyValue(users);
                })
                .doOnSuccess(response -> log.info(bundle.getString("log.users.query.success")))
                .doOnError(error -> log.error(bundle.getString("log.users.query.error"), error))
                .doFinally(signalType -> log.info(
                        MessageFormat.format(bundle.getString("log.method.end"),
                                "listenGETGetAllUsers", signalType)
                ));
    }

    public Mono<ServerResponse> listenGETValidateByDocument(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenGETValidateByDocument"));
        }
        return Mono.justOrEmpty(serverRequest.queryParam("document"))
                .filter(document -> !document.isBlank())
                .flatMap(document -> userUseCase.existsByDocumentoIdentificacion(document)
                        .hasElement()
                        .flatMap(exists -> ServerResponse.ok().bodyValue(exists))
                )
                .switchIfEmpty(ServerResponse.badRequest().bodyValue("El parámetro 'document' es obligatorio"))
                .doOnSuccess(response ->     log.info(bundle.getString("log.users.query.success")))
                .doOnError(error -> log.error(bundle.getString("log.user.query.byid.error"), error))
                .doFinally(signalType -> log.info(
                        MessageFormat.format(bundle.getString("log.method.end"),
                                "listenGETValidateByDocument", signalType)
                ));
    }

    private Mono<CreateUserDTO> validacion(CreateUserDTO request) {
        Set<ConstraintViolation<CreateUserDTO>> violaciones = validator.validate(request);
        if (!violaciones.isEmpty()) {
            String errorMessage = violaciones.stream()
                    .map(violation -> violation.getPropertyPath() + ": " +
                            violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Mono.error(new ValidationException(errorMessage));
        }
        return Mono.just(request);
    }

}
