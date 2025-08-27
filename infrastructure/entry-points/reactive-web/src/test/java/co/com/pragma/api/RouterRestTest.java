package co.com.pragma.api;

import co.com.pragma.api.config.dto.CreateUserDTO;
import co.com.pragma.api.config.dto.UserDTO;
import co.com.pragma.api.config.dto.mapper.UserDTOMapper;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Mock
//    private UserUseCase userUseCase;
//
//    @Mock
//    private UserDTOMapper userDTOMapper;
//
//
//    @Test
//    void testFindAllUsersExitoso() {
//        User user = new User(
//                "Kate",
//                "Beckett",
//                "correo@gmail.com",
//                "441",
//                "985-593-4710",
//                3,
//                30,
//                "password123"
//        );
//
//        UserDTO userDTO = new UserDTO(
//                "Kate",
//                "Beckett",
//                "correo@gmail.com",
//                "441",
//                "985-593-4710",
//                5000000.0
//        );
//
//        CreateUserDTO request = new CreateUserDTO("Kate", "Beckett", "correo@gmail.com",
//                "441", "985-593-4710", 5000000.0);
//
//        Mockito.when(userUseCase.findAllUsers()).thenReturn(Flux.just(user));
//        Mockito.when(userDTOMapper.toModel(request)).thenReturn(user);
//        Mockito.when(userDTOMapper.toResponse(user)).thenReturn(userDTO);
//
//        webTestClient.get()
//                .uri("/api/v1/usuarios/all")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$[0].nombre").isEqualTo("Kate")
//                .jsonPath("$[0].apellido").isEqualTo("Beckett")
//                .jsonPath("$[0].email").isEqualTo("correo@gmail.com");
//    }
//
//    @Test
//    void testFindAllUsersFailed() {
//        Mockito.when(userUseCase.findAllUsers()).thenReturn(Flux.error(new RuntimeException("Error interno")));
//
//        webTestClient.get()
//                .uri("/api/v1/usuarios/all")
//                .exchange()
//                .expectStatus().is5xxServerError();
//    }
}
