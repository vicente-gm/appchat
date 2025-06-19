package persistencia;

import java.util.List;

import dominio.Grupo;

public interface GrupoDAO {
	public void registrarGrupo(Grupo grupo);
	public void borrarGrupo(Grupo grupo);
	public void modificarGrupo(Grupo grupo);
	public Grupo recuperarGrupo(int id);
	public List<Grupo> recuperarTodosGrupos();
}
