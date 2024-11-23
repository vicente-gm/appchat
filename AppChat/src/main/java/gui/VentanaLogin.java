package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;


public class VentanaLogin {
	private JFrame frameLogin;
	private JTextField telefono;
	private JPasswordField password;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaLogin window = new VentanaLogin();
					window.frameLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public VentanaLogin () {
		initialize();
	}
	
	private void initialize() {
		frameLogin = new JFrame();
		frameLogin.setTitle("AppChat");
		frameLogin.setSize(300, 300);
		frameLogin.setLocationByPlatform(true);
		frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameLogin.setLayout(new BorderLayout());
		
		JLabel titulo = new JLabel("AppChat", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 24));
		
		JPanel panelCentral = new JPanel();
		JPanel panelCentralSup = new JPanel();
		JPanel panelCentralInf = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panelCentral.setBorder(new TitledBorder("Login"));
		panelCentralSup.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelCentralInf.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		telefono = new JTextField();
		telefono.setColumns(10);
		password = new JPasswordField();
		password.setColumns(10);
		JLabel lblTlf = new JLabel("Teléfono:", SwingConstants.RIGHT);
		JLabel lblPssw = new JLabel("Contraseña:", SwingConstants.RIGHT);
		
		panelCentralSup.add(lblTlf);
		panelCentralSup.add(telefono);
		panelCentralInf.add(lblPssw);
		panelCentralInf.add(password);
		
		panelCentral.add(panelCentralSup);
		panelCentral.add(panelCentralInf);
		
		JPanel panelSur = new JPanel();
		JPanel panelSurIzq = new JPanel();
		JPanel panelSurDer = new JPanel();
		panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.X_AXIS));
		panelSurIzq.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelSurDer.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton aceptar = new JButton("Aceptar");
		JButton cancelar = new JButton("Cancelar");
		JButton registrar = new JButton("Registrar");
		
		panelSurIzq.add(registrar);
		panelSurDer.add(cancelar);
		panelSurDer.add(aceptar);
		
		panelSur.add(panelSurIzq);
		panelSur.add(panelSurDer);
		
		frameLogin.add(titulo, BorderLayout.NORTH);
		frameLogin.add(Box.createHorizontalStrut(75), BorderLayout.EAST);
		frameLogin.add(Box.createHorizontalStrut(75), BorderLayout.WEST);
		frameLogin.add(panelCentral, BorderLayout.CENTER);
		frameLogin.add(panelSur, BorderLayout.SOUTH);
		
	}
}