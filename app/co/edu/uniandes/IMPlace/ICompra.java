package co.edu.uniandes.IMPlace;

import java.util.Collection;

public interface ICompra {

	/**
	 * Crea una compra asociada al cliente
	 * @param idCliente
	 * @return Long : id de la compra creada.
	 */
	Long crearCompra(Long idCliente);

	/**
	 * Cancela la compra
	 * @param idCompra
	 * @return boolean : estado de la operacion
	 */
	boolean cancelarCompra();

	/**
	 * Adiciona un item a la compra
	 * @param idCompra
	 * @param idProducto
	 * @param catidad
	 * @return boolean : estado de la operacion
	 */
	boolean adicionaProducto( Long idProducto, int catidad);

	/**
	 * Retira un item de la compra
	 * @param Producto
	 * @return boolean : estado de la operacion
	 */
	boolean retirarProducto( Long Producto);

	/**
	 * Liquida y cierra la compra.
	 * @return boolean : estado de la operacion
	 */
	boolean pagarCompra();

	/**
	 * Obtiene el estado actual de la compra.
	 * @return char :Representa estad de la compra
	 */
	char getEstado();
	
	/**
	 * Busca los items de la compra
	 * @return Colleccion de ItemCompra
	 */
	Collection<?> getItemsCompra();

}