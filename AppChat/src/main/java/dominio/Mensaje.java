package dominio;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensaje {
	public static final int TIPO_TEXTO = 0;
	public static final int TIPO_EMOTICONOS = 1;
	
	private String texto;
	private Date fecha;
	private int emoticono;
	private int tipo;
	private final String emisor, receptor;
	
	public Mensaje(String emisor, String receptor, String texto, int emoticono) {
		this(emisor, receptor, texto);
		
		this.emoticono = emoticono;
		this.tipo = TIPO_EMOTICONOS;
	}
	
	public Mensaje(String emisor, String receptor, String texto) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.texto = texto;
		this.fecha = new Date(); // Devuelve un objeto Date con la fecha y hora actual
		this.tipo = TIPO_TEXTO;
		this.emoticono = -1; // Significa que no hay emoticono
	}
	
	public String getTexto() {
		return this.texto;
	}
	
	public int getEmoticono() {
		return this.emoticono;
	}
	
	public int getTipoMensaje() {
		return this.tipo;
	}
	
	public String getEmisor() {
		return this.emisor;
	}
	
	public String getReceptor() {
		return this.receptor;
	}
	
	public Date getFechaEnvio() {
		return new Date(this.fecha.getTime());
	}
	
	public String getHora() {
		return new SimpleDateFormat("HH:mm").format(this.fecha);
	}
	
	public String getDia() {
		return new SimpleDateFormat("dd/MM/yyyy").format(this.fecha);
	}
}
