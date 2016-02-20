package co.edu.uniandes.IMPlace;

import java.util.Collection;

public interface ICalificacion {

	/**
	 * Crear calificacion sobre un servicio
	 * @param idServicio
	 * @param idCliente
	 * @param calificacion
	 * @param comentario
	 * @return
	 */
	boolean crearCalificacion(Long idServicio, Long idCliente, int calificacion, String comentario);
	
	/**
	 * Busca la calificaciones asociadas a un servicio.
	 * @param idServicio
	 * @return Colleccion de servicios
	 */
	Collection<?> buscarCalificacionXServicio(Long idServicio);
	
	/**
	 * Busca la calificaciones asociadas a un cliente.
	 * @param idCliente
	 * @return
	 */
	Collection<?> buscarCalificacionXCliente(Long idCliente);
	
}