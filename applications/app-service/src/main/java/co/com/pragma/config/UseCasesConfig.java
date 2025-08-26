package co.com.pragma.config;

import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
import co.com.pragma.usecase.user.in.UserUseCasePort;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "co.com.pragma.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    private final UserRepository userRepository;

    public UseCasesConfig(UserRepository solicitanteRepository) {
        this.userRepository = solicitanteRepository;
    }

    @Bean
    @Primary
    public UserUseCasePort userUseCasePort(){
        return new UserUseCase(userRepository);
    }

}
