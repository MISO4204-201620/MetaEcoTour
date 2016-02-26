package controllers.implementacion.usuarios;

import models.usuario.Usuario;

/**
 * Created by manuel on 26/02/16.
 */
public interface IUsuarios {

    public Usuario findByAuthToken (String authToken);

    public Usuario findByCorreoAndClave (String correo, String clave);

    public String gestionarToken(Usuario usuario, boolean estado);
}
