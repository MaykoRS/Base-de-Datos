package vuelos;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.text.MaskFormatter;




import java.awt.Color;

@SuppressWarnings("serial")
public class VentanaEmpleado extends javax.swing.JInternalFrame implements VentanaGeneral {

	private VentanaLogin Login;
	private JDesktopPane jDesktopPane1;
	private VentanaConexion conectar;
	protected Connection conexionBD = null;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxOrigen, comboBoxDestino;
	
	private JLabel lblOrigen;
	private JLabel lblDestino;
	private JLabel lblPartida;
	private JLabel lblRegreso;
	
	private JButton btnBuscar;
	
	private String origenSeleccionado;
	private String destinoSeleccionado;
	
	private JRadioButton rdbtnSoloIda;
	private JRadioButton rdbtnIdaVuelta;
	private DBTable tablaSoloIda, tablaIdaVuelta;
	private DBTable tablaVueloIda;
	private DBTable tablaVueloVuelta;
	
	private String vueloSeleccionado;
	private JButton btnReservarVuelo;
	private JLabel lblPasajeroa;
	private JLabel lblTipo;
	private JTextField textFielddoc_num;
	private JLabel lblDni;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxTipoDni;
	private JButton btnFinalizarReserva;
	
	private String tipo_doc;
	private int nro_dni;
	private String legajo;
	private boolean reservarIda = false;
	private boolean reservarIdaVuelta = false;
	
	
	private JFormattedTextField fechaRegreso;
	private JFormattedTextField fechaPartida;
	
	/**
	 * Crea lo necesario para armar la ventana.
	 * 
	 * @param JD
	 */
	public VentanaEmpleado(JDesktopPane JD) {
		super();
		setTitle("Vuelos - Empleado");
		getContentPane().setLayout(null);
		initGUI();
		jDesktopPane1 = JD;
		
	}
	
	/**
	 * Se encarga del logueo del empleado.
	 */
	public void logeo() {
		Login = new VentanaLogin(this);
		Login.setLocation(200, 100);
		this.Login.setVisible(true);
		Login.setTitulo("Vuelos - Login Empleado");
		Login.setPrimeraEtiqueta("Numero de Legajo");
		Login.setSegundaEtiqueta("Password");
		jDesktopPane1.add(this.Login);
		try {
			Login.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("rawtypes")
	private void initGUI() {
		setPreferredSize(new Dimension(1000, 600));
		this.setBounds(0, 0, 1000, 600);
		setVisible(false);
		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setMaximizable(true);
		
		rdbtnSoloIda = new JRadioButton("Solo Ida");
		rdbtnSoloIda.setBounds(129, 27, 144, 23);
		getContentPane().add(rdbtnSoloIda);
		
		rdbtnIdaVuelta = new JRadioButton("Ida y vuelta");
		rdbtnIdaVuelta.setBounds(511, 27, 144, 23);
		getContentPane().add(rdbtnIdaVuelta);
		
		lblOrigen = new JLabel("Origen");
		lblOrigen.setEnabled(false);
		lblOrigen.setBounds(129, 74, 66, 15);
		getContentPane().add(lblOrigen);
		
		lblDestino = new JLabel("Destino");
		lblDestino.setEnabled(false);
		lblDestino.setBounds(278, 74, 66, 15);
		getContentPane().add(lblDestino);
		
		comboBoxDestino = new JComboBox();
		comboBoxDestino.setEnabled(false);
		comboBoxDestino.setBounds(264, 94, 87, 23);
		getContentPane().add(comboBoxDestino);
		
		comboBoxOrigen = new JComboBox();
		comboBoxOrigen.setEnabled(false);
		comboBoxOrigen.setBounds(107, 94, 91, 23);
		getContentPane().add(comboBoxOrigen);
		
		lblPartida = new JLabel("Partida");
		lblPartida.setEnabled(false);
		lblPartida.setBounds(431, 74, 66, 15);
		getContentPane().add(lblPartida);
		
		lblRegreso = new JLabel("Regreso");
		lblRegreso.setEnabled(false);
		lblRegreso.setBounds(577, 74, 66, 15);
		getContentPane().add(lblRegreso);
		
		btnBuscar = new JButton("Buscar vuelos");
		btnBuscar.setEnabled(false);
		btnBuscar.setBounds(151, 144, 165, 25);
		getContentPane().add(btnBuscar);
		
		tablaSoloIda = new DBTable();
		tablaSoloIda.setBounds(10, 180, 764, 129);
		getContentPane().add(tablaSoloIda);
		tablaSoloIda.setEditable(false);
		
		tablaIdaVuelta = new DBTable();
		tablaIdaVuelta.setBounds(10,320,764,122);
		getContentPane().add(tablaIdaVuelta);
		tablaIdaVuelta.setEditable(false);
		
		tablaVueloVuelta = new DBTable();
		tablaVueloVuelta.setBounds(397, 453, 368, 106);
		getContentPane().add(tablaVueloVuelta);
		tablaVueloVuelta.setEditable(false);
		
		tablaVueloIda = new DBTable();
		tablaVueloIda.setBounds(10, 453, 359, 106);
		getContentPane().add(tablaVueloIda);
		tablaVueloIda.setEditable(false);
		
		btnReservarVuelo = new JButton("Reservar vuelo");
		btnReservarVuelo.setEnabled(false);
		btnReservarVuelo.setBounds(424, 145, 165, 23);
		getContentPane().add(btnReservarVuelo);
		
		JPanel panelFondo = new JPanel();
		panelFondo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
		panelFondo.setBounds(784, 180, 190, 309);
		getContentPane().add(panelFondo);
		panelFondo.setLayout(null);
		
		lblTipo = new JLabel("Tipo:");
		lblTipo.setBounds(20, 88, 50, 14);
		panelFondo.add(lblTipo);
		lblTipo.setEnabled(false);
		
	
		lblDni = new JLabel("Nro:");
		lblDni.setBounds(20, 142, 50, 14);
		panelFondo.add(lblDni);
		lblDni.setEnabled(false);
		
		comboBoxTipoDni = new JComboBox();
		comboBoxTipoDni.setBounds(64, 85, 105, 20);
		panelFondo.add(comboBoxTipoDni);
		comboBoxTipoDni.setEnabled(false);
		
		lblPasajeroa = new JLabel("PASAJERO/A");
		lblPasajeroa.setBounds(62, 33, 80, 14);
		panelFondo.add(lblPasajeroa);
		lblPasajeroa.setEnabled(false);
		
		textFielddoc_num = new JTextField();
		textFielddoc_num.setBounds(64, 139, 105, 20);
		panelFondo.add(textFielddoc_num);
		textFielddoc_num.setEditable(false);
		textFielddoc_num.setEnabled(false);
		textFielddoc_num.setColumns(10);
		
		btnFinalizarReserva = new JButton("Finalizar reserva");
		btnFinalizarReserva.setBounds(20, 223, 154, 60);
		panelFondo.add(btnFinalizarReserva);
		btnFinalizarReserva.setEnabled(false);
		
		try {
			//fechaPartida = new JFormattedTextField(new MaskFormatter("##'/##'/####"));
			fechaPartida = new JFormattedTextField(new MaskFormatter("##'/##'/####"));
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		fechaPartida.setBounds(407, 95, 120, 20);
		fechaPartida.setEnabled(false);
		getContentPane().add(fechaPartida);
		
		try {
			fechaRegreso = new JFormattedTextField(new MaskFormatter("##'/##'/####"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fechaRegreso.setBounds(549, 95, 120, 20);
		fechaRegreso.setEnabled(false);
		getContentPane().add(fechaRegreso);
		
		
		
		//------------- Accion de radioButtons -----------

			rdbtnSoloIda.addActionListener((ActionListener) new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					tablaSoloIda.removeAllRows();
					tablaIdaVuelta.removeAllRows();
					tablaVueloIda.removeAllRows();
					tablaVueloVuelta.removeAllRows();
					
					iniciarComboBox();
					rdbtnIdaVuelta.setSelected(false);
					
					lblRegreso.setEnabled(false);
					fechaRegreso.setEnabled(false);
					reservarIda = false;
					reservarIdaVuelta = false;
					btnFinalizarReserva.setEnabled(false);
					
			}}
			);				
			
			rdbtnIdaVuelta.addActionListener((ActionListener) new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e){
						tablaSoloIda.removeAllRows();
						tablaIdaVuelta.removeAllRows();
						tablaVueloIda.removeAllRows();
						tablaVueloVuelta.removeAllRows();
						iniciarComboBox();
						rdbtnSoloIda.setSelected(false);	
						
						lblRegreso.setEnabled(true);
						fechaRegreso.setEnabled(true);
						
						reservarIda = false;
						reservarIdaVuelta = false;
						btnFinalizarReserva.setEnabled(false);
				}}
				);
			

			// ------- Accion de boton "Buscar vuelos" ----------------------------------------------------------------
			// Se debe mostrar los vuelos disponibles que satisfagan los datos proporcionados.
			btnBuscar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
		
					lblPasajeroa.setEnabled(false);
					lblTipo.setEnabled(false);
					textFielddoc_num.setEnabled(false);
					textFielddoc_num.setEditable(false);
					lblDni.setEnabled(false);
					comboBoxTipoDni.setEnabled(false);
					btnFinalizarReserva.setEnabled(false);
					reservarIda = false;
					reservarIdaVuelta = false;
					
					
					if(rdbtnSoloIda.isSelected()){
						consultaSoloIda();
					}
					else {
						if(rdbtnIdaVuelta.isSelected())
							consultaSoloIda();
							consultaIdaVuelta();
						}
			
				}
				
			});
			
			// boton Reservar vuelo
			btnReservarVuelo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tablaSoloIda.removeAllRows();
					tablaIdaVuelta.removeAllRows();
					tablaVueloIda.removeAllRows();
					tablaVueloVuelta.removeAllRows();
					
					if(rdbtnSoloIda.isSelected()){
						if(Fechas.validar(fechaPartida.getText())) {
							reservarIda = true;
							reservarIdaVuelta = false;
							consultaSoloIda();
							activarLabels();
						}else
							JOptionPane.showMessageDialog(null,"La fecha de partida es inválida.","Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						if(rdbtnIdaVuelta.isSelected()) {
							if(Fechas.validar(fechaPartida.getText()) && Fechas.validar(fechaRegreso.getText())) {
								reservarIdaVuelta = true;
								reservarIda = false;
								consultaSoloIda();
								consultaIdaVuelta();
								activarLabels();
							}else
								JOptionPane.showMessageDialog(null,"Alguna de las fechas es inválida.","Error", JOptionPane.ERROR_MESSAGE);
						}
							
					}
					
				}
			});
			
			btnFinalizarReserva.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(textFielddoc_num.getText().toString().matches("[0-9]+")){
						
						tipo_doc= comboBoxTipoDni.getSelectedItem().toString();
						nro_dni= Integer.parseInt(textFielddoc_num.getText());
						int leg = Integer.parseInt(legajo);
						if(rdbtnSoloIda.isSelected()){
							reservarVueloIda(tipo_doc,nro_dni,leg);
						}
						else {
							if(rdbtnIdaVuelta.isSelected()) 
								reservarVueloIdaVuelta(tipo_doc,nro_dni,leg);
						}
					}else
						JOptionPane.showMessageDialog(null,"El número de documento debe contener dígitos del 0 al 9.");
					
				}
			});
		}
	
	/*Activa los labels del pasajero*/
	private void activarLabels() {
		JOptionPane.showMessageDialog(null,"Seleccione un vuelo y clase para continuar con la reserva");
		btnReservarVuelo.setEnabled(true);
		lblPasajeroa.setEnabled(true);
		lblTipo.setEnabled(true);
		textFielddoc_num.setEnabled(true);
		textFielddoc_num.setEditable(true);
		lblDni.setEnabled(true);
		comboBoxTipoDni.setEnabled(true);
		cargarComboBoxTipoDni();
	}
	
	/*Muestra todos los vuelos disponibles de Ida con los valores seleccionados*/
	private void consultaSoloIda(){
		origenSeleccionado = comboBoxOrigen.getSelectedItem().toString();
		destinoSeleccionado= comboBoxDestino.getSelectedItem().toString();
		if(Fechas.validar(fechaPartida.getText())){
			java.sql.Date partidaSeleccionada = Fechas.convertirStringADateSQL(this.fechaPartida.getText().trim());
		
			try {
				Statement stmt = this.conexionBD.createStatement();
				String sql = "	select distinct vuelo, Nombre_Aeropuerto_Salida, Hora_Salida, Nombre_Aeropuerto_Llegada,"+
							" Hora_Llegada, Modelo_Avion, Tiempo_Estimado"+
						" from vuelos_disponibles as vd " +
						"where '"+
							origenSeleccionado+"'= vd.Ciudad_Aeropuerto_Salida and '"+
							destinoSeleccionado+"'=vd.Ciudad_Aeropuerto_LLegada and '"+
							partidaSeleccionada+"'= vd.Fecha";
				
				ResultSet rs = stmt.executeQuery(sql);
				tablaSoloIda.refresh(rs);
				if (tablaSoloIda.getColumnByDatabaseName("Hora_Salida") != null) {
					tablaSoloIda.getColumnByDatabaseName("Hora_Salida").setDateFormat("hh:mm:ss");
					tablaSoloIda.getColumnByDatabaseName("Hora_Salida").setMinWidth(80);
				}
				if (tablaSoloIda.getColumnByDatabaseName("Hora_Llegada") != null) {
					tablaSoloIda.getColumnByDatabaseName("Hora_Llegada").setDateFormat("hh:mm:ss");
					tablaSoloIda.getColumnByDatabaseName("Hora_Llegada").setMinWidth(80);
				}
				if (tablaSoloIda.getColumnByDatabaseName("Hora_Salida") != null) {
					tablaSoloIda.getColumnByDatabaseName("Hora_Salida").setDateFormat("hh:mm:ss");
					tablaSoloIda.getColumnByDatabaseName("Hora_Salida").setMinWidth(80);
				}
				if (tablaSoloIda.getColumnByDatabaseName("Tiempo_Estimado") != null) {
					tablaSoloIda.getColumnByDatabaseName("Tiempo_Estimado").setDateFormat("hh:mm:ss");
					tablaSoloIda.getColumnByDatabaseName("Tiempo_Estimado").setMinWidth(80);
				}
				tablaSoloIda.addMouseListener(new MouseAdapter() {
	                public void mouseClicked(MouseEvent evt) {
	                   llenarTablaVueloIda(evt,partidaSeleccionada);
	                }
	         	});
				
			} catch (Exception ex) {
				
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
				"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
			}
		}else
			JOptionPane.showMessageDialog(this,"La fecha de partida es inválida.","Error", JOptionPane.ERROR_MESSAGE);
		
	}
	
	/*Muestra todos los vuelos disponibles de Ida y Vuelta con los valores seleccionados*/
	private void consultaIdaVuelta(){
		destinoSeleccionado = comboBoxOrigen.getSelectedItem().toString();
		origenSeleccionado= comboBoxDestino.getSelectedItem().toString();
		if(Fechas.validar(fechaRegreso.getText())){
			java.sql.Date regresoSeleccionado = Fechas.convertirStringADateSQL(this.fechaRegreso.getText().trim());
			try {
				Statement stmt = this.conexionBD.createStatement();
				String sql = "	select	distinct vuelo, Nombre_Aeropuerto_Salida, Hora_Salida,"+
							" Nombre_Aeropuerto_Llegada, Hora_Llegada, Modelo_Avion, Tiempo_Estimado"+
						" from vuelos_disponibles as vd " +
						"where '"+
							origenSeleccionado+"'= vd.Ciudad_Aeropuerto_Salida and '"+
							destinoSeleccionado+"'=vd.Ciudad_Aeropuerto_LLegada and '"+
							regresoSeleccionado+"'= vd.Fecha";
				
				ResultSet rs = stmt.executeQuery(sql);
				tablaIdaVuelta.refresh(rs);
				if (tablaIdaVuelta.getColumnByDatabaseName("Hora_Salida") != null) {
					tablaIdaVuelta.getColumnByDatabaseName("Hora_Salida").setDateFormat("hh:mm:ss");
					tablaIdaVuelta.getColumnByDatabaseName("Hora_Salida").setMinWidth(80);
				}
				if (tablaIdaVuelta.getColumnByDatabaseName("Hora_Llegada") != null) {
					tablaIdaVuelta.getColumnByDatabaseName("Hora_Llegada").setDateFormat("hh:mm:ss");
					tablaIdaVuelta.getColumnByDatabaseName("Hora_Llegada").setMinWidth(80);
				}
				if (tablaIdaVuelta.getColumnByDatabaseName("Hora_Salida") != null) {
					tablaIdaVuelta.getColumnByDatabaseName("Hora_Salida").setDateFormat("hh:mm:ss");
					tablaIdaVuelta.getColumnByDatabaseName("Hora_Salida").setMinWidth(80);
				}
				if (tablaIdaVuelta.getColumnByDatabaseName("Tiempo_Estimado") != null) {
					tablaIdaVuelta.getColumnByDatabaseName("Tiempo_Estimado").setDateFormat("hh:mm:ss");
					tablaIdaVuelta.getColumnByDatabaseName("Tiempo_Estimado").setMinWidth(80);
				}
				tablaIdaVuelta.addMouseListener(new MouseAdapter() {
	                public void mouseClicked(MouseEvent evt) {
	                   llenarTablaVueloVuelta(evt, regresoSeleccionado);
	                }
	         	});
				
				
			} catch (Exception ex) {
				
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
				"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
			}
		}else
			JOptionPane.showMessageDialog(this,"La fecha de regreso es inválida.","Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/*Muestra la clase disponible, cantidad de asientos y precio del vuelo seleccionado de Ida*/
	private void llenarTablaVueloIda(MouseEvent evt,java.sql.Date fecha){
		if ((this.tablaSoloIda.getSelectedRow() != -1) && (evt.getClickCount() == 1)) {
			vueloSeleccionado=tablaSoloIda.getValueAt(this.tablaSoloIda.getSelectedRow(), 0).toString();
			try {
				Statement stmt = this.conexionBD.createStatement();
				String sql = "select vuelo, Clase,Cant_asientos_disponibles,Precio from vuelos_disponibles as vd where vd.vuelo = '"+vueloSeleccionado+"' and vd.Fecha = '"+fecha+"';";
				ResultSet rs = stmt.executeQuery(sql);
				
				tablaVueloIda.refresh(rs);
				tablaVueloIda.addMouseListener(new MouseAdapter() {
	                public void mouseClicked(MouseEvent evt) {
	                	if(rdbtnIdaVuelta.isSelected()) {
	                		reservarIdaVuelta = true;
	                		reservarIda = false;
	                	}else
	                		if(rdbtnSoloIda.isSelected() && reservarIda) {
		                	   btnFinalizarReserva.setEnabled(true);
		                	   //reservarIdaVuelta = false;
	                		}	
	                }
	         	});
				
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
				"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/*Muestra la clase disponible, cantidad de asientos y precio del vuelo seleccionado de Vuelta*/
	private void llenarTablaVueloVuelta(MouseEvent evt, java.sql.Date fecha){
		if ((this.tablaIdaVuelta.getSelectedRow() != -1) && (evt.getClickCount() == 1)) {
			vueloSeleccionado=tablaIdaVuelta.getValueAt(this.tablaIdaVuelta.getSelectedRow(), 0).toString();
			try {
				Statement stmt = this.conexionBD.createStatement();
				String sql = "select vuelo, Clase,Cant_asientos_disponibles,Precio from vuelos_disponibles as vd where vd.vuelo = '"+vueloSeleccionado+"' and vd.Fecha = '"+fecha+"';";
				ResultSet rs = stmt.executeQuery(sql);
				tablaVueloVuelta.refresh(rs);
				tablaVueloIda.addMouseListener(new MouseAdapter() {
	                public void mouseClicked(MouseEvent evt) {
	                	if(rdbtnIdaVuelta.isSelected() && reservarIdaVuelta)
		                	   btnFinalizarReserva.setEnabled(true);
	                }
	         	});
			} catch (Exception ex) {
				
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
				"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void reservarVueloIda(String tipoDoc, int numDoc, int legajo){
		
		
			try {
				if((this.tablaSoloIda.getSelectedRow() != -1) && (this.tablaVueloIda.getSelectedRow() != -1)) {
					String numVuelo=tablaSoloIda.getValueAt(this.tablaSoloIda.getSelectedRow(), 0).toString();
					java.sql.Date fecha = Fechas.convertirStringADateSQL(this.fechaPartida.getText().trim());
					String clase= tablaVueloIda.getValueAt(this.tablaVueloIda.getSelectedRow(), 1).toString();
					String resultado = "Result";
					int n = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea reservar el vuelo seleccionado?", "Confirmar operación",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (n == JOptionPane.YES_OPTION) {
						Statement stmt = this.conexionBD.createStatement();
						String sql = "CALL reserva_ida('"+numVuelo+"','"+fecha+"','"+clase+"','"+tipoDoc+"',"+numDoc+","+legajo+",@"+resultado+")";
						String sql2 = "select @Result as Resultado";
						
						stmt.executeQuery(sql);
						
						ResultSet rs2 = stmt.executeQuery(sql2);
						
						if (rs2.next()) {
							resultado = rs2.getString("Resultado");
							JOptionPane.showMessageDialog(this,resultado,"Reserva finalizada", JOptionPane.INFORMATION_MESSAGE);
						}
					}else
						return;
				}else
					JOptionPane.showMessageDialog(this,"Seleccione una clase del vuelo","Error", JOptionPane.ERROR_MESSAGE);
			}catch(SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
						"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
			}
		
	}
	
	private void reservarVueloIdaVuelta(String tipoDoc, int numDoc, int legajo){
		
		try {
			if((this.tablaVueloIda.getSelectedRow() != -1) && (this.tablaVueloVuelta.getSelectedRow() != -1)) { 
				String numVueloIda=tablaSoloIda.getValueAt(this.tablaSoloIda.getSelectedRow(), 0).toString();
				java.sql.Date fechaIda = Fechas.convertirStringADateSQL(this.fechaPartida.getText().trim());
				String claseIda = tablaVueloIda.getValueAt(this.tablaVueloIda.getSelectedRow(), 1).toString();
				
				String numVueloVuelta=tablaIdaVuelta.getValueAt(this.tablaIdaVuelta.getSelectedRow(), 0).toString();
				java.sql.Date fechaVuelta = Fechas.convertirStringADateSQL(this.fechaRegreso.getText().trim());
				String claseVuelta = tablaVueloVuelta.getValueAt(this.tablaVueloVuelta.getSelectedRow(), 1).toString();
				
				String resultado = "Result";
				int n = JOptionPane.showConfirmDialog(null, "ṡEstá seguro que desea reservar el vuelo seleccionado?", "Confirmar operación",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (n == JOptionPane.YES_OPTION) {
					Statement stmt = this.conexionBD.createStatement();
					String sql = "CALL reserva_ida_vuelta('"+numVueloIda+"','"+fechaIda+"','"+claseIda+"','"+numVueloVuelta+"','"+fechaVuelta+"','"+claseVuelta+"','"+tipoDoc+"',"+numDoc+","+legajo+",@"+resultado+")";
					String sql2 = "select @Result as Resultado";
					
					stmt.executeQuery(sql);
					
					ResultSet rs2 = stmt.executeQuery(sql2);
					
					if (rs2.next()) {
						resultado = rs2.getString("Resultado");
						JOptionPane.showMessageDialog(this,resultado,"Reserva finalizada", JOptionPane.INFORMATION_MESSAGE);
					}
				}else
					return;
			}else
				JOptionPane.showMessageDialog(this,"Seleccione las clases para el vuelo de ida y vuelta","Error", JOptionPane.ERROR_MESSAGE);
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	//----------------INICIAR------------------------------------------------------------------------------------
	private void iniciarComboBox()
	{
		lblOrigen.setEnabled(true);
		comboBoxOrigen.setEnabled(true);
		
		lblDestino.setEnabled(true);
		comboBoxDestino.setEnabled(true);
		
		lblPartida.setEnabled(true);
		fechaPartida.setEnabled(true);
		
		cargarComboBoxCiudades();
		
		//cargarComboBoxPartida();
		
		btnBuscar.setEnabled(true);
		btnReservarVuelo.setEnabled(true);
		
	}
	
	// INTENTO DE MOSTRAR CIUDADES
	@SuppressWarnings("unchecked")
	public void cargarComboBoxCiudades() {
		try {
			Statement stmt = this.conexionBD.createStatement();
			String sql = "select ciudad from vuelos.ubicaciones";
			ResultSet rs = stmt.executeQuery(sql);
			@SuppressWarnings("rawtypes")
			DefaultListModel modelo = new DefaultListModel();
			if(comboBoxOrigen.getItemCount()==0 && comboBoxDestino.getItemCount()==0)
			while (rs.next()) {
				modelo.addElement(rs.getString("ciudad"));
				comboBoxOrigen.addItem(rs.getString("ciudad"));
				comboBoxDestino.addItem(rs.getString("ciudad"));
						
			}
			
		} catch (SQLException ex) {
			// en caso de error, se muestra la causa en la consola
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
		}
	}	

	
	
	@SuppressWarnings("unchecked")
	private void cargarComboBoxTipoDni(){
		if(comboBoxTipoDni.getItemCount() == 0){
			comboBoxTipoDni.addItem("DNI");
			comboBoxTipoDni.addItem("CI");
			comboBoxTipoDni.addItem("LE");
			comboBoxTipoDni.addItem("LC");
		}
		
	}
	
	
	
	/**
	 * Corrobora si los datos de logueo del empleado son correctos.
	 * 
	 * @param usuario
	 *            nombre de usuario.
	 * @param clave
	 *            contraseña del usuario.
	 * @return true si se pudo conectar, falso caso contrario.
	 */
	public boolean CorroborarLogin(String usuario, String clave) {
		boolean correcto = false;
		try {
			Statement stmt = this.conexionBD.createStatement();
			String sql = "select * from empleados where legajo=" + usuario + " and password=md5('" + clave + "')";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				correcto = true;
			rs.close();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Contraseña o usuario invalido.", JOptionPane.ERROR_MESSAGE);
		}
		return correcto;
	}

	
	@Override
	public boolean ingresarAlaBD(String clave, String usuario) {
		conectar = new VentanaConexion(null);
		conectar.ingresarBaseDato("empleado", "empleado");
		conexionBD = conectar.conexion();
		if (CorroborarLogin(usuario, clave)) {
			this.Login.setVisible(false);
			this.setVisible(true);
			//@SuppressWarnings("unused")
			legajo = usuario;
			try {
				@SuppressWarnings("unused")
				Statement stmt = this.conexionBD.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
			return true;
		} else
			return false;
	}
}