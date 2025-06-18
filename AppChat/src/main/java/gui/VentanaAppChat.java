package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dominio.AppChat;

import java.awt.*;
import java.io.File;

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
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Hacemos resize a la imagen de usuario
        ImageIcon imagen = new ImageIcon(getClass().getResource("/usuarios/user.jpeg"));
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
        panelSuperior.add(nuevoContactoButton);

        JButton nuevoGrupoButton = new JButton("Crear grupo");
        panelSuperior.add(nuevoGrupoButton);
        
        JButton verContactosButton = new JButton("Ver contactos");
        panelSuperior.add(verContactosButton);
        
        JButton premiumButton = new JButton("Premium");
        panelSuperior.add(premiumButton);

        JButton buscarMensajesButton = new JButton("Buscar mensajes");
        panelSuperior.add(buscarMensajesButton);


        add(panelSuperior, BorderLayout.NORTH);

        // Panel de contactos/mensajes
        JPanel principalPanel = new JPanel();
        principalPanel.setLayout(new BorderLayout());

        JPanel listaContactosPanel = new JPanel();
        listaContactosPanel.setLayout(new BoxLayout(listaContactosPanel, BoxLayout.Y_AXIS));

        for (int i = 1; i <= 7; i++) { // TODO RECORRER CONTACTOS DE VERDAD
        	JPanel contactoPanel = new JPanel();
        	contactoPanel.setLayout(new BorderLayout());
            contactoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            
            // Nombre contacto
            JLabel nombreLabel = new JLabel("Usuario");
            nombreLabel.setFont(nombreLabel.getFont().deriveFont(Font.BOLD, 18f));
            contactoPanel.add(nombreLabel, BorderLayout.NORTH);
            
            // Foto contacto
        	ImageIcon imagenContacto = new ImageIcon(getClass().getResource("/usuarios/user.jpeg"));
            JLabel fotoContactoLabel = new JLabel(new ImageIcon(imagen.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            contactoPanel.add(fotoContactoLabel, BorderLayout.WEST);

            // Información del contacto
            JLabel infoContactoLabel = new JLabel("<html>" + "Telefono: 123" + "<br>" + "Hola soy usuario!" + "</html>");
            infoContactoLabel.setHorizontalAlignment(SwingConstants.LEFT);
            infoContactoLabel.setFont(infoContactoLabel.getFont().deriveFont(Font.PLAIN));
            contactoPanel.add(infoContactoLabel, BorderLayout.CENTER);

            // Nuevos menasjes y boton añadir contacto
            JPanel panelNotificaciones = new JPanel();
            panelNotificaciones.setLayout(new BoxLayout(panelNotificaciones, BoxLayout.Y_AXIS));
            panelNotificaciones.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            
            JLabel nuevosMensajesLabel = new JLabel(" 5 ");
            nuevosMensajesLabel.setOpaque(true);
            nuevosMensajesLabel.setBackground(Color.GREEN);
            nuevosMensajesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelNotificaciones.add(nuevosMensajesLabel, BorderLayout.EAST);
            
            panelNotificaciones.add(Box.createVerticalStrut(15));

            JButton anadirContactoButton = new JButton(" + ");
            panelNotificaciones.add(anadirContactoButton);
            
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

        JLabel chatConLabel = new JLabel("Mensajes con usuario1");
        chatConLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chatPanel.add(chatConLabel, BorderLayout.NORTH);

        JPanel mensajesPanel = new JPanel();
        mensajesPanel.setLayout(new BoxLayout(mensajesPanel, BoxLayout.Y_AXIS));

        mensajesPanel.add(new BubbleText(mensajesPanel, "Hola grupo!!", Color.GREEN, "J.Ramón", BubbleText.SENT));
        mensajesPanel.add(new BubbleText(mensajesPanel, "Hola prueba", Color.LIGHT_GRAY, "J", BubbleText.RECEIVED));
        mensajesPanel.add(new BubbleText(mensajesPanel, "Probando", Color.GREEN, "J.Ramón", BubbleText.SENT));
        mensajesPanel.add(new BubbleText(mensajesPanel, "Test", Color.LIGHT_GRAY, "J", BubbleText.RECEIVED));
        chatPanel.add(mensajesPanel, BorderLayout.CENTER);

        JScrollPane scrollMensajes = new JScrollPane(mensajesPanel);
        scrollMensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollMensajes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatPanel.add(scrollMensajes, BorderLayout.CENTER);
        
        JPanel panelEntrada = new JPanel(new BorderLayout());
        
        // Configuración de la área de texto
        areaTexto = new JTextArea(1, 30); // Inicial con 1 línea
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        // Hacer que el JTextArea crezca dinámicamente
        areaTexto.setPreferredSize(new Dimension(300, 30));
        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        });
        
        JButton botonEnviar = new JButton(new ImageIcon("icono_enviar.png")); // TODO poner icono
        
        panelEntrada.add(areaTexto, BorderLayout.CENTER);
        panelEntrada.add(botonEnviar, BorderLayout.EAST);

        chatPanel.add(panelEntrada, BorderLayout.SOUTH);
       

        add(chatPanel, BorderLayout.CENTER);
    }

    private void ajustarTamañoAreaTexto() {
    	int lineas = areaTexto.getLineCount();
    	int altura = 20 * lineas; // Ajusta el valor según el tamaño de fuente
    	areaTexto.setPreferredSize(new Dimension(300, altura));
    	areaTexto.revalidate();
    }
}
