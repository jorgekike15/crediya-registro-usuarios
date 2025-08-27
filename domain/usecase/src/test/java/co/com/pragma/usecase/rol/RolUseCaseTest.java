package co.com.pragma.usecase.rol;

import co.com.pragma.model.rol.Rol;
import co.com.pragma.model.rol.gateways.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class RolUseCaseTest {

    private RolRepository rolRepository;
    private RolUseCase useCase;

    @BeforeEach
    void setUp() {
        rolRepository = Mockito.mock(RolRepository.class);
        useCase = new RolUseCase(rolRepository);
    }

    @Test
    void consultRol_RolExiste() {
        Rol rol = new Rol(1, "Super admin", "super admin");
        when(rolRepository.findById(1)).thenReturn(Mono.just(rol));

        StepVerifier.create(useCase.consultRol(1))
                .expectNext(rol)
                .verifyComplete();
    }

    @Test
    void consultRol_RolNoExiste() {
        when(rolRepository.findById(2)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.consultRol(2))
                .verifyComplete();
    }
}
