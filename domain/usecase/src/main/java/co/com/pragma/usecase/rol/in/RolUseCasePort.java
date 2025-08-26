package co.com.pragma.usecase.rol.in;

import co.com.pragma.model.rol.Rol;
import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;

public interface RolUseCasePort {
    Mono<Rol> consultRol(int id);
}
