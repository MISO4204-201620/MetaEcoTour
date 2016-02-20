package co.edu.uniandes.IMPlace;

import java.util.Collection;

public interface IServicio {

	/**
	 * Crea un servicio con los datos suministrados
	 *
	 * @param idProveedor
	 *            : Id proveedor asociado
	 * @param nombre
	 *            : Nombre Servicio
	 * @param descripcion
	 *            : Descripción Servicio
	 * @param precioActual
	 *            : Precio Actual
	 * @param foto
	 *            : Binario del archivo de fotos.
	 * @return Long : Id del servicio creado.
	 */
	Long crearServicio(Long idProveedor, String nombre, String descripcion, Double precioActual, char foto);

	/**
	 * Actualiza un servicio con los datos suministrados
	 * 
	 * @param id
	 *            : Id del servicio
	 * @param idProveedor
	 *            : Id proveedor asociado
	 * @param nombre
	 *            : Nombre Servicio
	 * @param descripcion
	 *            : Descripcion Servicio
	 * @param precioActual
	 *            : Precio Actual
	 * @param foto
	 *            : Binario del archivo de fotos.
	 * @return Boolean : Estado de la operación
	 */
	boolean actualizaServicio(Long id, Long idProveedor, String nombre, String descripcion, Double precioActual,
			char foto);

	/**
	 * Busca el servicio asociado al id
	 * 
	 * @param id
	 *            Servicio
	 * @return Objeto representativo del servicio
	 */
	Object getServicioXId(Long idProveedor, Long id);

	/**
	 * Busca todos los servicios asociados a un proveedor
	 * 
	 * @param idProveedor
	 * @return Colleccion de Servicios relacionados al proveedor
	 */
	Collection<?> getServicioXProveedor(Long idProveedor);

	/**
	 * Busca todos los servicios asociados a un paquete
	 * 
	 * @param idProveedor
	 * @return Colleccion de Servicios relacionados al paquete
	 */
	Collection<?> getServicioXPaquete(Long idProveedor, Long idPaquete);

	/**
	 * Relaciona el servicio con un paquete.
	 * 
	 * @param idProveedor
	 * @param idPaquete
	 * @param idServicio
	 * @param cantidad
	 * @return Boolean : Estado de la operacion
	 */
	boolean adicionaServicioPaquete(Long idProveedor, Long idPaquete, Long idServicio, Double cantidad);

	/**
	 * Calcula y retorna el promedio de las calificaciones del servicio
	 * @param id
	 * @param idProveedor
	 * @return Duble : Calificacion promedio.
	 */
	Double getCalificacionPromedio(Long id, Long idProveedor);
	
	
	
	/**s
	 * Se inactiva el servicio
	 * @param idProveedor
	 * @param Id
	 * @return
	 */
	boolean inActivar(Long idProveedor, Long Id);

	/**
	 * Se reactiva el servicio
	 * @param idProveedor
	 * @param Id
	 * @return
	 */
	boolean reActivar(Long idProveedor, Long Id);

}