package dominio;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Mensaje {
	private String texto;
	private LocalDate fecha;
	private int emoticono;
	private TipoMensaje tipo;
	private final String emisor;
	private final String receptor;
	private int id;
	
	public Mensaje(String emisor, String receptor, String texto, int emoticono, TipoMensaje tipo) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.texto = texto;
		this.fecha = LocalDate.now(); // Devuelve un objeto Date con la fecha y hora actual
		this.emoticono = emoticono;
		this.tipo = tipo;
	}
	
	public int getId() {
    	return this.id;
    }
	
	public void setId(int _id) {
    	this.id = _id;
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
	
	public String getEmisor() {
		return this.emisor;
	}
	
	public String getReceptor() {
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
	
	public void setFecha(LocalDate f) {
		this.fecha = f;
	}
}
