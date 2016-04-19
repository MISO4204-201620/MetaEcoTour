package controllers.implementacion.reportes;

import controllers.contratos.reportes.IReporteCompra;
import models.reporte.ComprasDTO;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by JoséLuis on 17/04/2016.
 */
public class ReporteCompras implements IReporteCompra {
    @Override
    public List<ComprasDTO> getComprasByProveedorConsolidado(Long idProveedor) {
        String query=this.getQueryConsol(1);
        return JPA.em().createQuery(query).setParameter(1,idProveedor).getResultList();
    }

    @Override
    public List<ComprasDTO> getComprasByProveedorDetalle(Long idProveedor, Long idProducto) {
        String query=this.getQueryDetalle(1);
        return JPA.em().createQuery(query).setParameter(1,idProveedor).setParameter(2,idProducto).getResultList();
    }

    @Override
    public List<ComprasDTO> getComprasByUsuarioConsolidado(Long idUsuario) {
        String query=this.getQueryConsol(2);
        return JPA.em().createQuery(query).setParameter(1,idUsuario).getResultList();
    }

    @Override
    public List<ComprasDTO> getComprasByUsuarioDetalle(Long idUsuario, Long idProducto) {
        String query=this.getQueryDetalle(2);
        return JPA.em().createQuery(query).setParameter(1,idUsuario).setParameter(2,idProducto).getResultList();
    }

    private String getQueryConsol(int agrupacion){
        String query="";
        String groupBy="", sum="", where="";
        switch(agrupacion){
            case 1:{//By proveedor
                groupBy=" p.idProveedor , i.idProducto,  p.descripcion ";
                where="  and p.idProveedor = ?1  ";
            }break;
            case 2:{//By usuario
                groupBy=" c.idUsuario , i.idProducto,  p.descripcion ";
                where=" and c.idUsuario = ?1 ";
            }break;
        }
        sum="sum(i.cantidad) as cantidad, sum(i.cantidad * i.precio) as precio";

        query = query + "SELECT  ";
        query = query + groupBy;
        query = query + " , ";
        query = query + sum;
        query = query + "  FROM Compra c,                   ";
        query = query + "  ItemCompra i,                    ";
        query = query + "  Producto p,                      ";
        query = query + "  Usuario u                        ";
        query = query + "  where i.idCompra=c.idCompra      ";
        query = query + "  and i.idProducto = p.id          ";
        query = query + "  and u.id = c.idUsuario           ";
        query = query + where;
        query = query + " group by "+groupBy;

        return query;
    }

    private String getQueryDetalle(int agrupacion){
        String query="";
        String  where="";
        switch(agrupacion){
            case 1:{//By proveedor
                where="  and p.idProveedor = ?1 and i.idProducto = ?2 ";
            }break;
            case 2:{//By usuario
                where=" and c.idUsuario = ?1 and i.idProducto = ?2 ";
            }break;
        }

        query = query + "SELECT  i.idProducto                     ";
        query = query + ", p.descripcion                    ";
        query = query + ", i.cantidad                       ";
        query = query + ", i.precio                         ";
        query = query + ", c.idCompra                       ";
        query = query + ", c.FechaCreacion                  ";
        query = query + ", c.FechaActualizacion             ";
        query = query + ", c.estado                         ";
        query = query + ", c.medioPago                      ";
        query = query + ", c.idUsuario                      ";
        query = query + ", u.nombre                         ";
        query = query + "  FROM Compra c,                   ";
        query = query + "  ItemCompra i,                    ";
        query = query + "  Producto p,                      ";
        query = query + "  Usuario u                        ";
        query = query + "  where i.idCompra=c.idCompra      ";
        query = query + "  and i.idProducto = p.id          ";
        query = query + "  and u.id = c.idUsuario           ";
        query = query + where;

        return query;
    }

}
