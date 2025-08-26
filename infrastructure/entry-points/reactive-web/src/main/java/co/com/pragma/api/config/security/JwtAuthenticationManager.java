package co.com.pragma.api.config.security;

import co.com.pragma.model.user.gateways.UserRepository;
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
    private final UserRepository userRepository;

    public JwtAuthenticationManager(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        try {
            String email = jwtUtil.extractUsername(token);
            return userRepository.findByEmail(email).flatMap(user ->
                    Mono.just(new UsernamePasswordAuthenticationToken(email, null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getIdRol())))));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}