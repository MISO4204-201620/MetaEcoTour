package Activos;

import controllers.contratos.mensajerias.IComentario;
import controllers.contratos.usuarios.IUsuarios;
import controllers.implementacion.mensajerias.Comentarios;
import controllers.implementacion.usuarios.Usuarios;
import models.mensajeria.Comentario;
import models.mensajeria.MensajeDTO;
import models.usuario.UsuarioDTO;
import play.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

/**
 * Created by crimago on 13/05/16.
 */
public class MensajeriaAction extends Action<Mensajeria> {

    private static IComentario comentarios = new Comentarios();
    private static IUsuarios usuarios = new Usuarios();
    @Override
    @play.db.jpa.Transactional
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        Logger.info("Calling action for {}", ctx);

        List<MensajeDTO> mensajesUsuario = null;
        List<UsuarioDTO> usuarioDTOs = null;
        String[] paths = ctx.request().path().split("/");
        String primero = "";
        String segundo = "";
        if (paths.length > 5){
            primero = paths[4];
            segundo = paths[5];
        }

        String metodo = (String)ctx.args.get("ROUTE_ACTION_METHOD");

        if (configuration.value()){

            if ("getMensajesByUsuarios".equals(metodo)){
                long segundoL =  Long.valueOf(segundo);
                long primeroL =  Long.valueOf(primero);
                mensajesUsuario = comentarios.getMensajesByUsuarios(primeroL, segundoL);
                ctx.args.put("mensajesUsuario", mensajesUsuario);

            } else {
                long primeroL =  Long.valueOf(primero);
                Comentario.Tipo tipo = Comentario.Tipo.MENSAJE;
                if (segundo.equals(Comentario.Tipo.COMENTARIO.name()))
                {
                    tipo = Comentario.Tipo.COMENTARIO;
                }
                if (segundo.equals(Comentario.Tipo.PREGUNTA.name()))
                {
                    tipo = Comentario.Tipo.PREGUNTA;
                }
                usuarioDTOs = usuarios.getUsuariosInteraccionMensajes(primeroL, tipo);
                ctx.args.put("usuarioDTOs", usuarioDTOs);

            }
        }

        return delegate.call(ctx);
    }
}

