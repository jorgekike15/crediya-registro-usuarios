package co.com.pragma.usecase.autentication;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class AutenticationUseCaseTest {

    private UserRepository userRepository;
    private AutenticationUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        useCase = new AutenticationUseCase(userRepository);
    }

    @Test
    void login_UsuarioExisteYPasswordCorrecto() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("1234");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.login("test@mail.com", "1234"))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void login_UsuarioExisteYPasswordIncorrecto() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("1234");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.login("test@mail.com", "wrong"))
                .verifyComplete();
    }

    @Test
    void login_UsuarioNoExiste() {
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.login("notfound@mail.com", "1234"))
                .verifyComplete();
    }
}
