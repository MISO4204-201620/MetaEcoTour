package controllers.implementacion.usuarios;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.usuarios.IUsuarios;
import controllers.implementacion.usuarios.Secured;
import controllers.implementacion.usuarios.SecurityStrategy;
import controllers.implementacion.usuarios.Usuarios;
import models.usuario.Usuario;
import play.Configuration;
import play.Play;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;

/**
 * Created by camilo on 24/04/16.
 */
public class SocialNetSecurity extends Controller implements SecurityStrategy{

    private static IUsuarios usuarios = new Usuarios();

    @Transactional
    @Override
    public Result login(){

        Configuration conf = Play.application().configuration();

        boolean autenticacionSocial= conf.getBoolean("social.login");
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El usuario asociado al token no existe\"}");

        if(autenticacionSocial) {
            JsonNode json = request().body().asJson();

            Usuario usuario = usuarios.findBySocialToken(json.get("socialToken").asText());
            if (usuario != null) {
                String authToken = usuarios.gestionarToken(usuario, true);
                response().setCookie(AUTH_TOKEN, authToken);
                try {
                    JSONObject jsonObjectUsuario = new JSONObject(Json.toJson(usuario).toString());
                    jsonObjectUsuario.remove("clave");
                    respuesta = Json.parse(jsonObjectUsuario.toString());
                } catch (JSONException e) {
                    System.out.println("Se ha presentado un error en la autenticación con redes sociales");
                }
            }
        }else{
            respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El servicio no se encuentra activo\"}");
        }
        return ok(respuesta);
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
}
