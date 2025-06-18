package dominio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
	
	// Utilizaremos esta función para hashear las claves de los usuarios
	public static String encriptarMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
        	return null;
        }
    }

}
