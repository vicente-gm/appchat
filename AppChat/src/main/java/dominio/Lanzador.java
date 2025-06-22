package dominio;

import java.awt.EventQueue;

import gui.VentanaLogin;

public class Lanzador {
	public static void main(String[] args) {
		CargarAppChat.cargar();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaLogin login = new VentanaLogin();
					login.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
