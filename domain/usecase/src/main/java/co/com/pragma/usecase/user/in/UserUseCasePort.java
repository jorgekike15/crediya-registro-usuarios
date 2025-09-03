package co.com.pragma.usecase.user.in;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserDocumentValidationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserUseCasePort {

    Mono<User> saveUser(User user);

    Flux<User> findAllUsers();

    Mono<UserDocumentValidationResponse> existsByDocumentoIdentificacion(String documentoIdentificacion, String emailUserLogin);

    Mono<User> findByEmail(String email);

    Mono<User> findByDocument(String document);

}
