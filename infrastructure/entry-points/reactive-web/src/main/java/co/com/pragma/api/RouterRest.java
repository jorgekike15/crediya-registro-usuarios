package co.com.pragma.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenGETCreateUser",
                    operation = @Operation(
                            summary = "Crear usuario",
                            description = "Crea un nuevo usuario en el sistema",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos para crear usuario",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = co.com.pragma.api.config.dto.CreateUserDTO.class),
                                            examples = @ExampleObject(
                                                    value = """
                                                            {
                                                              "nombre": "Juan",
                                                              "apellido": "Pérez",
                                                              "email": "juan.perez@correo.com",
                                                              "documentoIdentificacion": "123456789",
                                                              "telefono": "3001234567",
                                                              "salarioBase": 5000000
                                                            }
                                                            """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Usuario creado exitosamente",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = co.com.pragma.api.config.dto.UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Error de validación"),
                                    @ApiResponse(responseCode = "409", description = "Conflicto"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/all",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGETGetAllUsers",
                    operation = @Operation(
                            summary = "Obtener todos los usuarios",
                            description = "Devuelve la lista de todos los usuarios",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de usuarios",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = co.com.pragma.api.config.dto.UserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::listenGETCreateUser)
                .andRoute(GET("/api/v1/usuarios/all"), handler::listenGETGetAllUsers);
    }
}