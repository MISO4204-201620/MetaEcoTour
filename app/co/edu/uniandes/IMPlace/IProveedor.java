package co.edu.uniandes.IMPlace;

public interface IProveedor {

	/**
	 * Crea un proveedor
	 * @param descr
	 * @return Long : Id del proveedor creado.
	 */
	Long crearProveedor(String descr);

	/**
	 * Se inactiva el proveedor
	 *
	 * @return Boolean : Estado de la operación
	 */
	boolean inActivar(Long Id);

	/**
	 * Se reactiva el proveedor
	 *
	 * @return Boolean : Estado de la operación
	 */
	boolean reActivar(Long Id);

	
}