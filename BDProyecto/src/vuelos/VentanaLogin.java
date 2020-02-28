package vuelos;


import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Se encarga de crear la ventana para los logueos.
 */
@SuppressWarnings("serial")
public class VentanaLogin extends javax.swing.JInternalFrame {

	private JPanel contentPane;
	private JPasswordField pass;
	private JTextField usuario;
	private VentanaGeneral ventana;
	
	private JLabel lblUsuario;
	private JLabel lblPassword;

	/**
	 * Arma la ventana.
	 * 
	 * @param v Tipo de ventana segun si es inspector o admin.
	 */
	public VentanaLogin(VentanaGeneral v) {
		ventana = v;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 232);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JPanel Login = new JPanel();
		Login.setBounds(47, 24, 310, 135);
		contentPane.add(Login);
		Login.setLayout(null);

		lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(12, 35, 119, 15);
		Login.add(lblUsuario);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(12, 61, 119, 15);
		Login.add(lblPassword);

		usuario = new JTextField();
		usuario.setBounds(141, 32, 114, 19);
		Login.add(usuario);
		usuario.setColumns(10);

		pass = new JPasswordField();
		pass.setBounds(141, 58, 114, 19);
		Login.add(pass);

		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!ventana.ingresarAlaBD(new String(pass.getPassword()), usuario.getText())) {
					JOptionPane.showMessageDialog(contentPane, "Datos ingresados incorrectos");
					usuario.setText("");
					pass.setText("");
				}
				
			}
		});
		btnIngresar.setBounds(37, 87, 117, 25);
		Login.add(btnIngresar);

		JButton button = new JButton("Cancelar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button.setBounds(164, 88, 117, 25);
		Login.add(button);

	}
	
	public void setTitulo(String titulo) {
		this.setTitle(titulo);
		
	}
	
	public void setPrimeraEtiqueta(String nombreEtiqueta){
		lblUsuario.setText(nombreEtiqueta);
	}
	
	public void setSegundaEtiqueta(String nombreEtiqueta){
		lblPassword.setText(nombreEtiqueta);
	}
	
	public void setVisiblePrimeraEtiqueta(boolean visible) {
		lblUsuario.setVisible(visible);
	}
	
	public void setVisiblePrimerTxt(boolean visible) {
		usuario.setVisible(visible);
	}
}
