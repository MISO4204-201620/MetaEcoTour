package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IItemServicio;
import controllers.contratos.catalogos.IProducto;
import controllers.contratos.catalogos.IServicio;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by JoséLuis on 05/03/2016.
 */
public class ServiciosController  extends Controller {
public static IServicio servicios = new Servicios();
    public static IProducto productos = new Productos();
    private static IItemServicio itemServicios = new ItemServicios();

    @Transactional(readOnly=true)
    public Result getServicioById(Long servId) {
        return ok(Json.toJson(servicios.getServicioById(servId)));
    }

    @Transactional(readOnly=true)
    public Result delete(Long servId){
        if(itemServicios.getItemsByServicios(servId).size()>0){
            return ok(Json.toJson(servicios.getServicioById(servId)));
        }else{
            return ok(Json.toJson(servicios.delete(servId)));
        }

    }


    }
