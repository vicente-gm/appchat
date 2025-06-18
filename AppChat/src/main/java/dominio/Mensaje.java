package dominio;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensaje {
	private String texto;
	private Date fecha;
	private int emoticono;
	private int tipo;
	
	public Mensaje(String texto, Date fecha, int emoticono) {
		this(texto, fecha);
		
		this.emoticono = emoticono;
		this.tipo = 1;
	}
	
	public Mensaje(String texto, Date fecha) {
		this.texto = texto;
		this.fecha = fecha;
		this.tipo = 0;
	}
	
	public String getTexto() {
		return this.texto;
	}
	
	public String getHora() {
		return new SimpleDateFormat("HH:mm").format(this.fecha);
	}
}
