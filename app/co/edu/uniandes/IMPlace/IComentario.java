package co.edu.uniandes.IMPlace;

import java.util.Collection;

public interface IComentario {

	/**
	 * Crea un comentario hecho por un usuario al un servicio
	 * @param idUsuario
	 * @param idCliente
	 * @param comPadre
	 * @param comentario
	 * @return int : id del comentario creado
	 */
	int crearComentario(Long idServicio, Long idUsuario, int comPadre, String comentario);

	/**
	 * Busca los comentarios realizados sobre un servicio
	 * @param idServicio
	 * @return Colleccion de comentarios.
	 */
	Collection<?> getComentariosXServicio(Long idServicio);
	
}