package dominio;

import java.util.ArrayList;

public class ContactoIndividual extends Contacto {
	
	private Usuario usuario;
	
	public ContactoIndividual(Usuario usuario, String nombre) {
		this(usuario);
		this.nombre = nombre;
	}
	
	public ContactoIndividual(Usuario usuario) {
		this.usuario = usuario;
		this.mensajes = new ArrayList<Mensaje>();  
	}
	
    public Usuario getUsuario() {
        return usuario;
    }
}
