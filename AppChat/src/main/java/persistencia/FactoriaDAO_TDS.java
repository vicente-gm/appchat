package persistencia;

public class FactoriaDAO_TDS extends FactoriaDAO {

	@Override
	public UsuarioDAO crearUsuarioDAO() {
		return UsuarioDAO_TDS.getInstancia();
	}
	
	@Override
	public ContactoIndividualDAO crearContactoIndividualDAO() {
		return ContactoIndividualDAO_TDS.getInstancia();
	}
	
	@Override
	public GrupoDAO crearGrupoDAO() {
		return GrupoDAO_TDS.getInstancia();
	}
	
	@Override
	public MensajeDAO crearMensajeDAO() {
		return MensajeDAO_TDS.getInstancia();
	}
	
}
