package co.edu.uniandes.IMPlace;

import java.util.Collection;

public interface IPaquete {

	/**
	 * Crea un paquete con los datos suministrados
	 * @param idProveedor : Id proveedor asociado
	 * @param nombre : Nombre paquete
	 * @param descripcion : Descripcion paquete
	 * @param precioActual : Precio Actual
	 * @param foto : Binario del archivo de fotos.
	 * @return Long : Id del paquete creado.
	 */
	Long crearPaquete(Long idProveedor, String nombre, String descripcion, Double precioActual, char foto);

	/**
	 * Editar un paquete con los datos suministrados
	 *
	 * @param id : Id del Paquete
	 * @param idProveedor : Id proveedor asociado
	 * @param nombre : Nombre paquete
	 * @param descripcion : Descripción paquete
	 * @param precioActual : Precio Actual
	 * @param foto : Binario del archivo de fotos.
	 * @return Long : Id del paquete creado.
	 * @return Boolean : Estado de la operación
	 */
	boolean actualizarPaquete(Long id,Long idProveedor, String nombre, String descripcion, Double precioActual, char foto);

	/**
	 * Adiciona un servicio al paquete
	 *
	 * @param idServicio : Id del Servicio
	 * @return Boolean : Estado de la operación
	 */
	boolean adicionarServicio(Long idProveedor, Long idServicio);

	/**
	 * Busca el paquete asociado al Id
	 * @param id
	 * @return Paquete encontrado.
	 */
	Object getPaqueteXId(Long id);
	
	/**
	 * Busca todos los paquetes asociados a un proveedor
	 * @param idProveedor
	 * @return Collection : Paquetes relacionados con el proveedor
	 */
	Collection<?> getPaquetesXProveedor(Long idProveedor);
	
	/**
	 * Se inactiva el Paquete
	 *
	 * @return Boolean : Estado de la operación
	 */
	boolean inActivar(Long Id);

	/**
	 * Se reactiva el Paquete
	 *
	 * @return Boolean : Estado de la operación
	 */
	boolean reActivar(Long Id);

}