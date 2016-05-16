package Procesador;

/**
 * Created by camilo on 14/05/16.
 */
public class ConstantesRoutes {

    public static final String SOCIAL_LOGIN_ROUTE = "POST        /sociallogin                          controllers.implementacion.usuarios.SocialNetSecurity.login()";
    public static final String SEARCH_REPORT_ROUTE = "GET         /api/busqueda/tipo/:tipo/provider/:providerId/fechaInicio/:fechaInicio/fechaFin/:fechaFin/cuenta/:cuenta        controllers.implementacion.catalogos.BusquedasController.getBusquedasByTypeAndDate(tipo:String, providerId: Long, fechaInicio : String, fechaFin : String, cuenta:String)";
    public static final String SEARCH_REPORT_SOLD_ROUTE = "GET        /api/rptCompra/:entidad/:id                           controllers.implementacion.reportes.ReporteComprasController.getComprasConsolidado(entidad : String, id : Long)";
    public static final String SEARCH_REPORT_SOLD_ROUTE_DETAIL = "GET        /api/rptCompra/:entidad/:id/:idProducto               controllers.implementacion.reportes.ReporteComprasController.getComprasDetalle(entidad : String, id : Long, idProducto : Long)";


    public static final String FILE_ROUTE = "GET         /*file                          controllers.Assets.versioned(path=\"/public\", file: Asset)";

    public static final String MENSAJERIA_USUARIOS_ROUTE = "GET         /api/mensajeria/usuarios/:id/:tipo    controllers.implementacion.usuarios.UsuariosController.getUsuariosInteraccionMensajes(id: Long, tipo: String)";
    public static final String MENSAJERIA_MENSAJES_ROUTE = "GET         /api/mensajeria/msn/:idOrigen/:idDestino    controllers.implementacion.mensajerias.ComentariosController.getMensajesByUsuarios(idOrigen: Long, idDestino: Long)";

    public static final String CALIFICACION_USUARIOS_ROUTE = "GET         /api/calificacion/usr/:usuarioId        controllers.implementacion.compras.CalificacionesController.getCalificaionesByUsuario(usuarioId:Long)";
    public static final String CALIFICACION_SERVICIO_ROUTE = "GET         /api/calificacion/prd/:productId        controllers.implementacion.compras.CalificacionesController.getCalificaionesByServicio(productId:Long)";
    public static final String CREACION_CALIFICACION_ROUTE = "POST        /api/calificacion/                      controllers.implementacion.compras.CalificacionesController.save()";






}
