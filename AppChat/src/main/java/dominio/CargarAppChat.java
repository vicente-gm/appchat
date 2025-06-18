package dominio;

import java.time.LocalDate;

public class CargarAppChat {
	

	public static void cargar() {
		AppChat appChat = AppChat.INSTANCE;
		appChat.registrarUsuario("aa", "aa", "11", "aa", "Hola, soy jesus", LocalDate.of(1960, 10, 03),"/usuarios/user.jpeg");
		appChat.registrarUsuario("bb","bb", "22", "bb", "hola, soy elena", LocalDate.of(1995, 12, 28), "/usuarios/user.jpeg");
		appChat.registrarUsuario("cc", "cc", "33", "cc", "hola, soy rosalia", LocalDate.of(2000, 5, 15), "/usuarios/user.jpeg");
		appChat.registrarUsuario("dd", "dd", "44", "dd", "hola, soy diego", LocalDate.of(1970, 5, 11), "/usuarios/user.jpeg");
		appChat.registrarUsuario("ee", "ee", "55", "ee", "hola, soy anne", LocalDate.of(1990, 3, 28), "/usuarios/user.jpeg");
		
		appChat.login("11", "aa");
		
		ContactoIndividual c2 = appChat.agregarContacto("elena", "22");
		ContactoIndividual c3 = appChat.agregarContacto("rosalia", "33");
		
		appChat.enviarMensajeContacto(c2, "Hola, ¿cómo estás?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 2, TipoMensaje.ENVIADO);
		
		appChat.enviarMensajeContacto(c3, "Cuando cantas?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 6, TipoMensaje.ENVIADO);
		
		appChat.login("22", "bb");
		
		//ContactoIndividual c1 =appChat.agregarContacto("jesus", "11");
		ContactoIndividual c1 = RepositorioUsuarios.INSTANCE.buscarUsuarioPorTelefono("22").buscarContactoIndividual("11");
		ContactoIndividual c4 = appChat.agregarContacto("diego", "44");
		ContactoIndividual c5 = appChat.agregarContacto("anne", "55");
		
		appChat.enviarMensajeContacto(c1, "Vienes este finde?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c1, "", 3, TipoMensaje.ENVIADO);
	    appChat.enviarMensajeContacto(c4, "Juegas esta semana?", -1, TipoMensaje.ENVIADO);	
	    
	    System.out.println("Fin de la carga de datos");
	}

}
