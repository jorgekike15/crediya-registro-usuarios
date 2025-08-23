package co.com.pragma.api;

import co.com.pragma.api.config.dto.CreateUserDTO;
import co.com.pragma.api.config.dto.mapper.UserDTOMapper;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;

    public Mono<ServerResponse> listenGETCreateUser(ServerRequest serverRequest) {
        log.info("Iniciando proceso de creación de usuario");
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .flatMap(createUserDTO ->
                        userUseCase.saveUser(userDTOMapper.toModel(createUserDTO))
                                .then(ServerResponse.ok().bodyValue(createUserDTO))
                )
                .doOnError(e -> log.error("Error al crear usuario", e))
                .onErrorResume(e ->
                        ServerResponse.status(500).bodyValue("Error interno: " + e.getMessage())
                );
    }

    public Mono<ServerResponse> listenGETGetAllUsers(ServerRequest serverRequest) {
        System.out.println("Received request to get all users: " + serverRequest.queryParams());
        return ServerResponse.ok()
                .body(userUseCase.findAllUsers(), User.class);
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

//    @PostMapping
//    public Mono<Void> createUser(@RequestBody CreateUserDTO createUserDTO) {
//        return userUseCase.saveUser(userDTOMapper.toModel(createUserDTO));
//    }
//
//    @GetMapping
//    public Flux<UserDTO> getAllUsers() {
//        return userUseCase.findAllUsers()
//                .map(userDTOMapper::toResponse)
//                .doOnNext(userDTO -> log.info("Usuario encontrado: {}", userDTO));
//    }
}
