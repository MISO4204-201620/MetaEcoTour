package controllers.contratos.usuarios;

import models.mensajeria.Comentario;
import models.usuario.Proveedor;
import models.usuario.Usuario;
import models.usuario.UsuarioDTO;

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

    public List<Usuario> getUsuariosByType(String tipo);

    public Usuario getUsuarioById(Long idProvider);

    public Usuario deleteUserById(Long userId);

    public List<UsuarioDTO> getUsuariosInteraccionMensajes (Long id, Comentario.Tipo type);
}
