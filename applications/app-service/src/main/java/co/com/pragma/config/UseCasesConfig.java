package co.com.pragma.config;

import co.com.pragma.model.rol.gateways.RolRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.rol.RolUseCase;
import co.com.pragma.usecase.rol.in.RolUseCasePort;
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
    private final RolRepository rolRepository;

    public UseCasesConfig(UserRepository solicitanteRepository, RolRepository rolRepository) {
        this.userRepository = solicitanteRepository;
        this.rolRepository = rolRepository;
    }

    @Bean
    @Primary
    public UserUseCasePort userUseCasePort(){
        return new UserUseCase(userRepository);
    }

    @Bean
    @Primary
    public RolUseCasePort rolUseCasePort(){
        return new RolUseCase(rolRepository);
    }

}
