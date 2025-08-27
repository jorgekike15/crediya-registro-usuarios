package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class UserUseCaseTest {

    private UserRepository userRepository;
    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        useCase = new UserUseCase(userRepository);
    }

    @Test
    void testSaveUseExist() {
        User user = new User(
                "Kate",
                "Beckett",
                "correo@gmail.com",
                "441",
                "9855934710",
                3,
                30,
                "password"
        );

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.saveUser(user))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El correo electrónico ya está en uso"))
                .verify();
    }


    @Test
    void testSaveUser_EmailNotExists() {
        User user = new User(
                "Kate",
                "Beckett",
                "correo@gmail.com",
                "441",
                "9855934710",
                3,
                30,
                "password"
        );

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(userRepository.saveUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.saveUser(user))
                .expectNext(user)
                .verifyComplete();
    }


    @Test
    void testFindByEmail() {
        User user = new User(
                "Kate",
                "Beckett",
                "correo@gmail.com",
                "441",
                "9855934710",
                3,
                30,
                "password"
        );

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.findByEmail(user.getEmail()))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testUserfindAllUsers() {
        User user = new User(
                "Kate",
                "Beckett",
                "correo@gmail.com",
                "441",
                "9855934710",
                3,
                30,
                "password"
        );

        when(userRepository.findAllUsers()).thenReturn(
                Flux.just(user)
        );

        StepVerifier.create(useCase.findAllUsers())
                .expectNext(user)
                .verifyComplete();

    }

    @Test
    void testExistsByDocumentoIdentificacion_UsuarioExisteYDocumentoConcuerda() {
        User user = new User(
                "Kate",
                "Beckett",
                "correo@gmail.com",
                "441",
                "9855934710",
                3,
                30,
                "password"
        );
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.existsByDocumentoIdentificacion("441", "correo@gmail.com"))
                .expectNextMatches(response ->
                        response.isExist() && response.getMessage().isEmpty()
                )
                .verifyComplete();
    }

    @Test
    void testExistsByDocumentoIdentificacion_UsuarioExisteYDocumentoNoConcuerda() {
        User user = new User(
                "Kate",
                "Beckett",
                "correo@gmail.com",
                "441",
                "9855934710",
                3,
                30,
                "password"
        );
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.existsByDocumentoIdentificacion("999", "correo@gmail.com"))
                .expectNextMatches(response ->
                        !response.isExist() && response.getMessage().equals("Documento de identificación no concuerda con el usuario logeado")
                )
                .verifyComplete();
    }

    @Test
    void testExistsByDocumentoIdentificacion_UsuarioNoExiste() {
        when(userRepository.findByEmail("correo@gmail.com")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.existsByDocumentoIdentificacion("441", "correo@gmail.com"))
                .expectNextMatches(response ->
                        !response.isExist() && response.getMessage().equals("Documento de identificación no concuerda con el usuario logeado")
                )
                .verifyComplete();
    }
}
