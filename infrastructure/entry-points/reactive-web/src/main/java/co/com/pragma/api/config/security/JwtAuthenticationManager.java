package co.com.pragma.api.config.security;

import co.com.pragma.usecase.rol.in.RolUseCasePort;
import co.com.pragma.usecase.user.UserUseCase;
import co.com.pragma.usecase.user.in.UserUseCasePort;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtil jwtUtil;
    private final UserUseCasePort userUseCasePort;
    private final RolUseCasePort rolUseCasePort;

    public JwtAuthenticationManager(JwtUtil jwtUtil, UserUseCase userUseCasePort, RolUseCasePort rolUseCasePort) {
        this.jwtUtil = jwtUtil;
        this.userUseCasePort = userUseCasePort;
        this.rolUseCasePort = rolUseCasePort;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        try {
            String email = jwtUtil.extractUsername(token);
            return userUseCasePort.findByEmail(email)
                    .flatMap(user -> rolUseCasePort.consultRol(user.getIdRol())
                            .map(rol ->
                                    new UsernamePasswordAuthenticationToken(email, null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                            ))
                    );
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}