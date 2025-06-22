package gui;

import javax.swing.*;

import dominio.AppChat;
import dominio.ContactoIndividual;

import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class VentanaCrearGrupo extends JDialog {
    private DefaultListModel<String> modeloContactos;
    private DefaultListModel<String> modeloAnadidos;
    private JList<String> listaContactos;
    private JList<String> listaAnadidos;
    private JTextField nombreGrupoField;
    private AppChat controlador;

    public VentanaCrearGrupo() {
    	this.controlador = AppChat.INSTANCE;
		initialize();
    }
    
    public void initialize() {
        setTitle("AppChat | Crear grupo");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JPanel tituloPanel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Crear grupo", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tituloPanel.add(titulo, BorderLayout.NORTH);

        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nombrePanel.add(new JLabel("Nombre grupo:"));
        nombreGrupoField = new JTextField(20);
        nombrePanel.add(nombreGrupoField);
        tituloPanel.add(nombrePanel, BorderLayout.SOUTH);

        add(tituloPanel, BorderLayout.NORTH);

        // Panel central con listas
        modeloContactos = new DefaultListModel<String>();
        modeloAnadidos = new DefaultListModel<String>();

        listaContactos = new JList<String>(modeloContactos);
        listaAnadidos = new JList<String>(modeloAnadidos);

        JScrollPane scrollIzq = new JScrollPane(listaContactos);
        JScrollPane scrollDer = new JScrollPane(listaAnadidos);

        scrollIzq.setBorder(BorderFactory.createTitledBorder("Contactos"));
        scrollDer.setBorder(BorderFactory.createTitledBorder("Contactos añadidos"));

        JButton añadir = new JButton("->");
        JButton quitar = new JButton("<-");

        añadir.addActionListener(e -> {
            for (String seleccionado : listaContactos.getSelectedValuesList()) {
                if (!modeloAnadidos.contains(seleccionado)) {
                	modeloAnadidos.addElement(seleccionado);
                    modeloContactos.removeElement(seleccionado);
                }
            }
        });

        quitar.addActionListener(e -> {
            for (String seleccionado : listaAnadidos.getSelectedValuesList()) {
                if (!modeloContactos.contains(seleccionado)) {
                    modeloContactos.addElement(seleccionado);
                    modeloAnadidos.removeElement(seleccionado);
                }
            }
        });

        JPanel botonesCentro = new JPanel();
        botonesCentro.setLayout(new BoxLayout(botonesCentro, BoxLayout.Y_AXIS));
        botonesCentro.add(Box.createVerticalGlue());
        botonesCentro.add(añadir);
        botonesCentro.add(Box.createVerticalStrut(10));
        botonesCentro.add(quitar);
        botonesCentro.add(Box.createVerticalGlue());

        JPanel centro = new JPanel(new BorderLayout(10, 0));
        centro.add(scrollIzq, BorderLayout.WEST);
        centro.add(botonesCentro, BorderLayout.CENTER);
        centro.add(scrollDer, BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);

        JPanel botonesAbajo = new JPanel();

        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	dispose();
		    }
		});
        botonesAbajo.add(cancelar);

        
        JButton aceptar = new JButton("Aceptar");
        aceptar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String nombreGrupo = nombreGrupoField.getText().trim();
		    	
		    	List<String> seleccionados = new ArrayList<String>();
		    	for (int i = 0; i < modeloAnadidos.getSize(); i++) {
		    		seleccionados.add(modeloAnadidos.getElementAt(i));
		    	}

		    	
		    	if (nombreGrupo.isEmpty()) {
		            JOptionPane.showMessageDialog(VentanaCrearGrupo.this, "Rellene el nombre del grupo");
		    		return;
		    	}
		       
		    	if (seleccionados.isEmpty()) {
		            JOptionPane.showMessageDialog(VentanaCrearGrupo.this, "No puede crear un grupo vacío.");
		    		return;
		    	}
     
		    	List<ContactoIndividual> contactosSeleccionados = seleccionados.stream()
		    		    .map(s -> s.split(":")[1]) // Obtener solo el teléfono
		    		    .map(telefono -> controlador.buscarContactoIndividual(telefono))
		    		    .collect(Collectors.toList());
		    	
		        controlador.crearGrupo(nombreGrupo, contactosSeleccionados);
	            JOptionPane.showMessageDialog(VentanaCrearGrupo.this, "Grupo creado correctamente.");
		    	dispose();
		        
		    }
        });

        botonesAbajo.add(aceptar);
        add(botonesAbajo, BorderLayout.SOUTH);

        // Cargamos los nombres de todos los contactos individuales
        List<String> nombres = controlador.getContactos().stream()
        	    .filter(c -> c instanceof ContactoIndividual)
        	    .filter(c -> c.getNombre() != null)
        	    .map(c -> c.getNombre() + ":" + ((ContactoIndividual) c).getTelefono())
        	    .collect(Collectors.toList());

    	for (String nombre : nombres) {
    	    modeloContactos.addElement(nombre);
    	}
    }
}
