package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dominio.AppChat;

public class VentanaCrearContacto extends JDialog {

	private String telefono;
	private AppChat controlador;
	
	public VentanaCrearContacto(String telefono) {
		this.telefono = telefono; // Será null si se crea un contacto nuevo
		// Si se agrega uno del que se ha recibido un mensaje, entonces si contendrá el número
		this.controlador = AppChat.INSTANCE;
		initialize();
	}
	
	public void initialize() {
	    setTitle("AppChat | Crear contacto");
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    setSize(500, 300);
	    setLocationRelativeTo(null);
	    setLayout(new BorderLayout());

	    JLabel titulo = new JLabel("Nuevo usuario", SwingConstants.CENTER);
	    titulo.setFont(new Font("Arial", Font.BOLD, 18));
	    titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
	    add(titulo, BorderLayout.NORTH);

	    // Panel campos
	    JPanel camposPane = new JPanel();
	    camposPane.setLayout(new BoxLayout(camposPane, BoxLayout.Y_AXIS));
	    camposPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

	    JLabel nombreLabel = new JLabel("Nombre");
	    JTextField nombreField = new JTextField();
	    nombreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

	    camposPane.add(nombreLabel);
	    camposPane.add(nombreField);
	    camposPane.add(Box.createRigidArea(new Dimension(0, 10)));

	    JLabel telefonoLabel = new JLabel("Teléfono");
	    JTextField telefonoField = new JTextField(this.telefono);
	    telefonoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

	    camposPane.add(telefonoLabel);
	    camposPane.add(telefonoField);

	    add(camposPane, BorderLayout.CENTER);

	    // Panel botones
	    JPanel botonesPane = new JPanel();
	    botonesPane.setLayout(new BoxLayout(botonesPane, BoxLayout.X_AXIS));
	    botonesPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

	    JButton cancelarButton = new JButton("Cancelar");
	    cancelarButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	VentanaCrearContacto.this.dispose();
		    }
        });
	    JButton crearButton = new JButton("Añadir");
	    crearButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String nombreContacto = nombreField.getText().trim();
		    	String telefonoContacto = telefonoField.getText().trim();
		    	
		    	if (nombreContacto.isEmpty() || telefonoContacto.isEmpty()) {
		            JOptionPane.showMessageDialog(VentanaCrearContacto.this, "Rellene todos los campos.");
		    		return;
		    	}
		        if (!telefonoContacto.matches("\\d+")) {
		            JOptionPane.showMessageDialog(VentanaCrearContacto.this, "El teléfono solo puede contener números.");
		            return;
		        }
		        
		        if (controlador.getTelefono().equals(telefonoContacto)) {
		            JOptionPane.showMessageDialog(VentanaCrearContacto.this, "No puede agregarse a usted mismo");
		            return;
		        }
		        
		        if (controlador.buscarUsuario(telefonoContacto) == null) {
		            JOptionPane.showMessageDialog(VentanaCrearContacto.this, "El telefono introducido no existe.");
		            return;
		        }
      
		        controlador.agregarContacto(nombreContacto, telefonoContacto);
	            JOptionPane.showMessageDialog(VentanaCrearContacto.this, "Contacto creado correctamente.");
		    	dispose();
		        
		    }
        });

	    botonesPane.add(Box.createHorizontalGlue());
	    botonesPane.add(cancelarButton);
	    botonesPane.add(Box.createRigidArea(new Dimension(20, 0)));
	    botonesPane.add(crearButton);
	    botonesPane.add(Box.createHorizontalGlue());

	    add(botonesPane, BorderLayout.SOUTH);
	}
}
