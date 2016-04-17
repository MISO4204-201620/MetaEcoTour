package controllers.implementacion.usuarios;

import com.fasterxml.jackson.databind.JsonNode;

import controllers.contratos.usuarios.IUsuarios;
import models.mensajeria.Comentario;
import models.usuario.Administrador;
import models.usuario.Cliente;
import models.usuario.Proveedor;
import models.usuario.Usuario;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.provider_details;
import views.html.clientes_details;

import java.util.List;


/**
 * Created by camilo on 24/03/16.
 */
public class UsuariosController extends Controller {
    private static IUsuarios usuarios = new Usuarios();

    @Transactional
    public Result crearUsuario() {

        JsonNode json = request().body().asJson();

        String tipoUsuario=json.get("tipoUsuario").textValue();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"No se ha podido crear el usuario\"}");
        JsonNode usuarioJson =json.get("usuario");

        Usuario usuario = null;
        Usuario usuarioGuardado=null;

        if("ADMIN".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Administrador.class);
        }else if("PROVIDER".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Proveedor.class);
        }else if("CLIENT".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Cliente.class);
        }

        if(usuario != null){
            usuarioGuardado=usuarios.crearUsuario(usuario, tipoUsuario);
            if(usuarioGuardado !=null) {
                respuesta = Json.toJson(usuarioGuardado);
            }
        }

        return ok(respuesta);

    }

    @Transactional(readOnly=true)
    public Result getUsuariosByPageByType(Integer numPagina,String tipo){
        List<Usuario> usuariosList =usuarios.getUsuariosByTypeAndPage(numPagina,tipo);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"La página solicitada no existe\"}");
        if(usuariosList!=null){
            respuesta= Json.toJson(usuariosList);
        }
        return ok(respuesta);
    }

    @Transactional(readOnly=true)
    public Result getUsuariosByType(String tipo){
        List<Usuario> usuariosList =usuarios.getUsuariosByType(tipo);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"La página solicitada no existe\"}");
        if(usuariosList!=null){
            respuesta= Json.toJson(usuariosList);
        }
        return ok(respuesta);
    }

    @Transactional(readOnly=true)
    public Result getUsuarioById(Long providerId, Long tipo) {

        if(tipo == 1)
        {
            return ok(Json.toJson(usuarios.getUsuarioById(providerId)));
        }
        else if(tipo == 2)
        {
            return ok(provider_details.render((Proveedor) usuarios.getUsuarioById(providerId)));
        }else if(tipo == 3){
            return ok(clientes_details.render((Cliente) usuarios.getUsuarioById(providerId), false));
        }else{
            return ok();
        }
    }

    @Transactional(readOnly=true)
    public Result getCuentaExterno() {
        return ok(clientes_details.render(null, true));
    }


    @Transactional
    public Result deleteUserById(Long userId){
        Usuario usuario= usuarios.deleteUserById(userId);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El Usuario no existe\"}");

        if(usuario!=null){
            respuesta= Json.toJson(usuario);
        }
        return ok(respuesta);
        //return ok(crudproveedores.render());
        //return ok();

    }

    @Transactional(readOnly=true)
    public Result getUsuariosInteraccionMensajes(Long id, String type) {
        Comentario.Tipo tipo = Comentario.Tipo.MENSAJE;
        if (type.equals(Comentario.Tipo.COMENTARIO.name()))
        {
            tipo = Comentario.Tipo.COMENTARIO;
        }
        if (type.equals(Comentario.Tipo.PREGUNTA.name()))
        {
            tipo = Comentario.Tipo.PREGUNTA;
        }
        return ok(Json.toJson(usuarios.getUsuariosInteraccionMensajes(id, tipo)));
    }
}
