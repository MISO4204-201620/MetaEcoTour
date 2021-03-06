package models.reporte;

import java.sql.Date;

/**
 * Created by JoséLuis on 18/04/2016.
 */
public class ComprasDTO {

    private String product_type;
    private Long idproducto;
    private String descripcion;
    private double cantidad;
    private double precio;
    private Long idcompra;
    private Date fechacreacion;
    private Date fechaactualizacion;
    private String estado;
    private String mediopago;
    private Long idusuario ;
    private String nombre;

    ComprasDTO( Long idproducto, String descripcion, double cantidad, double precio, Long idcompra, Date fechacreacion, Date fechaactualizacion, String estado, String mediopago, Long idusuario , String nombre ){
        this.idproducto = idproducto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.idcompra = idcompra;
        this.fechacreacion = fechacreacion;
        this.fechaactualizacion = fechaactualizacion;
        this.estado = estado;
        this.mediopago = mediopago;
        this.idusuario = idusuario;
        this.nombre = nombre;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public Long getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Long idproducto) {
        this.idproducto = idproducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Long getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(Long idcompra) {
        this.idcompra = idcompra;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public Date getFechaactualizacion() {
        return fechaactualizacion;
    }

    public void setFechaactualizacion(Date fechaactualizacion) {
        this.fechaactualizacion = fechaactualizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMediopago() {
        return mediopago;
    }

    public void setMediopago(String mediopago) {
        this.mediopago = mediopago;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
