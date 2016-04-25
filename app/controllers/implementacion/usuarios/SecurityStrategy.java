package controllers.implementacion.usuarios;

import models.usuario.Usuario;
import play.db.jpa.Transactional;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by camilo on 24/04/16.
 */
public interface SecurityStrategy{
    //public static IUsuarios usuarios = new Usuarios();
    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";

    @Transactional
    public Result login();

    @Transactional
    @Security.Authenticated(Secured.class)
    public Result logout();


}
