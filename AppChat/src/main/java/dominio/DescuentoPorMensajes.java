package dominio;

public class DescuentoPorMensajes implements Descuento {
	private int MIN_MENSAJES = 100;
	
	public double getDescuento(double precio) {
		Long mensajes = AppChat.INSTANCE.getNumMensajesEnviadosUltMes();

		// Descuento del 5% si ha enviado más de 100
		if(mensajes >= MIN_MENSAJES) return precio * 0.95;
		
		return precio;
	}
}
