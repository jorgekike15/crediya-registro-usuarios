package co.com.pragma.api;

import co.com.pragma.api.config.dto.CreateUserDTO;
import co.com.pragma.api.config.dto.LoginDTO;
import co.com.pragma.api.config.dto.mapper.UserDTOMapper;
import co.com.pragma.api.config.exception.InvalidCredentialsException;
import co.com.pragma.api.config.security.JwtUtil;
import co.com.pragma.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
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
    private final JwtUtil jwtUtil;

    public Mono<ServerResponse> listenGETCreateUser(ServerRequest serverRequest) {
        log.trace("Iniciando creación de usuario desde request");
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(request -> log.debug("Payload recibido: {}", request))
                .flatMap(this::validacion)
                .doOnNext(valid -> log.trace("Payload validado correctamente"))
                .map(userDTOMapper::toModel)
                .doOnNext(domain -> log.debug("Objeto de dominio generado: {}", domain))
                .flatMap(user -> userUseCase.saveUser(user)
                        .as(transactionalOperator::transactional)
                )
                .map(userDTOMapper::toResponse)
                .doOnSuccess(saved -> log.info("Usuario creado exitosamente: {}", saved))
                .doOnError(error -> log.error("Error al crear usuario", error))
                .flatMap(saved -> {
                    log.trace("Construyendo respuesta HTTP 201 para usuario: {}", saved);
                    return ServerResponse.status(org.springframework.http.HttpStatus.CREATED).bodyValue(saved);
                })
                .doFinally(signalType -> log.info("Fin de método listenGETCreateUser (señal: {})", signalType));
    }

    public Mono<ServerResponse> listenGETGetAllUsers(ServerRequest serverRequest) {
        log.trace("Iniciando consulta de todos los usuarios");

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .flatMap(auth -> {
                    if (auth == null || !auth.isAuthenticated()) {
                        return ServerResponse.status(401).bodyValue("No autenticado");
                    }
                    String token = auth.getCredentials().toString();
                    Integer rol = jwtUtil.extractRol(token);
                    // Cambia el valor 1 por el id de rol que debe tener acceso
                    if (rol == null || rol != 1) {
                        return ServerResponse.status(403).bodyValue("No autorizado");
                    }
                    return userUseCase.findAllUsers()
                            .map(userDTOMapper::toResponse)
                            .collectList()
                            .doOnSuccess(response -> log.info("Consulta de usuarios completada exitosamente"))
                            .flatMap(users -> ServerResponse.ok().bodyValue(users));
                })
                .doOnError(error -> log.error("Error al consultar todos los usuarios", error))
                .doFinally(signalType -> log.info("Fin de método listenGETGetAllUsers (señal: {})", signalType));

//        return userUseCase.findAllUsers()
//                .map(userDTOMapper::toResponse)
//                .collectList()
//                .doOnNext(users -> log.debug("Usuarios recuperados: {}", users.size()))
//                .flatMap(users -> {
//                    log.trace("Construyendo respuesta HTTP 200 para usuarios");
//                    return ServerResponse.ok().bodyValue(users);
//                })
//                .doOnSuccess(response -> log.info("Consulta de usuarios completada exitosamente"))
//                .doOnError(error -> log.error("Error al consultar todos los usuarios", error))
//                .doFinally(signalType -> log.info("Fin de método listenGETGetAllUsers (señal: {})", signalType));
    }

    public Mono<ServerResponse> listenGETValidateByDocument(ServerRequest serverRequest) {
        return Mono.justOrEmpty(serverRequest.queryParam("document"))
                .filter(document -> !document.isBlank())
                .flatMap(document -> userUseCase.existsByDocumentoIdentificacion(document)
                        .hasElement()
                        .flatMap(exists -> ServerResponse.ok().bodyValue(exists))
                )
                .switchIfEmpty(ServerResponse.badRequest().bodyValue("El parámetro 'document' es obligatorio"))
                .doOnSuccess(response -> log.info("Consulta de usuario completada exitosamente"))
                .doOnError(error -> log.error("Error al consultar usuario por idenficación", error))
                .doFinally(signalType -> log.info("Fin de método listenGETValidateByDocument (señal: {})", signalType));
    }

    public Mono<ServerResponse> listenPOSTLogin(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDTO.class)
                .map(userDTOMapper::toModelLogin)
                .flatMap(loginRequest -> userUseCase.validatePassword(loginRequest.getEmail(), loginRequest.getPassword())
                        .switchIfEmpty(Mono.error(new InvalidCredentialsException("Credenciales inválidas")))
                        .flatMap(user -> {
                            String token = jwtUtil.generateToken(user);
                            return ServerResponse.ok().bodyValue(Map.of("token", token));
                        })
                )
                .doOnError(error -> log.error("Error en login", error));
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
