package dominio;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.List;

import gui.VentanaLogin;


public enum AppChat {
	INSTANCE;
	public static double COSTE_PREMIUM = 100.0;
	// public static final String DAO_TDS = "um.tds.appchat.persistencia.impl.FactoriaDAO_TDS";
	// private FactoriaDAO factoriaDAO;
	// private UsuarioDAO usuarioDAO;
	// private ContactoIndividualDAO contactoIndividualDAO;
	// private GrupoDAO grupoDAO;
	// private MensajeDAO mensajeDAO;
	private Usuario usuarioActual;
	
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
				usuarioActual = usuarioLogin;
				return true;
			}
		}
		
		return false;
	}
	
	public void cambiarImagen(String img) {
		this.usuarioActual.setImagen(img);
	}
	
	public String getSaludo() {
		return this.usuarioActual.getSaludo();
	}
	
	public void cambiarSaludo(String msg) {
		this.usuarioActual.setSaludo(msg);
	}

	public Usuario getUsuarioActual() {
		return this.usuarioActual;
	}
	
	public void enviarMensajeContacto(Contacto contacto, String texto, int emoticono, TipoMensaje tipo) {
		
	}
	
}