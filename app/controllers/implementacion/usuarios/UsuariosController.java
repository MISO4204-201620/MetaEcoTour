package controllers.implementacion.usuarios;

import Activos.Mensajeria;
import com.fasterxml.jackson.databind.JsonNode;

import controllers.contratos.usuarios.IUsuarios;
import models.mensajeria.Comentario;
import models.usuario.*;
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
    public static final String AUTH_TOKEN = "authToken";

    @Transactional
    public Result crearUsuario() {

        JsonNode json = request().body().asJson();

        String tipoUsuario=json.get("tipoUsuario").textValue();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"No se ha podido crear el usuario\"}");
        JsonNode usuarioJson =json.get("usuario");

        UsuarioFactoryMethod usuarioFactoryMethod = new UsuarioFactory();
        Usuario usuario = usuarioFactoryMethod.crerUsuario(tipoUsuario,usuarioJson);
        Usuario usuarioGuardado=null;

        /*
        if("ADMIN".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Administrador.class);
        }else if("PROVIDER".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Proveedor.class);
        }else if("CLIENT".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Cliente.class);
        }
        */

        if(usuario != null){
            usuarioGuardado=usuarios.crearUsuario(usuario, tipoUsuario);
            if(usuarioGuardado !=null) {
                String authToken = usuarios.gestionarToken(usuarioGuardado, true);
                response().setCookie(AUTH_TOKEN, authToken);
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
    @Mensajeria(true)     public Result getUsuariosInteraccionMensajes(Long id, String type) {

        List<UsuarioDTO> usuarioDTOs = (List<UsuarioDTO>)ctx().args.get("usuarioDTOs");
        if (usuarioDTOs != null){
            return ok(Json.toJson(usuarioDTOs));
        }
        return badRequest("No existe el servicio para mensajeria");

    }
}
