package dominio;

import java.time.LocalDate;

public class DescuentoPorFecha implements Descuento {
	// Dado el precio original, devolvemos el nuevo precio con descuento calculado
	public double getDescuento(double precio) {
		LocalDate fechaRegistro = AppChat.INSTANCE.getUsuarioActual().getFechaRegistro();
		int mes = fechaRegistro.getMonthValue();
		
		// Aplicaremos descuento del 10% si el usuario se ha registrado entre Enero y Marzo.
		if(mes >= 1 && mes <= 3) return precio * 0.9;
		
		return precio;
	}
}
