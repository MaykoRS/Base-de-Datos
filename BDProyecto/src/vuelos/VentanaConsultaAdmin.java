package vuelos;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import quick.dbtable.*;


/**
 * Se encarga de todo lo relacionado a la ventana de operaciones del
 * administrador y las operaciones que realiza.
 */
@SuppressWarnings("serial")
public class VentanaConsultaAdmin extends javax.swing.JInternalFrame implements VentanaGeneral {
	private JPanel pnlConsulta;
	private JTextArea txtConsulta;
	private JButton botonBorrar;
	private JButton btnEjecutar;
	private DBTable tabla;
	private JScrollPane scrConsulta;
	private VentanaLogin Login;
	private JDesktopPane jDesktopPane1;
	private VentanaConexion conectar;
	protected Connection conexionBD = null;
	private JScrollPane scrollPane;
	@SuppressWarnings("rawtypes")
	private JList listaDeTablas;
	private JScrollPane scrollPane_1;
	@SuppressWarnings("rawtypes")
	private JList listaDeAtributos;
	private Statement stmt;

	/**
	 * Crea lo necesario para armar la ventana.
	 * 
	 * @param JD
	 */
	public VentanaConsultaAdmin(JDesktopPane JD) {
		super();
		setTitle("Vuelos - Administrador");
		setClosable(true);
		initGUI();
		jDesktopPane1 = JD;
	}

	/**
	 * Se encarga del logueo del administrador.
	 */
	public void logeo() {
		Login = new VentanaLogin(this);
		Login.setLocation(200, 100);
		this.Login.setVisible(true);
		Login.setTitulo("Vuelos - Login Administrador");
		Login.setSegundaEtiqueta("Password");
		Login.setVisiblePrimeraEtiqueta(false);
		Login.setVisiblePrimerTxt(false);
			
		jDesktopPane1.add(this.Login);
		try {
			Login.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicializa la ventana para las consultas del administrador.
	 */
	@SuppressWarnings("rawtypes")
	private void initGUI() {
		try {
			// Creacion de la ventana
			setPreferredSize(new Dimension(800, 600));
			this.setBounds(0, 0, 800, 600);
			setVisible(false);
			this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			this.setMaximizable(true);
			getContentPane().setLayout(null);

			{
				pnlConsulta = new JPanel();
				pnlConsulta.setBounds(0, 0, 784, 186);
				getContentPane().add(pnlConsulta);
				{
					scrConsulta = new JScrollPane();
					pnlConsulta.add(scrConsulta);
					{
						txtConsulta = new JTextArea();
						scrConsulta.setViewportView(txtConsulta);
						txtConsulta.setTabSize(3);
						txtConsulta.setColumns(80);
						txtConsulta.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						txtConsulta.setFont(new java.awt.Font("Monospaced", 0, 12));
						txtConsulta.setRows(10);
					}
				}
				{
					btnEjecutar = new JButton();
					pnlConsulta.add(btnEjecutar);
					btnEjecutar.setText("Ejecutar");
					btnEjecutar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnEjecutarActionPerformed(evt);
						}
					});
				}
				{
					botonBorrar = new JButton();
					pnlConsulta.add(botonBorrar);
					botonBorrar.setText("Borrar");
					botonBorrar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							txtConsulta.setText("");
						}
					});
				}
			}

			{
				// crea la tabla
				tabla = new DBTable();
				tabla.setBounds(0, 349, 784, 221);

				// Agrega la tabla al frame (no necesita JScrollPane como
				// Jtable)
				getContentPane().add(tabla);

				// setea la tabla para sólo lectura (no se puede editar su
				// contenido)
				tabla.setEditable(false);
			}
			{
				scrollPane = new JScrollPane();
				scrollPane.setBounds(102, 220, 150, 100);
				getContentPane().add(scrollPane);
				{
					listaDeTablas = new JList();
					scrollPane.setViewportView(listaDeTablas);
				}
			}
			{
				scrollPane_1 = new JScrollPane();
				scrollPane_1.setBounds(336, 220, 150, 100);
				getContentPane().add(scrollPane_1);
				{
					listaDeAtributos = new JList();
					scrollPane_1.setViewportView(listaDeAtributos);
				}
			}
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Evento para desconectarse de la BD.
	 * 
	 * @param evt
	 */
	@SuppressWarnings("unused")
	private void thisComponentHidden(ComponentEvent evt) {
		this.desconectarBD();
	}

	/**
	 * Evento para refrescar la tabla.
	 * 
	 * @param evt
	 */
	private void btnEjecutarActionPerformed(ActionEvent evt) {
		this.refrescarTabla();
	}

	/**
	 * Realiza la desconexion de la base de datos.
	 */
	private void desconectarBD() {
		try {
			tabla.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 */
	private void refrescarTabla() {
		String sql = txtConsulta.getText();

		try {
			if (sql.startsWith("select") || sql.startsWith("Select") || sql.startsWith("SELECT")) {
				ResultSet rs = stmt.executeQuery(sql);

				// actualiza el contenido de la tabla con los datos del resul set rs
				tabla.refresh(rs);

				if (tabla.getColumnByDatabaseName("hora_sale") != null) {
					tabla.getColumnByDatabaseName("hora_sale").setDateFormat("hh:mm:ss");
					tabla.getColumnByDatabaseName("hora_sale").setMinWidth(80);
				}
				
				if (tabla.getColumnByDatabaseName("hora_llega") != null) {
					tabla.getColumnByDatabaseName("hora_llega").setDateFormat("hh:mm:ss");
					tabla.getColumnByDatabaseName("hora_llega").setMinWidth(80);
				}
				if (tabla.getColumnByDatabaseName("Hora_Salida") != null) {
					tabla.getColumnByDatabaseName("Hora_Salida").setDateFormat("hh:mm:ss");
					tabla.getColumnByDatabaseName("Hora_Salida").setMinWidth(80);
				}
				if (tabla.getColumnByDatabaseName("Hora_Llegada") != null) {
					tabla.getColumnByDatabaseName("Hora_Llegada").setDateFormat("hh:mm:ss");
					tabla.getColumnByDatabaseName("Hora_Llegada").setMinWidth(80);
				}
				if (tabla.getColumnByDatabaseName("fecha") != null) {
					tabla.getColumnByDatabaseName("fecha").setDateFormat("dd/MM/YYYY");
					tabla.getColumnByDatabaseName("fecha").setMinWidth(80);
				}
				if (tabla.getColumnByDatabaseName("Fecha") != null) {
					tabla.getColumnByDatabaseName("Fecha").setDateFormat("dd/MM/YYYY");
					tabla.getColumnByDatabaseName("Fecha").setMinWidth(80);
				}
				if (tabla.getColumnByDatabaseName("fecha_vuelo") != null) {
					tabla.getColumnByDatabaseName("fecha_vuelo").setDateFormat("dd/MM/YYYY");
					tabla.getColumnByDatabaseName("fecha_vuelo").setMinWidth(80);
				}
				if (tabla.getColumnByDatabaseName("vencimiento") != null) {
					tabla.getColumnByDatabaseName("vencimiento").setDateFormat("dd/MM/YYYY");
					tabla.getColumnByDatabaseName("vencimiento").setMinWidth(80);
				}
				if (tabla.getColumnByDatabaseName("Tiempo_Estimado") != null) {
					tabla.getColumnByDatabaseName("Tiempo_Estimado").setDateFormat("hh:mm:ss");
					tabla.getColumnByDatabaseName("Tiempo_Estimado").setMinWidth(80);
				}
				
				
			} else {
				stmt.executeUpdate(sql);
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Se modificaron datos \n", "Cambios en la BD",
						JOptionPane.INFORMATION_MESSAGE);

			}
		} catch (SQLException ex) {
			// en caso de error, se muestra la causa en la consola
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error al ejecutar la consulta o a ejecutado un insert.", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Muesta en un JScrollPane, las distintas tablas de la base de datos.
	 */
	@SuppressWarnings("unchecked")
	public void datosdeladm() {
		try {
			Statement stmt = this.conexionBD.createStatement();
			String sql = "show tables";
			ResultSet rs = stmt.executeQuery(sql);
			@SuppressWarnings("rawtypes")
			DefaultListModel modelo = new DefaultListModel();
			while (rs.next()) {
				modelo.addElement(rs.getString("Tables_in_vuelos"));
			}
			listaDeTablas.setModel(modelo);
			listaDeTablas.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			listaDeTablas.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					if (!arg0.getValueIsAdjusting()) {
						mostrarAtributosTabla(listaDeTablas.getSelectedValue().toString());
					}
				}
			});
		} catch (SQLException ex) {
			// en caso de error, se muestra la causa en la consola
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Muestra en un JScrollPane, los distintos atributos de una tabla de la base de
	 * datos.
	 * 
	 * @param tabla
	 *            De la cual se mostraran los atributos.
	 */
	@SuppressWarnings("unchecked")
	public void mostrarAtributosTabla(String tabla) {
		try {
			String sql = "DESCRIBE " + tabla;
			ResultSet rs;
			rs = stmt.executeQuery(sql);
			@SuppressWarnings("rawtypes")
			DefaultListModel modelo = new DefaultListModel();
			while (rs.next()) {
				modelo.addElement(rs.getString("Field"));
			}
			listaDeAtributos.setModel(modelo);
			listaDeAtributos.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Realiza la conexion del administrador con la base de datos.
	 */
	public boolean ingresarAlaBD(String clave, String usuario) {
		conectar = new VentanaConexion(tabla);
		conectar.ingresarBaseDato(clave, "admin");
		conexionBD = conectar.conexion();
		if (conexionBD != null) {
			this.Login.setVisible(false);
			this.setVisible(true);
			// muestra las tablas al logearse
			datosdeladm();
			//refrescarUnico();
			try {
				stmt = this.conexionBD.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}
	
	
}