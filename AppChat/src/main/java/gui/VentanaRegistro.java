package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.io.File;

import javax.swing.*;
import java.awt.*;


import com.toedter.calendar.JDateChooser;

import dominio.RepositorioUsuarios;

public class VentanaRegistro extends JFrame {

	private JFrame ventanaPrevia;
	
	public VentanaRegistro(JFrame ventanaPrevia) {
		this.ventanaPrevia = ventanaPrevia;
		initialize();
	}
	
	private void initialize() {
		setBounds(100, 100, 600, 500);
		setTitle("Registro");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
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
        
        // Se trata de una imagen con relación de aspecto 3:4
        ImageIcon imagen = new ImageIcon(getClass().getResource("/usuarios/user.jpeg"));
        Image resizedImagen = imagen.getImage().getScaledInstance(125, 125*(4/3), Image.SCALE_SMOOTH);
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
        
        buttonCambiar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
	            
		    	PanelArrastrarImagen ventanaSelecionarImagen = new PanelArrastrarImagen(VentanaRegistro.this);
	            List<File> imagenesElegidas = ventanaSelecionarImagen.showDialog();
	            
	            if (!imagenesElegidas.isEmpty()) {
	            	File imagenElegida = imagenesElegidas.get(0);
            		String ruta = imagenElegida.getAbsolutePath();

	            	if (ruta.toLowerCase().endsWith(".jpeg") || ruta.toLowerCase().endsWith(".jpg") || ruta.toLowerCase().endsWith(".png")) {
	            		ImageIcon nuevaImagen = new ImageIcon(ruta);
	            		Image nuevaImagenRedimensionada = nuevaImagen.getImage().getScaledInstance(125, 125 * (4/3), Image.SCALE_SMOOTH);
	                    
	                    placeholderImagen.setIcon(new ImageIcon(nuevaImagenRedimensionada));
	            	} else {
	            		JOptionPane.showMessageDialog(VentanaRegistro.this, "Formato de archivo inválido.");
	            	}
	            }
		    }
		});
		
        
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
        
        buttonCancelar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	ventanaPrevia.setVisible(true);
		    	dispose();
		    }
		});
        
        JButton buttonAceptar = new JButton("Aceptar");
        gbc.gridx = 2;
        panelBotones.add(buttonAceptar, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
        
        buttonAceptar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String nombre = fieldNombre.getText().trim();
		        String apellidos = fieldApellidos.getText().trim();
		        String telefono = fieldTlf.getText().trim();
		        String pass = new String(fieldPass.getPassword());
		        String passRep = new String(fieldPassRep.getPassword());
		        Date fechaNacimiento = chooserFecha.getDate();
		        String saludo = areaSaludo.getText().trim();

		        // Verificar todo compleot
		        if (nombre.trim().isEmpty() || apellidos.trim().isEmpty() || telefono.trim().isEmpty()
		                || pass.trim().isEmpty() || passRep.trim().isEmpty() || fechaNacimiento == null || saludo.trim().isEmpty()) {
		            JOptionPane.showMessageDialog(VentanaRegistro.this, "Rellene todos los campos.");
		            return;
		        }

		        // Validar telefono
		        if (!telefono.matches("\\d+")) {
		            JOptionPane.showMessageDialog(VentanaRegistro.this, "El teléfono solo puede contener números.");
		            return;
		        }

		        // Las contraseñas deben ser iguales
		        if (!pass.equals(passRep)) {
		            JOptionPane.showMessageDialog(VentanaRegistro.this, "Las contraseñas no coinciden.");
		            return;
		        }
		        
		        if (dominio.AppChat.INSTANCE.buscarUsuario(telefono) != null) {
		            JOptionPane.showMessageDialog(VentanaRegistro.this, "El telefono introducido ya existe.");
		            return;
		        }

		        // Todo bien, creamos el usuario
		        // TODO ALMACENAR IMAGEN ELEGIDA		        
		        dominio.Usuario nuevoUsuario = new dominio.Usuario(nombre, apellidos, telefono, pass, saludo, fechaNacimiento, "/usuarios/user.jpeg");
		        dominio.AppChat.INSTANCE.registrarUsuario(nuevoUsuario);
	            JOptionPane.showMessageDialog(VentanaRegistro.this, "¡Usuario registrado correctamente!");
	            
	            // Volver al login
		    	ventanaPrevia.setVisible(true);
		    	dispose();
		    }
        });
        
        add(panelBotones, BorderLayout.SOUTH);

	}


}
