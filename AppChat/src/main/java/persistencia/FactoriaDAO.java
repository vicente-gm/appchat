package persistencia;

import tds.driver.ServicioPersistencia.*;

public abstract class FactoriaDAO {
	private static FactoriaDAO instancia;
	
	public static FactoriaDAO getInstancia(String tipo) throws Exception {
		if (instancia == null) {
			instancia = (FactoriaDAO) Class.forName(tipo).newInstance();
		}
		return instancia;
	}
	
	public abstract UsuarioDAO crearUsuarioDAO();
	
	public abstract ContactoIndividualDAO crearContactoIndividualDAO();
	
	public abstract GrupoDAO crearGrupoDAO();
	
	public abstract MensajeDAO crearMensajeDAO();

}