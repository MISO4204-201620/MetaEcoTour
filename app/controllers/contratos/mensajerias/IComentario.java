package controllers.contratos.mensajerias;

import models.mensajeria.Comentario;
import models.usuario.Usuario;

import java.util.List;

/**
 * Created by crimago on 11/03/16.
 */
public interface IComentario {

    Comentario getComentarioById(Long id);

    Comentario save(Comentario comentario);

    Comentario delete(Long id);

    Comentario getComentario(Long id);

    List<Object> getComentariosByIdProductoAndType(Long id, int page, Comentario.Tipo type);

    List<Comentario> getComentariosByIdUsuario(Long id);

    List<Comentario> getSubComentarios(Long id);

    List<Object> getComentariosByIdPadreComentario(Long id);

    public List<Comentario> getMensajesByUsuarios(Long idOrigen, Long idDestino);

}

