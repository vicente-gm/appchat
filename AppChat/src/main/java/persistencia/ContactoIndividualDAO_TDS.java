package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.Mensaje;
import dominio.Usuario;
import dominio.ContactoIndividual;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class ContactoIndividualDAO_TDS implements ContactoIndividualDAO {
	
	private static final String NOMBRE = "nombre";
	private static final String MENSAJES = "mensajes";
	private static final String USUARIO = "usuario";
	
	private ServicioPersistencia servicioPersistencia;
	private static ContactoIndividualDAO_TDS instancia;
	
	public static ContactoIndividualDAO_TDS getInstancia() {
		if (instancia == null) instancia = new ContactoIndividualDAO_TDS();
		return instancia;
	}
	
	public ContactoIndividualDAO_TDS() {
		this.servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
    @Override
    public void registrarContactoIndividual(ContactoIndividual contacto) {
    	Entidad eIndividual = null;
		try {
			eIndividual = servicioPersistencia.recuperarEntidad(contacto.getId());
			
			if (eIndividual != null) {
		        throw new Exception("Error: el contacto ya existe en el sistema.");
		    }
		} catch (Exception e) {
			eIndividual = null;
		}
		eIndividual = servicioPersistencia.registrarEntidad(IndividualToEntidad(contacto)) ;
		contacto.setId(eIndividual.getId());
    }

    @Override
    public void borrarContactoIndividual(ContactoIndividual contacto) {
    	Entidad eIndividual = servicioPersistencia.recuperarEntidad(contacto.getId());
		servicioPersistencia.borrarEntidad(eIndividual);
    }

    @Override
    public void modificaContactoIndividual(ContactoIndividual contacto) {

    	Entidad eIndividual = servicioPersistencia.recuperarEntidad(contacto.getId());
		for (Propiedad prop : eIndividual.getPropiedades()) {
			if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(contacto.getNombre());
			} else if (prop.getNombre().equals(USUARIO)) {
				prop.setValor(String.valueOf(contacto.getUsuario().getId()));
			} else if (prop.getNombre().equals(MENSAJES)) {
				prop.setValor(obtenerIDsMensajes(contacto.getMensajes()));
			}
			
			servicioPersistencia.modificarPropiedad(prop);	
		}
		if (PoolDAO.getInstance().contains(contacto.getId())) {
			PoolDAO.getInstance().changeObject(contacto.getId(), contacto);
		}
    }

    @Override
    public ContactoIndividual recuperarContactoIndividual(int id) {
		if (PoolDAO.getInstance().contains(id)) {
			return (ContactoIndividual) PoolDAO.getInstance().getObject(id);
		}
    	
    	Entidad eIndividual = servicioPersistencia.recuperarEntidad(id);
        if(eIndividual==null) return null;
        ContactoIndividual c = entidadToIndividual(eIndividual);
        
		PoolDAO.getInstance().addObject(id, c);

        return c;
    }

    @Override
    public List<ContactoIndividual> recuperarTodosContactosIndividual() {
    	return servicioPersistencia.recuperarEntidades("ContactoIndividual").stream()
			.map(e->entidadToIndividual(e))
			.collect(Collectors.toList());
    }
    
    private String obtenerIDsMensajes(List<Mensaje> mensajes) {
		return mensajes.stream()
			    .map(m -> String.valueOf(m.getId()))
			    .collect(Collectors.joining(" "));
	}
	
    private List<Mensaje> obtenerMensajesIDs(String mensajes){

		List<Mensaje> listaMensajes = new LinkedList<Mensaje>();
		StringTokenizer strTok = new StringTokenizer(mensajes, " ");
		while(strTok.hasMoreTokens()) {
			try {
				listaMensajes.add(FactoriaDAO.getInstancia("persistencia.FactoriaDAO_TDS").crearMensajeDAO().recuperarMensaje(Integer.valueOf((String)strTok.nextElement())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listaMensajes;
	}
    
    private ContactoIndividual entidadToIndividual(Entidad eIndividual) {
    	String nombre = servicioPersistencia.recuperarPropiedadEntidad(eIndividual, NOMBRE);
		int usuario = Integer.parseInt(servicioPersistencia.recuperarPropiedadEntidad(eIndividual, USUARIO));
		List<Mensaje> mensajes = obtenerMensajesIDs(servicioPersistencia.recuperarPropiedadEntidad(eIndividual, MENSAJES));
		
		
		Usuario u = null;
		try {
			u = FactoriaDAO.getInstancia("persistencia.FactoriaDAO_TDS").crearUsuarioDAO().recuperarUsuario(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ContactoIndividual individual = new ContactoIndividual(u, nombre);
		individual.setMensajes(mensajes);
		individual.setId(eIndividual.getId());
		
		return individual;
	}
    
    private Entidad IndividualToEntidad(ContactoIndividual individual) {
		Entidad eIndividual = new Entidad();
		eIndividual.setNombre("ContactoIndividual");
		eIndividual.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad(NOMBRE, individual.getNombre()),
						new Propiedad(USUARIO, String.valueOf(individual.getUsuario().getId())),
						new Propiedad(MENSAJES, obtenerIDsMensajes(individual.getMensajes())))));
		return eIndividual;
	}
}
