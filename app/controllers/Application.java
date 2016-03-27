package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render());
    }

    public Result creapaqser()
    {
        return ok(crearserpaq.render());
    }

    public Result crudproviders()
    {
        return ok(crudproveedores.render());
    }
    public Result crudclients(){return ok(crudclientes.render());}
    public Result paqser()
    {
        return ok(serpaq.render());
    }

    public Result preguntas(){return ok(preguntas.render());
    }
    public Result shoppingCar()
    {
        return ok(shopping.render());
    }

    public Result login() {
        return ok(login.render("Login MetaEcoTour"));
    }

    public Result catalog() {
        return ok(catalog.render("Catálogo MetaEcoTour"));
    }

}
