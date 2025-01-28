package dominio;

import java.awt.EventQueue;

import gui.VentanaLogin;


public class AppChat {
	public static void main(String[] args) {
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