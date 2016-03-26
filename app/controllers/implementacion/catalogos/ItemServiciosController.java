package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.catalogos.IItemServicio;
import models.catalogo.ItemServicio;
import models.catalogo.Recurso;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.db.jpa.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class ItemServiciosController  extends Controller {
    private static IItemServicio itemServicios = new ItemServicios();


    @Transactional
    public Result save() {
        JsonNode json = request().body().asJson();
        List<ItemServicio> itemsServiciosGuardados = new ArrayList<ItemServicio>();
        Long idProducto = json.get("idProducto").asLong();
        for (JsonNode itemServiciosFromArray : json.withArray("itemServicios")) {
            ItemServicio itemServicio = Json.fromJson(itemServiciosFromArray, ItemServicio.class);

            if (itemServicio != null) {
                itemServicio.setIdProducto(idProducto);

                itemServicio = itemServicios.save(itemServicio);
                itemsServiciosGuardados.add(itemServicio);
            }
        }
        return ok(Json.toJson(itemsServiciosGuardados));
    }

    @Transactional
    public Result delete(String id) {
        return null;
    }

    @Transactional(readOnly=true)
    public Result getItemsByPaquetes(Long idPaquete) {
        return ok(Json.toJson(itemServicios.getItemsByPaquetes(idPaquete)));
    }

    @Transactional(readOnly=true)
    public Result getItemsByServicios(Long idServicio) {
        return ok(Json.toJson(itemServicios.getItemsByServicios(idServicio)));
    }

    @Transactional(readOnly=true)
    public Result getServicioByPaquetes(Long idPaquete) {
        return ok(Json.toJson(itemServicios.getServicioByPaquetes(idPaquete)));
    }


}
