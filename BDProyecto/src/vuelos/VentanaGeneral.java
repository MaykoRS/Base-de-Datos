package vuelos;


/**
 * Interfaz para los distintos tipos de logueo en la ventana de logueo.
 * 
 *
 */
public interface VentanaGeneral {
	/**
	 * Realiza la conexion del usuario con la base de datos.
	 * 
	 * @param clave
	 *            contraseña del usuario.
	 * @param usuario
	 *            nombre del usuario.
	 * @return
	 */
	public boolean ingresarAlaBD(String clave, String usuario);
}