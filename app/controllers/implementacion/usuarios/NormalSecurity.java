package controllers.implementacion.usuarios;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.usuarios.IUsuarios;
import models.usuario.Administrador;
import models.usuario.Cliente;
import models.usuario.Proveedor;
import models.usuario.Usuario;
import play.data.validation.Constraints;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;

/**
 * Created by camilo on 24/04/16.
 */
public class NormalSecurity extends Controller implements SecurityStrategy{

    private static IUsuarios usuarios = new Usuarios();

    @Transactional
    public Result login() {

        JsonNode json = request().body().asJson();
        LoginUsuario loginUsuario = Json.fromJson(json, LoginUsuario.class);

        Usuario usuario = usuarios.findByCorreoAndClave(loginUsuario.correo, loginUsuario.clave);
        if(usuario == null){
            return unauthorized();
        }else {
            String authToken = usuarios.gestionarToken(usuario, true);
            response().setCookie(AUTH_TOKEN, authToken);
            if (usuario instanceof Proveedor){
                usuario.setTipo("Proveedor");
            } else if (usuario instanceof Administrador){
                usuario.setTipo("Administrador");
            } else if (usuario instanceof Cliente){
                usuario.setTipo("Cliente");
            }
            return ok(Json.toJson(usuario));
        }
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @Override
    public Result logout() {
        response().discardCookie(AUTH_TOKEN);
        usuarios.gestionarToken(getUsuario(), false);
        return ok(login.render("Login MetaEcoTour")); //Pagina de Logueo
    }

    public static Usuario getUsuario() {
        return (Usuario) Http.Context.current().args.get("user");
    }

        public static class LoginUsuario {

        @Constraints.Required
        @Constraints.Email
        public String correo;

        @Constraints.Required
        public String clave;

    }
}
