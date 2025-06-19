package persistencia;

import java.util.List;

import dominio.Mensaje;

public class MensajeDAO_TDS implements MensajeDAO {
	
	public static MensajeDAO_TDS getInstancia() {
		// TODO
		return null;
	}
	
    @Override
    public void registrarMensaje(Mensaje mensaje) {
    }

    @Override
    public void borrarMensaje(Mensaje mensaje) {
    }

    @Override
    public void modificarMensaje(Mensaje mensaje) {
    }

    @Override
    public Mensaje recuperarMensaje(int id) {
        return null;
    }

    @Override
    public List<Mensaje> recuperarTodosMensajes() {
        return null;
    }
}
