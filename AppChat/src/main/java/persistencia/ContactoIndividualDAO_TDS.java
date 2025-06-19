package persistencia;

import java.util.List;

import dominio.ContactoIndividual;

public class ContactoIndividualDAO_TDS implements ContactoIndividualDAO {

	public static ContactoIndividualDAO_TDS getInstancia() {
		// TODO
		return null;
	}
	
    @Override
    public void registrarContactoIndividual(ContactoIndividual contacto) {
    }

    @Override
    public void borrarContactoIndividual(ContactoIndividual contacto) {
    }

    @Override
    public void modificaContactoIndividual(ContactoIndividual contacto) {
    }

    @Override
    public ContactoIndividual recuperarContactoIndividual(int id) {
        return null;
    }

    @Override
    public List<ContactoIndividual> recuperarTodosContactosIndividual() {
        return null;
    }
	
}
