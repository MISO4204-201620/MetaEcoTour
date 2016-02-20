package co.edu.uniandes.IMPlace;

import java.util.Collection;

public interface IBusqueda {

	/**
	 * Busca los servicios que se ajusten a los parametros de entrada. Los
	 * valores null no se tendran en cuenta en el filtro.
	 * 
	 * @param idServicio
	 * @param idProveedor
	 * @param nombre
	 * @param descripcion
	 * @param precioActual
	 * @param precioIni
	 * @param precioFin
	 * @return Colleccion de servicios
	 */
	Collection<?> buscaServicios(Long idServicio, Long idProveedor, String nombre, String descripcion,
			Double precioIni, Double precioFin);

	/**
	 * Busca los servicios que se ajusten a los parametros de entrada. El valor
	 * 0, no se tendran en cuenta en el filtro(muestra todas la
	 * calificaciones).
	 * 
	 * @param calificacion
	 * @return
	 */
	Collection<?> buscaServiciosXCalificacion(int calificacion);

	/**
	 * Busca los productos que se ajusten a los parametros de entrada. Los
	 * valores null no se tendran en cuenta en el filtro.
	 * @param idServicio
	 * @param idProveedor
	 * @param nombre
	 * @param descripcion
	 * @param precioIni
	 * @param precioFin
	 * @return Colleccion de productos
	 */
	Collection<?> buscaProductos(Long idServicio, Long idProveedor, String nombre, String descripcion,Double precioIni, Double precioFin);
	
	/**
	 * Busca los productos que se ajusten a los parametros de entrada. Los
	 * valores null no se tendran en cuenta en el filtro.
	 * @param idServicio
	 * @param idProveedor
	 * @param nombre
	 * @param descripcion
	 * @param precioIni
	 * @param precioFin
	 * @return
	 */
	Collection<?> buscaPaquetes(Long idServicio, Long idProveedor, String nombre, String descripcion,Double precioIni, Double precioFin);
	
}