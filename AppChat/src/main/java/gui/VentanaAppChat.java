package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dominio.*;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.stream.Collectors;

import tds.BubbleText;

public class VentanaAppChat extends JDialog {
	
	private JFrame ventanaPrevia;
	private JTextArea areaTexto;
	private AppChat controlador;
	
	public VentanaAppChat(JFrame ventanaPrevia) {
		this.ventanaPrevia = ventanaPrevia;
		this.controlador = AppChat.INSTANCE;
		initialize();
	}
	
    public void initialize() {
        setTitle("AppChat");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Hacemos resize a la imagen de usuario
        ImageIcon imagen = new ImageIcon(getClass().getResource(controlador.getImagen()));
        Image resizedImagen = imagen.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel usuarioActual = new JLabel(new ImageIcon(resizedImagen));
        panelSuperior.add(usuarioActual);
        
        // Funcionalidad para cambiar la imagen del usuario y el saludo
        usuarioActual.setCursor(new Cursor(Cursor.HAND_CURSOR));	// Hacemos que el JLabel de la imagen sea clicable
        usuarioActual.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Creamos una ventana de diálogo
                JDialog dialogo = new JDialog(VentanaAppChat.this, "Editar perfil", true);
                dialogo.setLayout(new GridBagLayout());
                dialogo.setSize(450, 250);
                dialogo.setLocationRelativeTo(VentanaAppChat.this);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);

                // Cambios temporales
                final ImageIcon[] nuevaIcono = { (ImageIcon) usuarioActual.getIcon() };
                final String[] rutaImagenSeleccionada = { null };

                // Primera fila: imagen actual del usuario
                JLabel imagenLabel = new JLabel(usuarioActual.getIcon());
                gbc.gridx = 0;
                gbc.gridy = 0;
                dialogo.add(imagenLabel, gbc);

                // Primera fila: botón para abrir un fileChooser y elegir una imagen
                JButton cambiarImagenBtn = new JButton("Cambiar imagen");
                gbc.gridx = 1;
                gbc.gridy = 0;
                dialogo.add(cambiarImagenBtn, gbc);

                cambiarImagenBtn.addActionListener(e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    int opcion = fileChooser.showOpenDialog(dialogo);
                    if (opcion == JFileChooser.APPROVE_OPTION) {
                        File archivoSeleccionado = fileChooser.getSelectedFile();
                        ImageIcon nuevaImagen = new ImageIcon(archivoSeleccionado.getAbsolutePath());
                        Image imagenRedimensionada = nuevaImagen.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        nuevaIcono[0] = new ImageIcon(imagenRedimensionada);
                        imagenLabel.setIcon(nuevaIcono[0]);
                        rutaImagenSeleccionada[0] = archivoSeleccionado.getAbsolutePath();
                    }
                });

                // Segunda fila: cuadro de texto con el saludo actual
                String saludoActual = controlador.getSaludo();
                JTextField campoSaludo = new JTextField(saludoActual, 20);
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                dialogo.add(campoSaludo, gbc);

                // Tercera fila: botones de Aceptar y Cancelar
                JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton aceptarBtn = new JButton("Aceptar");
                JButton cancelarBtn = new JButton("Cancelar");
                botonesPanel.add(aceptarBtn);
                botonesPanel.add(cancelarBtn);

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.NONE;
                dialogo.add(botonesPanel, gbc);

                // Si pulsamos Cancelar, descartamos los cambios y cerramos la ventana de diálogo
                cancelarBtn.addActionListener(e -> dialogo.dispose());

                // Si pulsamos Aceptar, debemos guardar la imagen y el saludo
                aceptarBtn.addActionListener(e -> {
                    if (rutaImagenSeleccionada[0] != null) {
                        usuarioActual.setIcon(nuevaIcono[0]);
                        controlador.cambiarImagen(rutaImagenSeleccionada[0]); // TODO: revisar el tema de cómo se almacena la imagen
                    }

                    String nuevoSaludo = campoSaludo.getText();
                    if (!nuevoSaludo.equals(saludoActual)) {
                        controlador.cambiarSaludo(nuevoSaludo);
                    }

                    dialogo.dispose();
                });

                dialogo.setVisible(true);
            }
        });
        
        JButton nuevoContactoButton = new JButton("Crear contacto");
        nuevoContactoButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	VentanaCrearContacto crearContacto = new VentanaCrearContacto(null);
		    	
		    	
		    	crearContacto.addWindowListener(new WindowAdapter() {
		            @Override
		            public void windowClosed(WindowEvent e) {
		            	actualizarVentana();
		            }
		        });

		    	
		    	crearContacto.setVisible(true);
		    }
        });
        panelSuperior.add(nuevoContactoButton);

        JButton nuevoGrupoButton = new JButton("Crear grupo");
        nuevoGrupoButton.addActionListener(new ActionListener() {
        	
		    public void actionPerformed(ActionEvent e) {
		    	VentanaCrearGrupo crearGrupo = new VentanaCrearGrupo();
		    	
		    	crearGrupo.addWindowListener(new WindowAdapter() {
		            @Override
		            public void windowClosed(WindowEvent e) {
		            	actualizarVentana();
		            }
		        });

		    	crearGrupo.setVisible(true);
		    }
        });
        panelSuperior.add(nuevoGrupoButton);

        JButton buscarMensajesButton = new JButton("Buscar mensajes");
        buscarMensajesButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	VentanaBuscar ventanaBuscar = new VentanaBuscar();
		    	ventanaBuscar.setVisible(true);
		    }
        });
        panelSuperior.add(buscarMensajesButton);
        
        JButton pdfButton = new JButton("Generar PDF");
        if(this.controlador.isPremium()) panelSuperior.add(pdfButton);

        pdfButton.addActionListener(e -> {
            // Crear el diálogo
            JDialog dialog = new JDialog((Frame) null, "Generar PDF", true);
            dialog.setSize(500, 200);
            dialog.setLocationRelativeTo(null);

            // Panel principal con GridLayout de 3 filas y 2 columnas
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Fila 1: Label + Campo de texto
            JLabel contactoLabel = new JLabel("Nombre de contacto o grupo para imprimir mensajes:");
            JTextField contactoField = new JTextField();

            // Fila 2: Label + Selector de directorio
            JLabel rutaLabel = new JLabel("Ruta donde guardar el archivo:");
            JTextField rutaField = new JTextField();
            rutaField.setEditable(false);
            JButton seleccionarRutaBtn = new JButton("Seleccionar carpeta");

            seleccionarRutaBtn.addActionListener(ev -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int opcion = chooser.showOpenDialog(dialog);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                    rutaField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            });

            // Añadir la segunda fila como subpanel (label + botón + ruta)
            JPanel rutaPanel = new JPanel(new BorderLayout(5, 5));
            rutaPanel.add(rutaField, BorderLayout.CENTER);
            rutaPanel.add(seleccionarRutaBtn, BorderLayout.EAST);

            // Fila 3: Botones Cancelar y Aceptar
            JButton cancelarBtn = new JButton("Cancelar");
            cancelarBtn.addActionListener(ev -> dialog.dispose());

            JButton aceptarBtn = new JButton("Aceptar");
            aceptarBtn.addActionListener(ev -> {
                String contacto = contactoField.getText().trim();
                String ruta = rutaField.getText().trim();

                if (contacto.isEmpty() || ruta.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Debe completar ambos campos correctamente.", "Error", JOptionPane.ERROR_MESSAGE);
                } if(!this.controlador.existeContacto(contacto)) { 
                	JOptionPane.showMessageDialog(dialog, "El nombre del contacto o grupo introducido debe existir.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            	boolean res = this.controlador.crearPDF(ruta, contacto);
            	
            	if(!res) JOptionPane.showMessageDialog(dialog, "Error al generar el archivo PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            	else dialog.dispose();    
            });

            JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            botonesPanel.add(cancelarBtn);
            botonesPanel.add(aceptarBtn);

            // Añadir componentes al panel
            panel.add(contactoLabel);
            panel.add(contactoField);
            panel.add(rutaLabel);
            panel.add(rutaPanel);
            panel.add(new JLabel()); // celda vacía para alinear
            panel.add(botonesPanel);

            dialog.setContentPane(panel);
            dialog.setVisible(true);
        });
        
        JButton premiumButton = new JButton("Premium");
        if(!this.controlador.isPremium()) panelSuperior.add(premiumButton);
        
        premiumButton.addActionListener(e -> {
            // Crear el diálogo
            JDialog dialog = new JDialog((Frame) null, "Actualizar a Premium", true);
            dialog.setSize(300, 250);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new BorderLayout());

            JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
            panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Texto superior
            JLabel mensajeLabel = new JLabel("¿Quiere actualizar su cuenta a Premium?");
            mensajeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelContenido.add(mensajeLabel, BorderLayout.NORTH);

            // Panel central con precio y botón de aplicar descuentos
            JPanel panelCentro = new JPanel();
            panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
            JLabel precioLabel = new JLabel("Precio: $" + controlador.COSTE_PREMIUM);
            precioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton aplicarDescuentosBtn = new JButton("Aplicar descuentos");
            aplicarDescuentosBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            aplicarDescuentosBtn.addActionListener(ev -> {
                double nuevoPrecio = controlador.aplicarDescuentos(); // ← tu lógica
                precioLabel.setText("Precio: $" + String.format("%.2f", nuevoPrecio));
            });

            panelCentro.add(Box.createVerticalStrut(10));
            panelCentro.add(precioLabel);
            panelCentro.add(Box.createVerticalStrut(10));
            panelCentro.add(aplicarDescuentosBtn);

            panelContenido.add(panelCentro, BorderLayout.CENTER);

            // Panel inferior con botones Cancelar y Actualizar
            JPanel panelInferior = new JPanel(new BorderLayout());

            JButton cancelarBtn = new JButton("Cancelar");
            cancelarBtn.addActionListener(ev -> dialog.dispose());

            JButton actualizarBtn = new JButton("Actualizar");
            actualizarBtn.addActionListener(ev -> {
            	controlador.actualizarPremium(true);

                dialog.dispose();

                
                panelSuperior.remove(premiumButton);	// Eliminamos el botón Premium
                panelSuperior.add(pdfButton);	// Añadimos el botón de generar PDF
                
                // Actualizamos
                panelSuperior.revalidate();
                panelSuperior.repaint();

                JOptionPane.showMessageDialog(null, "¡Su cuenta ha sido actualizada correctamente!"); 
            });

            panelInferior.add(cancelarBtn, BorderLayout.WEST);
            panelInferior.add(actualizarBtn, BorderLayout.EAST);

            dialog.add(panelContenido, BorderLayout.CENTER);
            dialog.add(panelInferior, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });   

        JButton logoutButton = new JButton("Cerrar sesión");
        logoutButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	controlador.logout();
		    	VentanaAppChat.this.dispose();
		    	VentanaAppChat.this.ventanaPrevia.setVisible(true);
		    }
		});
        panelSuperior.add(logoutButton);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel de contactos/mensajes
        JPanel principalPanel = new JPanel();
        principalPanel.setLayout(new BorderLayout());

        JPanel listaContactosPanel = new JPanel();
        listaContactosPanel.setLayout(new BoxLayout(listaContactosPanel, BoxLayout.Y_AXIS));

        for (Contacto c : controlador.getContactos()) { 
        	
        	JPanel contactoPanel = new JPanel();
        	contactoPanel.setLayout(new BorderLayout());
            contactoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            contactoPanel.setMaximumSize(new Dimension(250, 100));
            contactoPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            // Cuando hacemos click, seleccionamos ese contacto
            contactoPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
    		    	controlador.setContactoActual(c);
    		    	actualizarVentana();
    		    }    
            });
            
            if (c.equals(controlador.getContactoActual())) {
            	contactoPanel.setBorder(BorderFactory.createLineBorder(Color.magenta));
            }
            
            String nombreContacto = c.getNombre();
            String telefonoContacto = null;
            String saludoContacto = null;
            String miembrosGrupo = null;
            String fotoUsuario = "/usuarios/user.jpeg"; // Por defecto la imagen de grupo
            boolean agregado = true;
            
            // Tratamos si es un grupo o usuario individual
            if (c instanceof ContactoIndividual) { // Si es un contacto individual ponemos la imagen y telefono de su usuario
            	telefonoContacto = ((ContactoIndividual) c).getTelefono();
            	saludoContacto = ((ContactoIndividual) c).getSaludo();
            	fotoUsuario = ((ContactoIndividual) c).getImagen();
            } else {
            	miembrosGrupo = ((Grupo) c).getMiembros().stream().map(ContactoIndividual::getNombre).collect(Collectors.joining("<br>"));
            }
            
            // Nos aseguramos si el contacto está agregado
            if (nombreContacto == null) {
            	agregado = false;
            	nombreContacto = telefonoContacto;
            }
            
            // Nombre contacto
            JLabel nombreLabel = new JLabel(nombreContacto);
            nombreLabel.setFont(nombreLabel.getFont().deriveFont(Font.BOLD, 18f));
            nombreLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            contactoPanel.add(nombreLabel, BorderLayout.NORTH);
            
            
            // Foto contacto
        	ImageIcon imagenContacto = new ImageIcon(getClass().getResource(fotoUsuario));
            JLabel fotoContactoLabel = new JLabel(new ImageIcon(imagenContacto.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            fotoContactoLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            contactoPanel.add(fotoContactoLabel, BorderLayout.WEST);

            // Información del contacto
            String textoInfoContacto = "<html>";
            if (saludoContacto != null) { // Si es un contacto individual ponemos el telefono y estado como información
            	textoInfoContacto += "Telefono: " + telefonoContacto + "<br>" + saludoContacto;
            } else { // Si es un grupo ponemos los miembros como información
            	textoInfoContacto += miembrosGrupo;
            }
            textoInfoContacto += "</html>";
            
            JLabel infoContactoLabel = new JLabel(textoInfoContacto);
            infoContactoLabel.setHorizontalAlignment(SwingConstants.LEFT);
            infoContactoLabel.setFont(infoContactoLabel.getFont().deriveFont(Font.PLAIN));
            contactoPanel.add(infoContactoLabel, BorderLayout.CENTER);

            // Nuevos menasjes y boton añadir contacto
            JPanel panelNotificaciones = new JPanel();
            panelNotificaciones.setLayout(new BoxLayout(panelNotificaciones, BoxLayout.Y_AXIS));
            panelNotificaciones.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            
            /*
            JLabel nuevosMensajesLabel = new JLabel(" 5 ");
            nuevosMensajesLabel.setOpaque(true);
            nuevosMensajesLabel.setBackground(Color.GREEN);
            nuevosMensajesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelNotificaciones.add(nuevosMensajesLabel, BorderLayout.EAST);
            panelNotificaciones.add(Box.createVerticalStrut(15));
            */
            // Si el contacto es desconcido ponemos la opción de agregarlo
            if (!agregado) {
            	String telefono = telefonoContacto;
            	
	            JButton anadirContactoButton = new JButton(" + ");
	            anadirContactoButton.addActionListener(new ActionListener() {
	            	
	    		    public void actionPerformed(ActionEvent e) {
	    		    	VentanaCrearContacto crearContacto = new VentanaCrearContacto(telefono);
	    		    	
	    		    	
	    		    	crearContacto.addWindowListener(new WindowAdapter() {
	    		            @Override
	    		            public void windowClosed(WindowEvent e) {
	    		            	actualizarVentana();
	    		            }
	    		        });

	    		    	
	    		    	crearContacto.setVisible(true);
	    		    }
	            });
	            panelNotificaciones.add(anadirContactoButton);
            }
            
            contactoPanel.add(panelNotificaciones, BorderLayout.EAST);
            
            
            listaContactosPanel.add(contactoPanel);
        }

        JScrollPane scrollContactos = new JScrollPane(listaContactosPanel);
        scrollContactos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollContactos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        principalPanel.add(scrollContactos, BorderLayout.CENTER);

        add(principalPanel, BorderLayout.WEST);

        // Panel de chat
        JPanel chatPanel = new JPanel(new BorderLayout());

        JLabel chatConLabel = new JLabel("Mensajes con " + controlador.getNombreContactoActual());
        chatConLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatPanel.add(chatConLabel, BorderLayout.NORTH);

        JPanel chat = new JPanel();
        chat.setLayout(new BoxLayout(chat,BoxLayout.Y_AXIS));
        chat.setSize(new Dimension(925, 1));

        
        for (Mensaje m : controlador.getMensajesContactoActual()) {
        	int tipoMensaje = BubbleText.RECEIVED;
        	Color colorMensaje = Color.LIGHT_GRAY;
        	String nombreEmisor;
        	
        	if (m.getEmisor().equals(controlador.getTelefono())) { // Si el autor soy yo
        		tipoMensaje = BubbleText.SENT;
        		colorMensaje = Color.GREEN;
        		nombreEmisor = "Yo";
        	} else {
        		nombreEmisor = controlador.getNombreContactoActual();
        	}
        	
        	if (m.getEmoticono() == -1) {        		
        		chat.add(new BubbleText(chat, m.getTexto(), colorMensaje, nombreEmisor, tipoMensaje, 16));
        	} else {
        		chat.add(new BubbleText(chat, m.getEmoticono(), colorMensaje, nombreEmisor, tipoMensaje, 16));
        	}
        }
        
        chatPanel.add(chat, BorderLayout.CENTER);


        JScrollPane scrollMensajes = new JScrollPane(chat);
        scrollMensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollMensajes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        SwingUtilities.invokeLater(() -> scrollMensajes.getVerticalScrollBar().setValue(scrollMensajes.getVerticalScrollBar().getMaximum()));
        
        chatPanel.add(scrollMensajes, BorderLayout.EAST);
        
        
        JPanel panelEntrada = new JPanel(new BorderLayout());

        
    	// Botón emoticonos
        JButton emojiButton = new JButton(BubbleText.getEmoji(0));
        JPopupMenu emojiPopup = new JPopupMenu();
        
        JPanel emojiGrid = new JPanel(new GridLayout(0, 5, 5, 5));
        emojiGrid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        for (int i = 0; i < BubbleText.MAXICONO; i++) {
            JLabel emojiLabel = new JLabel();
            emojiLabel.setIcon(BubbleText.getEmoji(i));
            emojiLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            emojiLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            final int index = i;
            emojiLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                	
    		    	if (controlador.getContactoActual() instanceof ContactoIndividual) {
    		    		controlador.enviarMensajeContacto((ContactoIndividual) controlador.getContactoActual(), "", index, TipoMensaje.ENVIADO);
    		    	} else {
    		    		controlador.enviarMensajeGrupo((Grupo) controlador.getContactoActual(), "", index, TipoMensaje.ENVIADO_GRUPO);
    		    	}
                    emojiPopup.setVisible(false); 
    		    	actualizarVentana();

                }
            });

            emojiGrid.add(emojiLabel);
        }

        emojiPopup.setLayout(new BorderLayout());
        emojiPopup.add(emojiGrid, BorderLayout.CENTER);

        emojiButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	emojiPopup.show(emojiButton, 0, emojiButton.getHeight());
		    }
        });

        
        panelEntrada.add(emojiButton, BorderLayout.WEST);
        
        // Configuración de la área de texto
        areaTexto = new JTextArea(1, 30); // Inicial con 1 línea
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 16));
        // Hacer que el JTextArea crezca dinámicamente
        areaTexto.setPreferredSize(new Dimension(300, 30));
        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
        	public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        	public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        	public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        });
        
        JButton botonEnviar = new JButton("Enviar"); 
        botonEnviar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String mensaje = areaTexto.getText().trim();
		    	
		    	if (mensaje.isEmpty()) {
		    		return;
		    	}
		    	
		    	if (controlador.getContactoActual() instanceof ContactoIndividual) {
		    		controlador.enviarMensajeContacto((ContactoIndividual) controlador.getContactoActual(), mensaje, -1, TipoMensaje.ENVIADO);
		    	} else { // Si es un grupo enviamos el mensaje a todos los miembros
		    		// Todo grupo
		    		controlador.enviarMensajeGrupo((Grupo) controlador.getContactoActual(), mensaje, -1, TipoMensaje.ENVIADO_GRUPO);
		    	}
		    	
		    	actualizarVentana();
		    }
        });
        
        panelEntrada.add(areaTexto, BorderLayout.CENTER);
        panelEntrada.add(botonEnviar, BorderLayout.EAST);

        chatPanel.add(panelEntrada, BorderLayout.SOUTH);
       

        add(chatPanel, BorderLayout.CENTER);
    }

    private void actualizarVentana() {
    	VentanaAppChat.this.getContentPane().removeAll();
    	VentanaAppChat.this.initialize();
    	VentanaAppChat.this.revalidate();
    	VentanaAppChat.this.repaint();
    }
    
    private void ajustarTamañoAreaTexto() {
    	int lineas = areaTexto.getLineCount();
    	int altura = 20 * lineas; // Ajusta el valor según el tamaño de fuente
    	areaTexto.setPreferredSize(new Dimension(300, altura));
    	areaTexto.revalidate();
    }
}
