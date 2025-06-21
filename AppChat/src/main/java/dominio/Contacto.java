package dominio;

import java.util.List;

public abstract class Contacto {
	protected String nombre;
	protected List<Mensaje> mensajes;
	
	public String getNombre() {
		return this.nombre;
	}
	
	public List<Mensaje> getMensajes() {
		return this.mensajes;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void addMensaje(Mensaje mensaje) {
		this.mensajes.add(mensaje);
	}
}
