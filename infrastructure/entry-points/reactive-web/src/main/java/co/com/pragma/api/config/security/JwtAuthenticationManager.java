package co.com.pragma.api.config.security;

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

    public JwtAuthenticationManager(JwtUtil jwtUtil, UserUseCase userUseCasePort) {
        this.jwtUtil = jwtUtil;
        this.userUseCasePort = userUseCasePort;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        try {
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRol(token);
            return userUseCasePort.findByEmail(email)
                    .flatMap(user -> Mono.just(
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                                    )
                            )
                    ));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}