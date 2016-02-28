package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IPaquete;
import models.catalogo.ItemServicio;
import models.catalogo.Paquete;
import models.catalogo.Servicio;
import play.db.jpa.JPA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoséLuis on 28/02/2016.
 */
public class Paquetes implements IPaquete {
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

    @Override
    public Paquete save(Paquete paquete) {
        return null;
    }

    @Override
    public Paquete delete(long l) {
        return null;
    }
}
