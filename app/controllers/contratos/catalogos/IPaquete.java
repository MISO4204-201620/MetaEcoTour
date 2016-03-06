package controllers.contratos.catalogos;

import models.catalogo.ItemServicio;
import models.catalogo.Paquete;
import models.catalogo.Servicio;

import java.util.List;

/**
 * Created by JoséLuis on 28/02/2016.
 */
public interface IPaquete {
    public List<Paquete> getPaquetes();
    public List<ItemServicio> getItemServicios(Long idPaquete);
    public List<Servicio> getServicios(Long idPaquete);

    public ItemServicio addServicioToPaquete(Long isPaquete, Long idServ,int cantidad);
    public ItemServicio removeServicioFromPaquete(Long isPaquete, Long idServ);

    Paquete save(Paquete paquete);

    Paquete delete(long l);

}
