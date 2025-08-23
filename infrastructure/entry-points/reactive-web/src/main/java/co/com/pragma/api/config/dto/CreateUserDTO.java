package co.com.pragma.api.config.dto;

public record CreateUserDTO(String nombre, String apellido, String email, String documentoIdentificacion, String telefono, Double salarioBase) {
}
