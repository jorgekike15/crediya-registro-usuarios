package co.com.pragma.api;

import co.com.pragma.api.config.dto.CreateUserDTO;
import co.com.pragma.api.config.dto.UserDTO;
import co.com.pragma.api.config.dto.mapper.UserDTOMapper;
import co.com.pragma.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenGETCreateUser(ServerRequest serverRequest) {
        log.info("Inicio de método: listenGETCreateUser ");
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .flatMap(this::validacion)
                .flatMap(dto -> userUseCase.saveUser(userDTOMapper.toModel(dto)))
                .map(userDTOMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .doOnError(e -> log.error("Error al crear usuario", e))
                .onErrorResume(e -> {
                    if (e instanceof IllegalArgumentException) {
                        return ServerResponse.status(409).bodyValue("Error: " + e.getMessage());
                    }
                    if (e instanceof ValidationException) {
                        return ServerResponse.status(400).bodyValue("Error de validación: " + e.getMessage());
                    }
                    return ServerResponse.status(500).bodyValue("Error: " + e.getMessage());
                })
                .doFinally(signalType -> log.info("Fin de método listenGETCreateUser "));
    }

    public Mono<ServerResponse> listenGETGetAllUsers(ServerRequest serverRequest) {
        log.info("Inicio de método: ");
        return ServerResponse.ok()
                .body(userUseCase.findAllUsers().map(userDTOMapper::toResponse), UserDTO.class)
                .doOnError(e -> log.error("Error al consultar todos los usuarios", e))
                .onErrorResume(e -> {
                    if (e instanceof IllegalArgumentException) {
                        return ServerResponse.status(409).bodyValue("Error: " + e.getMessage());
                    }
                    if (e instanceof ValidationException) {
                        return ServerResponse.status(400).bodyValue("Error de validación: " + e.getMessage());
                    }
                    return ServerResponse.status(500).bodyValue("Error: " + e.getMessage());
                })
                .doFinally(signalType -> log.info("Fin de método: listenGETGetAllUsers (señal: {})", signalType));
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
