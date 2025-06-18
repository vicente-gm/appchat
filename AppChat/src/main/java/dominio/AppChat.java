package dominio;

import java.awt.EventQueue;

import gui.VentanaLogin;


public enum AppChat {
	INSTANCE;
	public static double COSTE_PREMIUM = 100.0;
	public static RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();
	// public static final String DAO_TDS = "um.tds.appchat.persistencia.impl.FactoriaDAO_TDS";
	// private FactoriaDAO factoriaDAO;
	// private UsuarioDAO usuarioDAO;
	// private ContactoIndividualDAO contactoIndividualDAO;
	// private GrupoDAO grupoDAO;
	// private MensajeDAO mensajeDAO;
	private Usuario usuarioActual;
	
	public static void main(String[] args) {
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
	
	public void registrarUsuario(Usuario usuario) {
		repositorioUsuarios.guardarUsuario(usuario);
	}
	public void eliminarUsuario(Usuario usuario) {
		repositorioUsuarios.eliminarUsuario(usuario);
	}
	public Usuario buscarUsuario(String telefono) {
		return repositorioUsuarios.buscarUsuarioPorTelefono(telefono); 
	}
	
	public boolean login(String telefono, String clave) {
		Usuario usuarioLogin = repositorioUsuarios.buscarUsuarioPorTelefono(telefono);
		if (usuarioLogin != null) {
			System.out.println(usuarioLogin.getClave());
			System.out.println(clave);
			System.out.println(Utils.encriptarMD5(clave));
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
}