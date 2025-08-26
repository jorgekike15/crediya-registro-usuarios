package co.com.pragma.usecase.rol;

import co.com.pragma.model.rol.Rol;
import co.com.pragma.model.rol.gateways.RolRepository;
import co.com.pragma.usecase.rol.in.RolUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RolUseCase implements RolUseCasePort {

    private final RolRepository rolRepository;

    @Override
    public Mono<Rol> consultRol(int id) {
        return rolRepository.findById(id);
    }
}
