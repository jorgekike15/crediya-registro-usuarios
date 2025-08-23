package co.com.pragma.r2dbc.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("usuarios")
public class UserEntity {

    @Id
    private int id;
    private String documentoIdentificacion;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private int idRol;
    private double salarioBase;
}
