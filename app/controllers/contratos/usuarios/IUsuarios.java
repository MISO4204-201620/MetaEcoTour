package controllers.contratos.usuarios;

import models.usuario.Proveedor;
import models.usuario.Usuario;

import java.util.List;

/**
 * Created by manuel on 26/02/16.
 */
public interface IUsuarios {

    public Usuario findByAuthToken (String authToken);

    public Usuario findByCorreoAndClave (String correo, String clave);

    public String gestionarToken(Usuario usuario, boolean estado);

    public Usuario crearUsuario(Usuario usuario, String tipo);

    public List<Usuario> getUsuariosByTypeAndPage(Integer numPage, String tipo);

    public Usuario getProveedorById(Long idProvider);
}
