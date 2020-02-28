package vuelos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Font;

/**
 * Se encarga de manejar la ventana principal de la aplicacion, donde se
 * selecciona las operaciones para loguear como admin o empleado.
 */
@SuppressWarnings("serial")
public class VentanaPrincipal extends javax.swing.JFrame {
	private JDesktopPane jDesktopPane1;
	
	private VentanaConsultaAdmin ventanaAdmin;
	private VentanaEmpleado ventanaEmpleado;

	private JMenuBar jMenuBar1;
	private JMenuItem mniSalir;
	private JSeparator jSeparator1;
	private JMenuItem mniAdmin;
	private JMenuItem mniEmpleado;
	private JMenu mnuEjemplos;
	private JLabel lblDeLaBase;
	private JLabel lblNewLabel;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				VentanaPrincipal inst = new VentanaPrincipal();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	/**
	 * Arma la ventana principal.
	 */
	public VentanaPrincipal() {
		super();

		initGUI();

		this.ventanaAdmin = new VentanaConsultaAdmin(jDesktopPane1);
		this.ventanaAdmin.setVisible(false);
		this.jDesktopPane1.add(this.ventanaAdmin);
		
		this.ventanaEmpleado = new VentanaEmpleado(jDesktopPane1);
		this.ventanaEmpleado.setVisible(false);
		this.jDesktopPane1.add(this.ventanaEmpleado);

	}

	/**
	 * Inicializa la interfaz.
	 */
	private void initGUI() {
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { 
			{ 
				this.setTitle("Simulador Vuelo");
				this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(new Color(102, 153, 102));
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(800, 600));
				
				JLabel mensajeInicio1 = new JLabel("Bienvenido al sistema \r\n");
				mensajeInicio1.setFont(new Font("Tahoma", Font.BOLD, 16));
				mensajeInicio1.setVerticalAlignment(SwingConstants.TOP);
				mensajeInicio1.setBounds(247, 85, 274, 20);
				jDesktopPane1.add(mensajeInicio1);
				{
					lblDeLaBase = new JLabel("de la Base de Datos de Vuelos");
					lblDeLaBase.setFont(new Font("Tahoma", Font.BOLD, 16));
					lblDeLaBase.setBounds(236, 116, 328, 25);
					jDesktopPane1.add(lblDeLaBase);
				}
				{
					lblNewLabel = new JLabel("Seleccione una opción del menú Opciones\n");
					lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
					lblNewLabel.setBounds(124, 212, 577, 20);
					jDesktopPane1.add(lblNewLabel);
				}
			}

			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					mnuEjemplos = new JMenu();
					jMenuBar1.add(mnuEjemplos);
					mnuEjemplos.setText("Opciones");
					{
			  			mniAdmin = new JMenuItem();
						mnuEjemplos.add(mniAdmin);
						mniAdmin.setText("Login Administrador");
						mniAdmin.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								AdminActionPerformed(evt);
							}
						});
					}
					{
						mniEmpleado = new JMenuItem();
						mnuEjemplos.add(mniEmpleado);
						mniEmpleado.setText("Login Empleado");
						mniEmpleado.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								EmpleadoActionPerformed(evt);
							}
						});
					}

					{
						jSeparator1 = new JSeparator();
						mnuEjemplos.add(jSeparator1);
					}
					{
						mniSalir = new JMenuItem();
						mnuEjemplos.add(mniSalir);
						mniSalir.setText("Salir");
						mniSalir.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								SalirActionPerformed(evt);
							}
						});
					}
				}

			}
			this.setSize(800, 600);
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Listener para el logueo como admin.
	 * 
	 * @param evt
	 */
	private void AdminActionPerformed(ActionEvent evt) {
		try {
			this.ventanaAdmin.setMaximum(true);
		} catch (PropertyVetoException e) {
		}
		ventanaAdmin.logeo();
	}
	
	/**
	 * Listener para la conexion de un empleado.
	 * 
	 * @param evt
	 */
	private void EmpleadoActionPerformed(ActionEvent evt) {
		try {
			this.ventanaEmpleado.setMaximum(true);
		} catch (PropertyVetoException e) {
		}
		this.ventanaEmpleado.logeo();
	}

	/**
	 * Listener para salir de la ventana.
	 * 
	 * @param evt
	 */
	private void SalirActionPerformed(ActionEvent evt) {
		this.dispose();
	}
}
