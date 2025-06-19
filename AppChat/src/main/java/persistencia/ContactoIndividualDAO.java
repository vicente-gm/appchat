package persistencia;

import java.util.List;

import dominio.ContactoIndividual;

public interface ContactoIndividualDAO {
	public void registrarContactoIndividual(ContactoIndividual contacto);
	public void borrarContactoIndividual(ContactoIndividual contacto);
	public void modificaContactoIndividual(ContactoIndividual contacto);
	public ContactoIndividual recuperarContactoIndividual(int id);
	public List<ContactoIndividual> recuperarTodosContactosIndividual();
}
