package co.com.pragma.usecase.autentication;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.autentication.in.AutenticationUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticationUseCase implements AutenticationUseCasePort {

    private final UserRepository userRepository;

    @Override
    public Mono<User> login (String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.empty())
                .flatMap(user -> {
                    if (user.getPassword() != null && user.getPassword().equals(password)) {
                        return Mono.just(user);
                    }
                    return Mono.empty();
                });
    }
}
