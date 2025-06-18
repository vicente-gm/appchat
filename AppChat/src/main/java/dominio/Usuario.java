package dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

	private String nombre, apellidos, telefono, clave, saludo;
	private Date fechaNacimiento;
	private String imagen;
	private boolean premium;
	
	private List<Contacto> contactos; 
	
    public Usuario(String nombre, String apellidos, String telefono, String clave, String saludo, Date fechaNacimiento, String imagen) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.clave = Utils.encriptarMD5(clave);
        this.saludo = saludo;
        this.fechaNacimiento = fechaNacimiento;
        this.imagen = imagen;
        this.premium = false;
        
        this.contactos = new ArrayList<Contacto>();
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getClave() {
        return clave;
    }

    public String getSaludo() {
        return saludo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getImagen() {
        return imagen;
    }

    public boolean isPremium() {
        return premium;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

    public void addContacto(Contacto contacto) {
        if (contactos == null) {
            contactos = new ArrayList<>();
        }
        contactos.add(contacto);
    }

}
