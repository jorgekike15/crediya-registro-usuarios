package co.com.pragma.usecase.autentication.in;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;

public interface AutenticationUseCasePort {
    Mono<User> login(String email, String password);
}
