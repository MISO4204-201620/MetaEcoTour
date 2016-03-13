package controllers.implementacion.usuarios;



import controllers.contratos.usuarios.IUsuarios;
import models.usuario.Usuario;
import play.db.jpa.Transactional;
import play.mvc.Http;
import play.mvc.Security;

/**
 * Created by manuel on 26/02/16.
 */
public class Secured extends Security.Authenticator {

    private static IUsuarios usuarios = new Usuarios();

    @Override
    @Transactional
    public String getUsername(Http.Context ctx) {

        Usuario usuario = null;

        String[] authTokenHeaderValues = ctx.request().headers().get(SecurityController.AUTH_TOKEN_HEADER);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            usuario = usuarios.findByAuthToken(authTokenHeaderValues[0]);
            if (usuario != null) {
                ctx.args.put("user", usuario);
                return usuario.getCorreo();
            }
        }
        return null;
    }

    @Override
    public play.mvc.Result onUnauthorized(Http.Context ctx) {
        return super.onUnauthorized(ctx);
    }
}
