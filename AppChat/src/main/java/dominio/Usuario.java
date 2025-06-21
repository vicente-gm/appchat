package dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

	private String nombre, apellidos, telefono, clave, saludo;
	private LocalDate fechaNacimiento;
	private final LocalDate fechaRegistro;
	private String imagen;
	private boolean premium;
	private LocalDate fechaPremium;
	
	private List<Contacto> contactos; 
	
    public Usuario(String nombre, String apellidos, String telefono, String clave, String saludo, LocalDate fechaNacimiento, String imagen, LocalDate fechaReg) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.clave = Utils.encriptarMD5(clave);
        this.saludo = saludo;
        this.fechaNacimiento = fechaNacimiento;
        this.imagen = imagen;
        this.premium = false;
        this.fechaRegistro = fechaReg;
        this.fechaPremium = null;
        
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public String getImagen() {
        return imagen;
    }

    public boolean isPremium() {
        return premium;
    }
    
    public void actualizarPremium() {
    	this.premium = true;
    	this.fechaPremium = LocalDate.now();
    }
    
    public void caducarPremium() {
    	this.premium = false;
    }
    
    public LocalDate getFechaPremium() {
    	return this.fechaPremium;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

    public ContactoIndividual buscarContactoIndividual(String telefono) {
        for (Contacto c : this.contactos) {
            if (c instanceof ContactoIndividual && ((ContactoIndividual) c).getUsuario().getTelefono().equals(telefono)) {
                return (ContactoIndividual) c;
            }
        }
        return null;
    }
    
    public boolean existeContacto(String nombre) {
    	return this.contactos.stream()
    			.anyMatch(c -> c.getNombre().equals(nombre));
    }
    
    public void addContacto(Contacto contacto) {
        contactos.add(contacto);
    }

    public void setImagen(String img) {
    	this.imagen = img;
    }
}
