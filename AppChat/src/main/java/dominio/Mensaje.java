package dominio;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Mensaje {
	private String texto;
	private final LocalDate fecha;
	private int emoticono;
	private TipoMensaje tipo;
	private final Usuario emisor;
	private final Contacto receptor;
	
	public Mensaje(Usuario emisor, Contacto receptor, String texto, int emoticono, TipoMensaje tipo) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.texto = texto;
		this.fecha = LocalDate.now(); // Devuelve un objeto Date con la fecha y hora actual
		this.emoticono = emoticono;
		this.tipo = tipo;
	}
	
	public String getTexto() {
		return this.texto;
	}
	
	public int getEmoticono() {
		return this.emoticono;
	}
	
	public TipoMensaje getTipoMensaje() {
		return this.tipo;
	}
	
	public Usuario getEmisor() {
		return this.emisor;
	}
	
	public Contacto getReceptor() {
		return this.receptor;
	}
	
	public LocalDate getFechaEnvio() {
		return this.fecha;
	}
	
	public String getHora() {
		return new SimpleDateFormat("HH:mm").format(this.fecha);
	}
	
	public String getDia() {
		return new SimpleDateFormat("dd/MM/yyyy").format(this.fecha);
	}
}
