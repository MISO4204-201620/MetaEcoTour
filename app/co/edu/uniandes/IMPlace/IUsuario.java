package co.edu.uniandes.IMPlace;

public interface IUsuario {

	/**
	 * Valida el usuario y contrase�a en el sistema.
	 * @param id : Id del usuario
	 * @param psw : Contraseña
	 * @return Boolean indicando la validez de los datos.
	 */

	boolean validaUsuario(Long id, String psw);

	/**
	 * Crea un usuario en la BD con los datos proporcionados.
	 * @param id 
	 * @param nombre  
	 * @param tipodoc
	 * @param documento
	 * @param correo
	 * @param activo
	 * @return Long : Id usuario creado
	 */

	Long registrar( String nombre, String tipodoc, String documento, String correo, Boolean activo);

	/**
	 * Se inactiva el Usuario
	 *
	 * @return Boolean : Estado de la operación
	 */
	boolean inActivar(Long Id);

	/**
	 * Se reactiva el Usuario
	 *
	 * @return Boolean : Estado de la operación
	 */
	boolean reActivar(Long Id);

}