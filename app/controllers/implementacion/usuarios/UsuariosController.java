package controllers.implementacion.usuarios;

import com.fasterxml.jackson.databind.JsonNode;

import controllers.contratos.usuarios.IUsuarios;
import models.usuario.Administrador;
import models.usuario.Proveedor;
import models.usuario.Usuario;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.provider_details;

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
    public Result getProveedorById(Long providerId, Long tipo) {

        if(tipo == 1)
        {
            return ok(Json.toJson(usuarios.getProveedorById(providerId)));
        }
        else
        {
            return ok(provider_details.render((Proveedor) usuarios.getProveedorById(providerId)));
        }
    }
}
