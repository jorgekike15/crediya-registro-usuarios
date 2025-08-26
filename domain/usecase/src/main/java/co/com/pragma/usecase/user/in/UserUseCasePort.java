package co.com.pragma.usecase.user.in;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserUseCasePort {

    Mono<User> saveUser(User user);

    Flux<User> findAllUsers();

    Mono<User> existsByDocumentoIdentificacion(String documentoIdentificacion);

}
