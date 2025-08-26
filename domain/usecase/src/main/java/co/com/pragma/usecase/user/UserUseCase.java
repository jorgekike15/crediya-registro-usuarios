package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.in.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ResourceBundle;

@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {

    private final UserRepository userRepository;

    @Override
    public Mono<User> saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existsUser -> Mono.<User>error(
                        new IllegalArgumentException("El correo electrónico ya está en uso")))
                .switchIfEmpty(Mono.defer(() -> userRepository.saveUser(user)));
    }

    @Override
    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public Mono<User> existsByDocumentoIdentificacion(String documentoIdentificacion){
        return userRepository.findByDocumentoIdentificacion(documentoIdentificacion);
    }

}
