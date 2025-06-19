package persistencia;

import java.util.List;

import dominio.Usuario;

public class UsuarioDAO_TDS implements UsuarioDAO {
	
	public static UsuarioDAO_TDS getInstancia() {
		// TODO
		return null;
	}
	
	   	@Override
	    public void registrarUsuarios(Usuario usuario) {
	    }

	    @Override
	    public void borrarUsuarios(Usuario usuario) {
	    }

	    @Override
	    public void modificarUsuario(Usuario usuario) {
	    }

	    @Override
	    public Usuario recuperarUsuario(int id) {
	        return null;
	    }

	    @Override
	    public List<Usuario> recuperarTodosUsuarios() {
	        return null;
	    }
}
