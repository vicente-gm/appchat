package dominio;

import java.util.ArrayList;
import java.util.List;

public class ContactoIndividual extends Contacto {
	
	private Usuario usuario;
	
	public ContactoIndividual(Usuario usuario, String nombre) {
		this(usuario);
		this.nombre = nombre;
	}
	
	public ContactoIndividual(Usuario usuario) {
		super();
		this.usuario = usuario;
		this.mensajes = new ArrayList<Mensaje>();  
	}
	
    public Usuario getUsuario() {
        return usuario;
    }
    
    public String getTelefono() {
	    return this.usuario.getTelefono();
    }
    
    public String getSaludo() {
	    return this.usuario.getSaludo();
    }
    
    public String getImagen() {
	    return this.usuario.getImagen();
    }
    
    public void setMensajes(List<Mensaje> m) {
    	this.mensajes = m;
    }
}
