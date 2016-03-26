package models.catalogo;

import javax.persistence.*;

/**
 * Created by JoséLuis on 21/03/2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Atributo.findByProductId", query="SELECT a FROM Atributo a where a.idProducto = :idProducto order by  idAtributo")
})
public class Atributo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "atributoGen")
    @SequenceGenerator(name = "atributoGen",
            sequenceName = "atributo_seq")
    private Long idAtributo;
    @Column(nullable=false)
    private Long idProducto;
    @Column(nullable=false)
    private String nombre;
    @Column(nullable=true)
    private String tipo;
    @Column(nullable=false)
    private String valor;

    public Long getIdAtributo() {
        return idAtributo;
    }

    public void setIdAtributo(Long idAtributo) {
        this.idAtributo = idAtributo;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
