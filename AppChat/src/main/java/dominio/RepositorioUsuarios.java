package dominio;

import java.util.ArrayList;
import java.util.List;

import persistencia.FactoriaDAO;

public enum RepositorioUsuarios {
	INSTANCE;
	private static List<Usuario> usuarios = new ArrayList<Usuario>();

	public void cargarUsuarios() throws Exception {
		usuarios.addAll(FactoriaDAO.getInstancia("persistencia.FactoriaDAO_TDS").crearUsuarioDAO()
				.recuperarTodosUsuarios());
	}
	
    public void guardarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public Usuario buscarUsuarioPorTelefono(String telefono) {
        for (Usuario usuario : this.usuarios) {
            if (usuario.getTelefono().equals(telefono)) {
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> obtenerTodosUsuarios() {
        return this.usuarios;
    }

    public void eliminarUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
    }
    
}
