package dominio;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import gui.VentanaLogin;
import persistencia.*;


public enum AppChat {
	INSTANCE;
	public static double COSTE_PREMIUM = 100.0;
	public static final String DAO_TDS = "persistencia.FactoriaDAO_TDS";
	private FactoriaDAO factoriaDAO;
	private UsuarioDAO usuarioDAO;
	private ContactoIndividualDAO contactoIndividualDAO;
	private GrupoDAO grupoDAO;
	private MensajeDAO mensajeDAO;
	private Usuario usuarioActual;
	private Contacto contactoActual;
	
	
	private AppChat() {
		
		try {
			factoriaDAO = FactoriaDAO.getInstancia(DAO_TDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		usuarioDAO = factoriaDAO.crearUsuarioDAO();
		contactoIndividualDAO = factoriaDAO.crearContactoIndividualDAO();
		grupoDAO = factoriaDAO.crearGrupoDAO();
		mensajeDAO = factoriaDAO.crearMensajeDAO();
	}
	
	public static void main(String[] args) {
		CargarAppChat.cargar();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaLogin login = new VentanaLogin();
					login.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void registrarUsuario(String nombre, String apellidos, String telefono, String pass, String saludo, LocalDate fechaNacimiento, String rutaImagen) {
		Usuario usuario = new Usuario(nombre, apellidos, telefono, pass, saludo, fechaNacimiento, rutaImagen);
		RepositorioUsuarios.INSTANCE.guardarUsuario(usuario);
	}
	public void eliminarUsuario(Usuario usuario) {
		RepositorioUsuarios.INSTANCE.eliminarUsuario(usuario);
	}
	public Usuario buscarUsuario(String telefono) {
		return RepositorioUsuarios.INSTANCE.buscarUsuarioPorTelefono(telefono); 
	}
	
	public List<Contacto> getContactos() {
		return this.usuarioActual.getContactos(); 
	}
	
	public ContactoIndividual agregarContacto(String nombre, String telefono) {
		Usuario usuario = buscarUsuario(telefono);
		if (usuario != null) { // Comprobamos que existe el telefono
			ContactoIndividual existeContacto = usuarioActual.buscarContactoIndividual(telefono);
			if (existeContacto == null) { // Comprobamos si no existe ya en la lista de contactos (aunque no esté agregado)
				ContactoIndividual contacto = new ContactoIndividual(usuario, nombre);
				this.usuarioActual.addContacto(contacto);
				return contacto;
			} else {
				// Si existe solo le cambiamos el nombre al nuevo elegido
				existeContacto.setNombre(nombre);
				return existeContacto;
			}
		}
		
		return null;
	}
	
	public boolean login(String telefono, String clave) {
		Usuario usuarioLogin = RepositorioUsuarios.INSTANCE.buscarUsuarioPorTelefono(telefono);
		if (usuarioLogin != null) {
			if (usuarioLogin.getClave().equals(Utils.encriptarMD5(clave))) {
				this.usuarioActual = usuarioLogin;
				
				// Establecemos el contacto nº1 como el contacto actual
				if (!this.getContactos().isEmpty()) {					
					this.contactoActual = this.getContactos().get(0);
				}
				return true;
			}
		}
		
		return false;
	}
	
	public String getImagen() {
		return this.usuarioActual.getImagen();
	}
	
	public void cambiarImagen(String img) {
		this.usuarioActual.setImagen(img);
	}
	
	public String getSaludo() {
		return this.usuarioActual.getSaludo();
	}
	
	public String getTelefono() {
		return this.usuarioActual.getTelefono();
	}
	
	public void cambiarSaludo(String msg) {
		this.usuarioActual.setSaludo(msg);
	}

	public Usuario getUsuarioActual() {
		return this.usuarioActual;
	}
	
	public void enviarMensajeContacto(ContactoIndividual contacto, String texto, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje = new Mensaje(this.usuarioActual, contacto, texto, emoticono, tipo);
		
		contacto.addMensaje(mensaje);
		
		Mensaje mensajeRecibido = new Mensaje(this.usuarioActual, contacto, texto, emoticono, TipoMensaje.RECIBIDO);

		// Si el recibidor no nos tiene agregados, crearemos un nuevo contacto con nombre null
		ContactoIndividual contactoEnRecibidor = contacto.getUsuario().buscarContactoIndividual(this.usuarioActual.getTelefono());
		if (contactoEnRecibidor == null) {
			ContactoIndividual nuevoContactoSinAgregar = new ContactoIndividual(this.usuarioActual);
			nuevoContactoSinAgregar.addMensaje(mensajeRecibido);
			contacto.getUsuario().addContacto(nuevoContactoSinAgregar);
		} else {
			contactoEnRecibidor.addMensaje(mensajeRecibido);
		}
	}
	
	public ContactoIndividual buscarContactoIndividual(String telefono) {
		return this.usuarioActual.buscarContactoIndividual(telefono);
	}
	
	public void crearGrupo(String nombreGrupo, List<ContactoIndividual> miembros) {
		Grupo nuevoGrupo = new Grupo(miembros, nombreGrupo);
		this.usuarioActual.addContacto(nuevoGrupo);
	}
	
	public void enviarMensajeGrupo(Grupo grupo, String texto, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje = new Mensaje(this.usuarioActual, grupo, texto, emoticono, tipo);
		grupo.addMensaje(mensaje);
		
		for (ContactoIndividual c : grupo.getMiembros()) {
			enviarMensajeContacto(c, texto, emoticono, tipo);
		}
	}
	
	public void setContactoActual(Contacto contacto) {
		this.contactoActual = contacto;
	}
	public Contacto getContactoActual() {
		return this.contactoActual;
	}
	public String getNombreContactoActual() {
		if (this.contactoActual == null) {
			return null;
		}
		
		String nombre = this.contactoActual.getNombre();
		if (nombre == null) {
			// Sabemos que es ContactoIndividual (sin agregar) porque los grupos SIEMPRE tienen nombre
			nombre = ((ContactoIndividual) this.contactoActual).getTelefono();
		}
			
		return nombre;
	}
	public List<Mensaje> buscarMensajes(String texto, String telefono, String contacto) {
		List<Contacto> contactos = this.getContactos().stream()
				.filter(c -> (telefono.isEmpty() || // Si no hay filtro de telefono o, si hay, el telefono coincide
						((c instanceof ContactoIndividual) 
								&& ((ContactoIndividual) c).getTelefono().equals(telefono))
						)) 
				.filter(c -> (contacto.isEmpty() || c.getNombre().equals(contacto)))
				.collect(Collectors.toList());
		
		return contactos.stream()
				.flatMap(c -> c.getMensajes().stream())
				.filter(m -> m.getEmoticono() == -1 && m.getTipoMensaje() != TipoMensaje.ENVIADO_GRUPO)
				.filter(m -> (texto.isEmpty() || m.getTexto().contains(texto)))
				.collect(Collectors.toList());
		
	}
	
	public List<Mensaje> getMensajesContactoActual() {
		if (this.contactoActual == null) {
			return null;
		}
		return this.contactoActual.getMensajes();
	}
	
	public void logout() {
		this.usuarioActual = null;
		this.contactoActual = null;
	}
}