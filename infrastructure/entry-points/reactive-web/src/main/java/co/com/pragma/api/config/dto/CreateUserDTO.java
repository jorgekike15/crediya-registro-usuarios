package co.com.pragma.api.config.dto;

import jakarta.validation.constraints.*;

public record CreateUserDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico no es válido") String email,
        String documentoIdentificacion,
        String telefono,
        @NotNull(message = "El salario base es obligatorio")
        @Min(value = 0, message = "El salario base no puede ser menor que 0")
        @Max(value = 15000000, message = "El salario base no puede ser mayor que 15.000.000")
        Double salarioBase
) {}
