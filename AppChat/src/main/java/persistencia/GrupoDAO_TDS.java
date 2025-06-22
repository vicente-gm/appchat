package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.ContactoIndividual;
import dominio.Grupo;
import dominio.Mensaje;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class GrupoDAO_TDS implements GrupoDAO {
	private static final String NOMBRE = "nombre";
	private static final String MENSAJES = "mensajes";
	private static final String CONTACTOS = "contactos";
	
	private ServicioPersistencia servicioPersistencia;
	private static GrupoDAO_TDS instancia;
	
	public static GrupoDAO_TDS getInstancia() {
		if(instancia == null) instancia = new GrupoDAO_TDS();
		return instancia;
	}
	
	public GrupoDAO_TDS() {
		this.servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
    @Override
    public void registrarGrupo(Grupo grupo) {
    	Entidad eGrupo = null;
		try {
			eGrupo = servicioPersistencia.recuperarEntidad(grupo.getId());
			
			if (eGrupo != null) {
		        throw new Exception("Error: el grupo ya existe");
		    }
		} catch (Exception e) {
			eGrupo = null;
		}
		
		eGrupo = servicioPersistencia.registrarEntidad(GrupoToEntidad(grupo));
		grupo.setId(eGrupo.getId());
    }

    @Override
    public void borrarGrupo(Grupo grupo) {
    	Entidad eGrupo = servicioPersistencia.recuperarEntidad(grupo.getId());
		servicioPersistencia.borrarEntidad(eGrupo);
    }

    @Override
    public void modificarGrupo(Grupo grupo) {
    	Entidad eGrupo = servicioPersistencia.recuperarEntidad(grupo.getId());
		for (Propiedad prop : eGrupo.getPropiedades()) {
			if(prop.getNombre().equals(NOMBRE)) {
				prop.setValor(grupo.getNombre());
			} else if(prop.getNombre().equals(MENSAJES)) {
				prop.setValor(obtenerIDsMensajes(grupo.getMensajes()));
			} else if(prop.getNombre().equals(CONTACTOS)) {
				prop.setValor(obtenerIDsContactos(grupo.getMiembros()));
			}
			
			servicioPersistencia.modificarPropiedad(prop);
			if (PoolDAO.getInstance().contains(grupo.getId())) {
				PoolDAO.getInstance().changeObject(grupo.getId(), grupo);
			}
		}
    }

    @Override
    public Grupo recuperarGrupo(int id) {
		if (PoolDAO.getInstance().contains(id)) {
			return (Grupo) PoolDAO.getInstance().getObject(id);
		}
    	
    	Entidad eGrupo = servicioPersistencia.recuperarEntidad(id);
		if(eGrupo == null) return null;
		Grupo grupo = entidadToGrupo(eGrupo);
		
		PoolDAO.getInstance().addObject(id, grupo);
		
		return grupo;    }

    @Override
    public List<Grupo> recuperarTodosGrupos() {
    	return servicioPersistencia.recuperarEntidades("Grupo").stream()
				.map(e -> entidadToGrupo(e))
				.collect(Collectors.toList());
    }
    
    private String obtenerIDsMensajes(List<Mensaje> mensajes) {
		return mensajes.stream()
			    .map(m -> String.valueOf(m.getId()))
			    .collect(Collectors.joining(" "));
	}
    
    private String obtenerIDsContactos(List<ContactoIndividual> contactos) {
		return contactos.stream()
			    .map(c -> String.valueOf(c.getId()))
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
    
    private List<ContactoIndividual> obtenerContactosIDs(String contactos){
		List<ContactoIndividual> listaContactos = new LinkedList<ContactoIndividual>();
		StringTokenizer strTok = new StringTokenizer(contactos, " ");
		while(strTok.hasMoreTokens()) {
			try {
				listaContactos.add(FactoriaDAO.getInstancia("persistencia.FactoriaDAO_TDS").crearContactoIndividualDAO().recuperarContactoIndividual(Integer.valueOf((String)strTok.nextElement())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listaContactos;
	}
    
    private Grupo entidadToGrupo(Entidad eGrupo) {
		String nombre = servicioPersistencia.recuperarPropiedadEntidad(eGrupo, NOMBRE);
		List<Mensaje> mensajes = obtenerMensajesIDs(servicioPersistencia.recuperarPropiedadEntidad(eGrupo, MENSAJES));
		List<ContactoIndividual> contactos = obtenerContactosIDs(servicioPersistencia.recuperarPropiedadEntidad(eGrupo, CONTACTOS));
		Grupo grupo = new Grupo(contactos, nombre);
		grupo.setMensajes(mensajes);
		grupo.setId(eGrupo.getId());
		
		return grupo;
	}
    
    private Entidad GrupoToEntidad(Grupo grupo) {
		Entidad eGrupo = new Entidad();
		eGrupo.setNombre("Grupo");
		eGrupo.setPropiedades(new ArrayList<Propiedad>(
			Arrays.asList(new Propiedad(NOMBRE, grupo.getNombre()),
					new Propiedad(MENSAJES, obtenerIDsMensajes(grupo.getMensajes())),
					new Propiedad(CONTACTOS, obtenerIDsContactos(grupo.getMiembros()))))
		);
		return eGrupo;
	}
}
