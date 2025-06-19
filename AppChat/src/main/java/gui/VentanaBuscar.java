package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import dominio.AppChat;
import dominio.ContactoIndividual;
import dominio.Mensaje;

public class VentanaBuscar extends JDialog {
	
	private JPanel panelMensajes;
	private JTextField txtTexto;
	private JTextField txtTelefono;
	private JTextField txtContacto;
	
	private AppChat controlador;
	
	public VentanaBuscar() {
		this.controlador = AppChat.INSTANCE;
		initialize();
	}
	
	public void initialize() {
		setTitle("AppChat | Buscar mensaje");
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    setSize(500, 600);
	    setLocationRelativeTo(null);
	    setLayout(new BorderLayout());

	    JLabel titulo = new JLabel("Buscar mensajes", SwingConstants.CENTER);
	    titulo.setFont(new Font("Arial", Font.BOLD, 18));
	    titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
	    add(titulo, BorderLayout.NORTH);

	    JPanel panelBusqueda = new JPanel(new GridLayout(2, 3, 5, 5));
	    panelBusqueda.setBorder(new EmptyBorder(10, 10, 10, 10));

	    this.txtTexto = new JTextField();
	    this.txtTelefono = new JTextField();
	    this.txtContacto = new JTextField();
	    JButton btnBuscar = new JButton("Buscar");

	    panelBusqueda.add(new JLabel("Texto:"));
	    panelBusqueda.add(new JLabel("Teléfono:"));
	    panelBusqueda.add(new JLabel("Contacto:"));

	    panelBusqueda.add(txtTexto);
	    panelBusqueda.add(txtTelefono);
	    panelBusqueda.add(txtContacto);

	    JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    panelBoton.add(btnBuscar);

	    JPanel panelSuperior = new JPanel(new BorderLayout());
	    panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
	    panelSuperior.add(panelBoton, BorderLayout.SOUTH);

	    add(panelSuperior, BorderLayout.NORTH);

	    this.panelMensajes = new JPanel();
	    panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));

	    JScrollPane scrollPane = new JScrollPane(panelMensajes);
	    add(scrollPane, BorderLayout.CENTER);

	    btnBuscar.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            buscarMensajes();
	        }
	    });
	}

	private void buscarMensajes() {
	    panelMensajes.removeAll(); // Limpiar resultados anteriores

	    String texto = this.txtTexto.getText().trim();
	    String telefono = this.txtTelefono.getText().trim();
	    String contacto = this.txtContacto.getText().trim();
	    
	    for (Mensaje m : controlador.buscarMensajes(texto, telefono, contacto)) {
	        JPanel mensaje = new JPanel(new BorderLayout());
	        mensaje.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        String emisor;
	        String emisorTelefono = m.getEmisor().getTelefono();
	        String receptor;
	        String receptorTelefono = ((ContactoIndividual) m.getReceptor()).getTelefono();

	        
	        if (emisorTelefono.equals(controlador.getTelefono())) {
	        	emisor = "Yo";
	        } else {
	        	emisor = controlador.buscarContactoIndividual(emisorTelefono).getNombre();
	        }
	        
	        if (receptorTelefono.equals(controlador.getTelefono())) {
	        	receptor = "Yo";
	        } else {
	        	receptor = controlador.buscarContactoIndividual(receptorTelefono).getNombre();
	        }
	        
	        JLabel lblEmisor = new JLabel(emisor);
	        JLabel lblReceptor = new JLabel(receptor, SwingConstants.RIGHT);

	        JTextArea txtMensaje = new JTextArea(m.getTexto());
	        txtMensaje.setLineWrap(true);
	        txtMensaje.setWrapStyleWord(true);
	        txtMensaje.setEditable(false);
	        txtMensaje.setBorder(BorderFactory.createLineBorder(Color.GRAY));

	        JPanel header = new JPanel(new BorderLayout());
	        header.add(lblEmisor, BorderLayout.WEST);
	        header.add(lblReceptor, BorderLayout.EAST);

	        mensaje.add(header, BorderLayout.NORTH);
	        mensaje.add(txtMensaje, BorderLayout.CENTER);
	        
	        panelMensajes.add(mensaje);
	        panelMensajes.add(new JSeparator(SwingConstants.HORIZONTAL));
	    }

	    panelMensajes.revalidate();
	    panelMensajes.repaint();
	}
}
