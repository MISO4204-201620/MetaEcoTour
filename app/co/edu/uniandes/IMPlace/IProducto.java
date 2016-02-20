package co.edu.uniandes.IMPlace;

public interface IProducto {

	/**
	 * Crea un producto con los datos suministrados
	 *
	 * @param idProveedor : Id proveedor asociado
	 * @param nombre : Nombre producto
	 * @param descripcion : Descripción producto
	 * @param precioActual : Precio Actual
	 * @param foto : Binario del archivo de fotos.
	 * @return Long : Id del producto creado.
	 */
	Long crearProducto( Long idProveedor,String nombre, String descripcion, Double precioActual, char foto);

	/**
	 * Actualiza un producto con los datos suministrados
	 *
	 * @param id : Id del producto
	 * @param idProveedor : Id proveedor asociado
	 * @param nombre : Nombre producto
	 * @param descripcion : Descripción producto
	 * @param precioActual : Precio Actual
	 * @param foto : Binario del archivo de fotos.
	 * @return Boolean : Estado de la operación
	 */
	boolean actualizaProducto(Long id, Long idProveedor, String nombre, String descripcion, Double precioActual, char foto);

	/**
	 * Se inactiva el producto
	 * @param idProveedor
	 * @param Id
	 * @return
	 */
	boolean inActivar(Long idProveedor, Long Id);

	/**
	 * Se reactiva el producto
	 * @param idProveedor
	 * @param Id
	 * @return
	 */
	boolean reActivar(Long idProveedor, Long Id);

}