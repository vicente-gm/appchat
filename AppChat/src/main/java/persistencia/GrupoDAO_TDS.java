package persistencia;

import java.util.List;

import dominio.Grupo;

public class GrupoDAO_TDS implements GrupoDAO {
	
	
	public static GrupoDAO_TDS getInstancia() {
		// TODO
		return null;
	}
	
    @Override
    public void registrarGrupo(Grupo grupo) {
    }

    @Override
    public void borrarGrupo(Grupo grupo) {
    }

    @Override
    public void modificarGrupo(Grupo grupo) {
    }

    @Override
    public Grupo recuperarGrupo(int id) {
        return null;
    }

    @Override
    public List<Grupo> recuperarTodosGrupos() {
        return null;
    }
}
