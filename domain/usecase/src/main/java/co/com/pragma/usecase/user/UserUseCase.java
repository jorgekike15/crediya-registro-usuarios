package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {

        return userRepository.findByEmail(user.getEmail())
                .flatMap(existsUser -> Mono.<User>error(new IllegalArgumentException("El correo electrónico ya está en uso")))
                .switchIfEmpty(Mono.defer(() -> userRepository.saveUser(user)));
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

}
