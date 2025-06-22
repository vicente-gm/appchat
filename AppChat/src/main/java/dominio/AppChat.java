package dominio;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
	private List<Descuento> descuentos;
	private ExportPDF generadorPDF;
	
	private AppChat() {
		try {
			factoriaDAO = FactoriaDAO.getInstancia(DAO_TDS);
			RepositorioUsuarios.INSTANCE.cargarUsuarios();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		usuarioDAO = factoriaDAO.crearUsuarioDAO();
		contactoIndividualDAO = factoriaDAO.crearContactoIndividualDAO();
		grupoDAO = factoriaDAO.crearGrupoDAO();
		mensajeDAO = factoriaDAO.crearMensajeDAO();
		
		this.generadorPDF = new ExportPDF();
		
		descuentos = new LinkedList<>();
		descuentos.add(new DescuentoPorFecha());
		descuentos.add(new DescuentoPorMensajes());
	}
	
	public void registrarUsuario(String nombre, String apellidos, String telefono, String pass, String saludo, LocalDate fechaNacimiento, String rutaImagen) {
		Usuario usuario = new Usuario(nombre, apellidos, telefono, pass, saludo, fechaNacimiento, rutaImagen, LocalDate.now());
		RepositorioUsuarios.INSTANCE.guardarUsuario(usuario);
		this.usuarioDAO.registrarUsuarios(usuario);
	}
	
	public void eliminarUsuario(Usuario usuario) {
		RepositorioUsuarios.INSTANCE.eliminarUsuario(usuario);
		this.usuarioDAO.borrarUsuarios(usuario);
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
				this.contactoIndividualDAO.registrarContactoIndividual(contacto);
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
				
				// Comprobamos si ha caducado el Premium, que dura un año
				LocalDate f = this.usuarioActual.getFechaPremium();
				if(f != null && f.plusYears(1).isBefore(LocalDate.now())) actualizarPremium(false);
				
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
		this.usuarioDAO.modificarUsuario(usuarioActual);
	}
	
	public String getSaludo() {
		return this.usuarioActual.getSaludo();
	}
	
	public String getTelefono() {
		return this.usuarioActual.getTelefono();
	}
	
	public void cambiarSaludo(String msg) {
		this.usuarioActual.setSaludo(msg);
		this.usuarioDAO.modificarUsuario(usuarioActual);
	}

	public Usuario getUsuarioActual() {
		return this.usuarioActual;
	}
	
	public void enviarMensajeContacto(ContactoIndividual contacto, String texto, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje = new Mensaje(this.usuarioActual.getTelefono(), contacto.getTelefono(), texto, emoticono, tipo);
		
		contacto.addMensaje(mensaje);
		this.mensajeDAO.registrarMensaje(mensaje);
		
		Mensaje mensajeRecibido = new Mensaje(this.usuarioActual.getTelefono(), contacto.getTelefono(), texto, emoticono, TipoMensaje.RECIBIDO);

		// Si el recibidor no nos tiene agregados, crearemos un nuevo contacto con nombre null
		ContactoIndividual contactoEnRecibidor = contacto.getUsuario().buscarContactoIndividual(this.usuarioActual.getTelefono());
		if (contactoEnRecibidor == null) {
			ContactoIndividual nuevoContactoSinAgregar = new ContactoIndividual(this.usuarioActual);
			nuevoContactoSinAgregar.addMensaje(mensajeRecibido);
			this.mensajeDAO.registrarMensaje(mensaje);
			contacto.getUsuario().addContacto(nuevoContactoSinAgregar);
			this.contactoIndividualDAO.registrarContactoIndividual(nuevoContactoSinAgregar);
		} else {
			contactoEnRecibidor.addMensaje(mensajeRecibido);
			this.mensajeDAO.registrarMensaje(mensaje);
		}
	}
	
	public ContactoIndividual buscarContactoIndividual(String telefono) {
		return this.usuarioActual.buscarContactoIndividual(telefono);
	}
	
	public void crearGrupo(String nombreGrupo, List<ContactoIndividual> miembros) {
		Grupo nuevoGrupo = new Grupo(miembros, nombreGrupo);
		this.usuarioActual.addContacto(nuevoGrupo);
		this.grupoDAO.registrarGrupo(nuevoGrupo);
	}
	
	public void enviarMensajeGrupo(Grupo grupo, String texto, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje = new Mensaje(this.usuarioActual.getTelefono(), grupo.getNombre(), texto, emoticono, tipo);
		grupo.addMensaje(mensaje);
		this.mensajeDAO.registrarMensaje(mensaje);	// Se envía el mensaje al grupo y a cada contacto
		
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
	
	public long getNumMensajesEnviadosUltMes() {
		return this.usuarioActual.getContactos().stream()
			    .flatMap(contacto -> contacto.getMensajes().stream())
			    .filter(mensaje -> mensaje.getEmisor().equals(this.usuarioActual.getTelefono()))
			    .filter(mensaje -> {
			        LocalDate fecha = mensaje.getFechaEnvio();
			        LocalDate ahora = LocalDate.now();
			        LocalDate primerDiaMesPasado = ahora.minusMonths(1).withDayOfMonth(1);
			        LocalDate ultimoDiaMesPasado = ahora.withDayOfMonth(1).minusDays(1);
			        return !fecha.isBefore(primerDiaMesPasado) && !fecha.isAfter(ultimoDiaMesPasado);
			    })
			    .count();
	}
	
	public double aplicarDescuentos() {
		return descuentos.stream()
			    .reduce(
			        COSTE_PREMIUM,
			        (precioActual, descuento) -> descuento.getDescuento(precioActual),
			        (p1, p2) -> p1 // Este parámetro es un Combiner, pero no lo necesitamos
			    );
	}
	
	public void actualizarPremium(boolean act) {
		if(act) this.usuarioActual.actualizarPremium();
		else this.usuarioActual.caducarPremium();
		this.usuarioDAO.modificarUsuario(usuarioActual);
	}
	
	public boolean isPremium() {
		return this.usuarioActual.isPremium();
	}
	
	public boolean existeContacto(String contacto) {
		return this.usuarioActual.existeContacto(contacto);
	}
	
	public boolean crearPDF(String ruta, String contacto) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fecha = LocalDate.now().format(formato);
        
		return this.generadorPDF.crearPDF(ruta + "mensajes-" + contacto + "-" + fecha, contacto);
	}
	
	public List<Mensaje> getMensajesContacto(String contacto) {
	    return this.usuarioActual.getContactos().stream()
	            .filter(c -> c.getNombre().equals(contacto))
	            .findFirst()
	            .map(Contacto::getMensajes)
	            .orElse(Collections.emptyList());
	}
	
	public Contacto getContacto(String nombre) {
		return this.usuarioActual.getContactos().stream()
				.filter(c -> c.getNombre().equals(nombre))
				.findFirst()
				.orElse(null);
	}
	
	public void logout() {
		this.usuarioActual = null;
		this.contactoActual = null;
	}
}