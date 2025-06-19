package dominio;

import java.util.ArrayList;
import java.util.List;

public class Grupo extends Contacto {
	private List<ContactoIndividual> miembros;
	
	public Grupo (List<ContactoIndividual> miembros, String nombre) {
		this.nombre = nombre;
		this.miembros = miembros;
		this.mensajes = new ArrayList<Mensaje>();
	}
	
	public List<ContactoIndividual> getMiembros() {
		return this.miembros;
	}
	
	public void addMiembro(ContactoIndividual contacto) {
		miembros.add(contacto);
	}
}
