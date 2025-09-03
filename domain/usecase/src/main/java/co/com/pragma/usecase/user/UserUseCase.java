package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserDocumentValidationResponse;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.in.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {

    private static final String USUARIO_NO_CONCUERDA = "Documento de identificación no concuerda con el usuario logeado";
    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {

        return userRepository.findByEmail(user.getEmail())
                .flatMap(existsUser -> Mono.<User>error(
                        new IllegalArgumentException("El correo electrónico ya está en uso")))
                .switchIfEmpty(Mono.defer(() -> userRepository.saveUser(user)));
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<UserDocumentValidationResponse> existsByDocumentoIdentificacion(String documentoIdentificacion, String emailUserLogin) {
        return userRepository.findByEmail(emailUserLogin)
                .flatMap(user -> {
                    if (user.getDocumentoIdentificacion().equals(documentoIdentificacion)) {
                        return Mono.just(new UserDocumentValidationResponse(true, ""));
                    }
                    return Mono.just(new UserDocumentValidationResponse(false,
                            USUARIO_NO_CONCUERDA));
                })
                .switchIfEmpty(Mono.just(new UserDocumentValidationResponse(false, USUARIO_NO_CONCUERDA)));
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Mono<User> findByDocument(String document) {
        return userRepository.findByDocumentoIdentificacion(document);
    }

}
