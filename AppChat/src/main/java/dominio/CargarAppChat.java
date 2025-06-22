package dominio;

import java.time.LocalDate;

public class CargarAppChat {
	

	public static void cargar() {
		AppChat appChat = AppChat.INSTANCE;
		appChat.registrarUsuario("Jesús", "Martínez", "11", "11", "Hola, soy Jesus", LocalDate.of(1960, 10, 03),"/usuarios/user.jpeg");
		appChat.registrarUsuario("Elena","Carrasco", "22", "22", "Hola, soy Elena", LocalDate.of(1995, 12, 28), "/usuarios/user.jpeg");
		appChat.registrarUsuario("Rosalía", "Hernández", "33", "33", "Hola, soy Rosalia", LocalDate.of(2000, 5, 15), "/usuarios/user.jpeg");
		appChat.registrarUsuario("Diego", "Belmonte", "44", "44", "Hola, soy Diego", LocalDate.of(1970, 5, 11), "/usuarios/user.jpeg");
		appChat.registrarUsuario("Anne", "Smith", "55", "55", "Hola, soy Anne", LocalDate.of(1990, 3, 28), "/usuarios/user.jpeg");
		
		appChat.login("11", "11");
		
		ContactoIndividual c2 = appChat.agregarContacto("Elena", "22");
		ContactoIndividual c3 = appChat.agregarContacto("Rosalía", "33");
		
		appChat.enviarMensajeContacto(c2, "Hola, ¿cómo estás?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 2, TipoMensaje.ENVIADO);
		
		appChat.enviarMensajeContacto(c3, "Cuando cantas?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 6, TipoMensaje.ENVIADO);
		
		appChat.logout();	
	    
	    System.out.println("Fin de la carga de datos");
	}

}
