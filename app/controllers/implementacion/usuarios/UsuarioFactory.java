package controllers.implementacion.usuarios;

import com.fasterxml.jackson.databind.JsonNode;
import models.usuario.Administrador;
import models.usuario.Cliente;
import models.usuario.Proveedor;
import models.usuario.Usuario;
import play.libs.Json;

/**
 * Created by camilo on 10/05/16.
 */
public class UsuarioFactory implements UsuarioFactoryMethod {

    @Override
    public Usuario crerUsuario(String tipoUsuario, JsonNode usuarioJson) {
        Usuario usuario = null;

        if("ADMIN".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Administrador.class);
        }else if("PROVIDER".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Proveedor.class);
        }else if("CLIENT".equals(tipoUsuario)){
            usuario = Json.fromJson(usuarioJson, Cliente.class);
        }
        return usuario;
    }
}
