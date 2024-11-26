package gui;

import java.awt.*;
import javax.swing.*;

import com.toedter.calendar.JDateChooser;

public class VentanaRegistro extends JDialog {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			VentanaRegistro dialog = new VentanaRegistro();
			
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VentanaRegistro() {
		setBounds(100, 100, 600, 500);
		setTitle("Registro");
		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        // TITULO
        JLabel labelTitulo = new JLabel("Registro", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		add(labelTitulo, BorderLayout.NORTH);
        
        // Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelCentral.add(labelNombre, gbc);
        
        JTextField fieldNombre = new JTextField(100);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        panelCentral.add(fieldNombre, gbc);

        // Apellidos
        JLabel labelApellidos = new JLabel("Apellidos:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelCentral.add(labelApellidos, gbc);
        
        JTextField fieldApellidos = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        panelCentral.add(fieldApellidos, gbc);
        
        // Telefono
        JLabel labelTlf = new JLabel("Telefono:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelCentral.add(labelTlf, gbc);
        
        JTextField fieldTlf = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelCentral.add(fieldTlf, gbc);
        
        // Contraseña
        JLabel labelPass = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelCentral.add(labelPass, gbc);
        
        JPasswordField fieldPass = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelCentral.add(fieldPass, gbc);
        
        // Reptetir contraseña
        JLabel labelPassRep = new JLabel("Contraseña:");
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelCentral.add(labelPassRep, gbc);
        
        JPasswordField fieldPassRep = new JPasswordField();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelCentral.add(fieldPassRep, gbc);
        
        // Fecha
        JLabel labelFecha = new JLabel("Nacimiento:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panelCentral.add(labelFecha, gbc);
        
        JDateChooser chooserFecha = new JDateChooser();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelCentral.add(chooserFecha, gbc);

        // Saludo
        JLabel labelSaludo = new JLabel("Saludo:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panelCentral.add(labelSaludo, gbc);
        
        // TODO limitar crecimiento (2 filas)
        JTextArea areaSaludo = new JTextArea("Hi there...\n");
        areaSaludo.setLineWrap(true);
        areaSaludo.setWrapStyleWord(true);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        panelCentral.add(areaSaludo, gbc);
        
        // Imagen
        JLabel labelImagen = new JLabel("Imagen:");
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelCentral.add(labelImagen, gbc);
        
        // TODO Sustituir imagen de prueba por imagen final
        // Se trata de una imagen con relación de aspecto 3:4
        Image resizedImagen = new ImageIcon("/tmp/test.jpeg").getImage().getScaledInstance(125, 125*(4/3), Image.SCALE_SMOOTH);
        JLabel placeholderImagen = new JLabel(new ImageIcon(resizedImagen));
        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        panelCentral.add(placeholderImagen, gbc);
        
        // Boton de cambiar Imagen
        JButton buttonCambiar = new JButton("Cambiar");
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        panelCentral.add(buttonCambiar, gbc);
        
        add(panelCentral, BorderLayout.CENTER);

        
        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton buttonCancelar = new JButton("Cancelar");
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(40, 5, 5, 5);
        panelBotones.add(buttonCancelar, gbc);
        
        JButton buttonAceptar = new JButton("Aceptar");
        gbc.gridx = 2;
        panelBotones.add(buttonAceptar, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
        
        add(panelBotones, BorderLayout.SOUTH);

	}

}
