package models.usuario;

/**
 * Created by crimago on 17/04/16.
 */
public class UsuarioDTO {

    String id;
    String nombre;

    public UsuarioDTO(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
