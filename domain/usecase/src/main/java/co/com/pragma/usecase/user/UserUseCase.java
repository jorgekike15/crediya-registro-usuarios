package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.in.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {

        return userRepository.findByEmail(user.getEmail())
                .flatMap(existsUser -> Mono.<User>error(new IllegalArgumentException("El correo electrónico ya está en uso")))
                .switchIfEmpty(Mono.defer(() -> userRepository.saveUser(user)));
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<User> existsByDocumentoIdentificacion(String documentoIdentificacion) {
        return userRepository.findByDocumentoIdentificacion(documentoIdentificacion);
    }

    public Mono<User> validatePassword(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.empty())
                .flatMap(user -> {
                    if (user.getPassword() != null && user.getPassword().equals(password)) {
                        return Mono.just(user);
                    }
                    return Mono.empty();
                });
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
