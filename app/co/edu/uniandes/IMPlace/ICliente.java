package co.edu.uniandes.IMPlace;

public interface ICliente {

	/**
	 * Crea un cliente
	 * @param apellido
	 * @return Long : Id del cliente creado.
	 */
	Long crearCliente(String apellido);

}