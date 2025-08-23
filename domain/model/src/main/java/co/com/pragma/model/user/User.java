package co.com.pragma.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String documentoIdentificacion;
    private String telefono;
    private int idRol;
    private double salarioBase;

}
