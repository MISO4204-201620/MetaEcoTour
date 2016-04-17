package models.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.mensajeria.Comentario;
//import models.mensajeria.Comentario;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
//import java.util.List;

/**
 * Created by manuel on 10/02/16.
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
        @NamedQuery(name="Usuario.findByAuthToken", query="SELECT u FROM Usuario u WHERE u.authToken =:token"),
        @NamedQuery(name="Usuario.findByCorreoAndClave", query="SELECT u FROM Usuario u WHERE u.correo =:correo AND u.clave =:clave")
        //@NamedQuery(name="Usuario.findUserByMensajes", query="SELECT comentario.idUsuarioDestino FROM Usuario u JOIN u.comentarios comentario WHERE u.id =:id AND comentario.tipo =:tipo " +
        //        "UNION SELECT comentariotmp.idUsuario FROM Usuario utmp JOIN utmp.comentariosDestino comentariotmp WHERE comentariotmp.idUsuarioDestino =:id AND comentariotmp.tipo =:tipo ")
        //@NamedQuery(name="Usuario.findUserByMensajes", query="SELECT DISTINCT comentariotmp.idUsuario FROM Usuario utmp JOIN utmp.comentariosDestino comentariotmp WHERE comentariotmp.idUsuarioDestino =:id AND comentariotmp.tipo =:tipo ")

})
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

    @Column(nullable=true)
    private byte[] shaClave;

    @Column(nullable=true)
    private String authToken;

    @Column(nullable=true)
    private String clave;


    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idUsuario")
    @JsonIgnore
    private List<Comentario> comentarios;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idUsuarioDestino")
    @JsonIgnore
    private List<Comentario> comentariosDestino;

    @Transient
    private String tipo;

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

    public byte[] getShaClave() {
        return shaClave;
    }

    public void setShaClave(byte[] shaClave) {
        this.shaClave = shaClave;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) { this.tipo = tipo;}

    public List<Comentario> getComentariosDestino() {
        return comentariosDestino;
    }

    public void setComentariosDestino(List<Comentario> comentariosDestino) {
        this.comentariosDestino = comentariosDestino;
    }
}
