package controllers.contratos.mensajerias;

import models.mensajeria.Comentario;

import java.util.List;

/**
 * Created by crimago on 11/03/16.
 */
public interface IComentario {

    Comentario getComentarioById(Long id);

    Comentario save(Comentario comentario);

    Comentario delete(Long id);

    List<Object> getComentariosByIdProducto(Long id, int page);

    List<Comentario> getComentariosByIdUsuario(Long id);

    List<Comentario> getSubComentarios(Long id);

    List<Object> getComentariosByIdPadreComentario(Long id);
}
