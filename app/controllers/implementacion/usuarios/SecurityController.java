package controllers.implementacion.usuarios;

import com.fasterxml.jackson.databind.JsonNode;
import models.usuario.Usuario;

import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by manuel on 26/02/16.
 */
public class SecurityController extends Controller {

    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";
    private static IUsuarios usuarios = new Usuarios();

    public static Usuario getUsuario() {
        return (Usuario) Http.Context.current().args.get("user");
    }


    public Result login() {

        JsonNode json = request().body().asJson();
        Login login = Json.fromJson(json, Login.class);

        Usuario usuario = usuarios.findByCorreoAndClave(login.correo, login.clave);
        if(usuario == null){
            return unauthorized();
        }else {
            String authToken = usuarios.gestionarToken(usuario, true);
            response().setCookie(AUTH_TOKEN, authToken);
            return ok(Json.toJson(usuario));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result logout() {
        response().discardCookie(AUTH_TOKEN);
        usuarios.gestionarToken(getUsuario(), false);
        return redirect("/"); //Pagina de Logueo
    }

    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String correo;

        @Constraints.Required
        public String clave;

    }

}
