package persistencia;

import java.util.List;

import dominio.Usuario;

public interface UsuarioDAO {
	public void registrarUsuarios(Usuario usuario);
	public void borrarUsuarios(Usuario usuario);
	public void modificarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(int id);
	public List<Usuario> recuperarTodosUsuarios();
}
