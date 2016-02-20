package co.edu.uniandes.IMPlace;

public interface IMensaje {

	/**
	 * Envia mensaje desde un usuario a otro.
	 * @param usrOrigen
	 * @param usrDestino
	 * @param asunto
	 * @param texto
	 * @return boolean : estado de la operacion
	 */
	boolean enviarMensaje(Long usrOrigen, Long usrDestino, String asunto, String texto);

}