package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ResourceBundle;

import static org.mockito.Mockito.when;

class UserUseCaseTest {
//
//    private UserRepository userRepository;
//    private UserUseCase useCase;
//
//    @BeforeEach
//    void setUp() {
//        userRepository = Mockito.mock(UserRepository.class);
//        useCase = new UserUseCase(userRepository);
//    }
//
//    @Test
//    void testUserfindAllUsers(){
//        User user = new User(
//                "Kate",
//                "Beckett",
//                "correo@gmail.com",
//                "441",
//                "985-593-4710",
//                "direccion",
//                30,
//                5000000.0
//        );
//
//        when(userRepository.findAllUsers()).thenReturn(
//                Flux.just(user)
//        );
//
//        StepVerifier.create(useCase.findAllUsers())
//                .expectNext(user)
//                .verifyComplete();
//
//    }
//
//    @Test
//    void testExistsByDocumentoIdentificacion(){
//        User user = new User(
//                "Kate",
//                "Beckett",
//                "correo@gmail.com",
//                "441",
//                "985-593-4710",
//                "direccion",
//                30,
//                5000000.0
//        );
//        when(userRepository.findByDocumentoIdentificacion(user.getDocumentoIdentificacion())).thenReturn(
//                Mono.just(user)
//        );
//
//        StepVerifier.create(useCase.existsByDocumentoIdentificacion(user.getDocumentoIdentificacion()))
//                .expectNext(user)
//                .verifyComplete();
//
//    }
//
//    @Test
//    void testSaveUseExist(){
//        User user = new User(
//                "Kate",
//                "Beckett",
//                "correo@gmail.com",
//                "441",
//                "985-593-4710",
//                "direccion",
//                30,
//                5000000.0
//        );
//
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));
//
//        StepVerifier.create(useCase.saveUser(user))
//                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
//                        throwable.getMessage().equals("El correo electrónico ya está en uso"))
//                .verify();
//    }
//
//
//    @Test
//    void testSaveUser_EmailNotExists() {
//        User user = new User(
//                "Kate",
//                "Beckett",
//                "correo@gmail.com",
//                "441",
//                "985-593-4710",
//                "direccion",
//                30,
//                5000000.0
//        );
//
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
//        when(userRepository.saveUser(user)).thenReturn(Mono.just(user));
//
//        StepVerifier.create(useCase.saveUser(user))
//                .expectNext(user)
//                .verifyComplete();
//    }
}
