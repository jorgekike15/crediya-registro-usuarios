package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ResourceBundle;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final ResourceBundle bundle = ResourceBundle.getBundle("log4j2");

    public Mono<User> saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existsUser -> Mono.<User>error(new IllegalArgumentException(
                        bundle.getString("user.email.exists"))))
                .switchIfEmpty(Mono.defer(() -> userRepository.saveUser(user)));
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<User> existsByDocumentoIdentificacion(String documentoIdentificacion){
        return userRepository.findByDocumentoIdentificacion(documentoIdentificacion);
    }

}
