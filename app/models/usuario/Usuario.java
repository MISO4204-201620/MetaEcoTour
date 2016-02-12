package models.usuario;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by manuel on 10/02/16.
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "usuarioGen")
    @SequenceGenerator(name = "usuarioGen",
            sequenceName = "usuario_seq")
    private long id;

    @Column(nullable=false)
    private String nombre;

    @Column(nullable=false)
    private String documento;

    @Column(nullable=false)
    private String correo;

    @Column(nullable=false)
    private String tipoDoc;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }
}
