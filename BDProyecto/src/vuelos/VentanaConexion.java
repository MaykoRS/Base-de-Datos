package vuelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import quick.dbtable.DBTable;


/**
 * Clase encargada de manejar las conexiones con la base de datos.
 * 
 *
 */
public class VentanaConexion {
	private Connection conexionBD = null;

	public VentanaConexion(DBTable tabla) {

	}

	/**
	 * A partir de un usuario y una clave, intenta realizar una conexion con el servidor.
	 * @param clave contraseña del usuario
	 * @param usuario nombre de usuario
	 */
	public void ingresarBaseDato(String clave, String usuario) {
		try {
			String servidor = "localhost:3306";
			String baseDatos = "vuelos";
			//String baseDatos = "vuelos?serverTimezone=America/Buenos_Aires";
			String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + "?serverTimezone=America/Buenos_Aires";
			
			this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
		} catch (SQLException ex) {

			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	/**
	 * Retorna la conexion con el server
	 * @return conexionBD, conexion con el server
	 */
	public Connection conexion() {
		return conexionBD;
	}
}
