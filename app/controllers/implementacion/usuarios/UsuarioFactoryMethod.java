package controllers.implementacion.usuarios;

import com.fasterxml.jackson.databind.JsonNode;
import models.usuario.Usuario;

/**
 * Created by camilo on 10/05/16.
 */
public interface UsuarioFactoryMethod {

    public Usuario crerUsuario(String tipoUsuario, JsonNode usuarioJson);
}
