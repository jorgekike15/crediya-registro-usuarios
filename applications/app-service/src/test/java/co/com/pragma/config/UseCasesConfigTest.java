package co.com.pragma.config;

import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UseCasesConfigTest {

    @Test
    void testUserUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            UserUseCase userUseCase = context.getBean(UserUseCase.class);
            assertNotNull(userUseCase, "El bean UserUseCase debe estar presente en el contexto");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {
        @Bean
        public co.com.pragma.model.user.gateways.UserRepository userRepository() {
            return org.mockito.Mockito.mock(co.com.pragma.model.user.gateways.UserRepository.class);
        }
    }
}