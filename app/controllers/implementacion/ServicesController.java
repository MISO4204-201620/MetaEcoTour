package controllers.implementacion;

import Procesador.Features;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by JoséLuis on 24/04/2016.
 */
public class ServicesController extends Controller {

    @Transactional
    public Result getServices() {
        Features ftr= new Features();
        return ok(Json.toJson(ftr.getVariableFeatures()));
    }

    @Transactional
    public Result getServicesJson() {
        Features ftr= new Features("Json");
        return ok(ftr.getConfigJson());
    }

}
