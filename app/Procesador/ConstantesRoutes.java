package Procesador;

/**
 * Created by camilo on 14/05/16.
 */
public class ConstantesRoutes {

    public static final String SOCIAL_LOGIN_ROUTE = "POST        /sociallogin                          controllers.implementacion.usuarios.SocialNetSecurity.login()";
    public static final String SEARCH_REPORT_ROUTE = "";

    public static final String FILE_ROUTE = "GET         /*file                          controllers.Assets.versioned(path=\"/public\", file: Asset)";

    public static final String MENSAJERIA_USUARIOS_ROUTE = "GET         /api/mensajeria/usuarios/:id/:tipo    controllers.implementacion.usuarios.UsuariosController.getUsuariosInteraccionMensajes(id: Long, tipo: String)";
    public static final String MENSAJERIA_MENSAJES_ROUTE = "GET         /api/mensajeria/msn/:idOrigen/:idDestino    controllers.implementacion.mensajerias.ComentariosController.getMensajesByUsuarios(idOrigen: Long, idDestino: Long)";

    public static final String CALIFICACION_USUARIOS_ROUTE = "GET         /api/calificacion/usr/:usuarioId        controllers.implementacion.compras.CalificacionesController.getCalificaionesByUsuario(usuarioId:Long)";
    public static final String CALIFICACION_SERVICIO_ROUTE = "GET         /api/calificacion/prd/:productId        controllers.implementacion.compras.CalificacionesController.getCalificaionesByServicio(productId:Long)";
    public static final String CREACION_CALIFICACION_ROUTE = "POST        /api/calificacion/                      controllers.implementacion.compras.CalificacionesController.save()";






}
