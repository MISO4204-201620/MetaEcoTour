package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IItemServicio;
import controllers.contratos.catalogos.IPaquete;
import controllers.contratos.catalogos.IProducto;
import models.catalogo.ItemServicio;
import models.catalogo.Paquete;
import models.catalogo.Servicio;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoséLuis on 28/02/2016.
 */
public class Paquetes implements IPaquete {
    private static IItemServicio itemServicios = new ItemServicios();
    private static IProducto productos = new Productos();

    @Override
    public List<ItemServicio> getItemServicios(Long idPaquete) {
        ItemServicios itmServ = new ItemServicios();
        return itmServ.getItemsByPaquetes(idPaquete);
    }

    @Override
    public List<Paquete> getPaquetes() {
        return null;
    }



    @Override
    public List<Servicio> getServicios(Long idPaquete) {
        List<ItemServicio> itmsServ = getItemServicios(idPaquete);
        List<Servicio> servicios = new ArrayList<Servicio>();
        for (ItemServicio iServ : itmsServ ) {
            Servicio servicioTemp = JPA.em().find(Servicio.class, iServ.getIdServicio());
            if (servicioTemp != null){
                servicios.add(servicioTemp);
            }
        }
        return servicios;
    }

    @Transactional(readOnly=true)
    public ItemServicio addServicioToPaquete(Long idProducto, Long idServ, int cantidad) {
        ItemServicio itmSrv = new ItemServicio();
        itmSrv.setIdProducto(idProducto);
        itmSrv.setIdServicio(idServ);
        itmSrv.setCantidad(cantidad);
        return itemServicios.save(itmSrv);
    }

    @Override
    public ItemServicio removeServicioFromPaquete(Long isPaquete, Long idServ) {
       return itemServicios.delete(isPaquete,idServ);
    }

    @Transactional(readOnly=true)
    public Paquete save(Paquete paquete) {
        return (Paquete) productos.save(paquete);
    }

    @Transactional(readOnly=true)
    public Paquete delete(long l) {
        return (Paquete) productos.delete(l);
    }
}
