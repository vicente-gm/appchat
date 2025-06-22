package persistencia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.Contacto;
import dominio.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;


public class UsuarioDAO_TDS implements UsuarioDAO {

	private ServicioPersistencia servicioPersistencia;
	private static UsuarioDAO_TDS instancia = null;
	
	public static UsuarioDAO_TDS getInstancia() {
		if(instancia == null) instancia = new UsuarioDAO_TDS();
		return instancia;
	}
	
	public UsuarioDAO_TDS() {
		this.servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
   	@Override
    public void registrarUsuarios(Usuario usuario) {
   		Entidad eUsuario = null;
   		
   		try {
   			eUsuario = this.servicioPersistencia.recuperarEntidad(usuario.getId());
   			if (eUsuario != null) {
		        throw new Exception("Error: el usuario ya existe.");
		    }
   		} catch (Exception e) {
			eUsuario = null;
		}
   		
   		eUsuario = new Entidad();
   		eUsuario.setNombre("Usuario");
   		eUsuario.setPropiedades(new ArrayList<Propiedad>(
   				Arrays.asList(new Propiedad("Nombre", usuario.getNombre()),
   						new Propiedad("Apellido", usuario.getApellidos()),
   						new Propiedad("Password", usuario.getClave()),
   						new Propiedad("Telefono", usuario.getTelefono()),
   						new Propiedad("Premium", String.valueOf(usuario.isPremium())),
   						new Propiedad("Nacimiento", usuario.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
   						new Propiedad("Registro", usuario.getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
   						new Propiedad(
   							    "FechaPremium",
   							    usuario.getFechaPremium() != null 
   							        ? usuario.getFechaPremium().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) 
   							        : "Null"
   						),
   						new Propiedad("Saludo", usuario.getSaludo()),
   						new Propiedad("RutaImagen", usuario.getImagen()),
   						new Propiedad("Contactos", obtenerCodigosContactos(usuario.getContactos()))
   				)));
   		eUsuario = this.servicioPersistencia.registrarEntidad(eUsuario);
   		usuario.setId(eUsuario.getId());
    }

    @Override
    public void borrarUsuarios(Usuario usuario) {
    	Entidad eUsuario = servicioPersistencia.recuperarEntidad(usuario.getId());
		servicioPersistencia.borrarEntidad(eUsuario);
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servicioPersistencia.recuperarEntidad(usuario.getId());
		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals("Nombre")) {
				prop.setValor(usuario.getNombre());
			} else if (prop.getNombre().equals("Apellido")) {
				prop.setValor(usuario.getApellidos());
			} else if (prop.getNombre().equals("Password")) {
				prop.setValor(usuario.getClave());
			} else if (prop.getNombre().equals("Telefono")) {
				prop.setValor(usuario.getTelefono());
			} else if (prop.getNombre().equals("Premium")) {
				prop.setValor(String.valueOf(usuario.isPremium()));
			} else if (prop.getNombre().equals("Nacimiento")) {
				prop.setValor(usuario.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			} else if (prop.getNombre().equals("Registro")) {
				prop.setValor(usuario.getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			} else if (prop.getNombre().equals("FechaPremium")) {
			    if (!prop.getValor().equals("Null")) {
			        usuario.setFechaPremium(LocalDate.parse(
			            prop.getValor(), 
			            DateTimeFormatter.ofPattern("dd/MM/yyyy")
			        ));
			    } else {
			        usuario.setFechaPremium(null);
			    }
			} else if (prop.getNombre().equals("Saludo")) {
				prop.setValor(usuario.getSaludo());
			} else if (prop.getNombre().equals("RutaImagen")) {
				prop.setValor(usuario.getImagen());
			} else if (prop.getNombre().equals("Contactos")) {
				prop.setValor(obtenerCodigosContactos(usuario.getContactos()));
			}

			servicioPersistencia.modificarPropiedad(prop);
		}
		if (PoolDAO.getInstance().contains(usuario.getId())) {
			PoolDAO.getInstance().changeObject(usuario.getId(), usuario);
		}
    }

    @Override
    public Usuario recuperarUsuario(int id) {
		if (PoolDAO.getInstance().contains(id)) {
			return (Usuario) PoolDAO.getInstance().getObject(id);
		}
    	
    	Entidad eUsuario = servicioPersistencia.recuperarEntidad(id);
		if (eUsuario == null) return null;
		String nombre = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Nombre");
		String apellido = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Apellido");
		String password = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Password");
		String telefono = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Telefono");
		boolean premium = Boolean.parseBoolean(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Premium"));
		LocalDate nacimiento = LocalDate.parse(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Nacimiento"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate registro = LocalDate.parse(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Registro"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		LocalDate fechaPremium = null;
		if (!servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "FechaPremium").equals("Null")) {
			fechaPremium = LocalDate.parse(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "FechaPremium"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	    }

		String saludo = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Saludo");
		String urlImagen = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "RutaImagen");
		
		Usuario usuario = new Usuario(nombre, apellido, telefono, password,  saludo, nacimiento, urlImagen, registro, false);
		usuario.setPremium(premium);
		usuario.setFechaPremium(fechaPremium);
		
		PoolDAO.getInstance().addObject(id, usuario);
		
		List<Contacto> contactos = obtenerContactosIDs(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "Contactos"));
		usuario.setId(id);
		usuario.setContactos(contactos);
		return usuario;
    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() {
    	return servicioPersistencia.recuperarEntidades("Usuario").stream()
				.map(eUsuario -> recuperarUsuario(eUsuario.getId()))
				.collect(Collectors.toList());
    }
    
    private String obtenerCodigosContactos(List<Contacto> listaContactos) {
		return listaContactos.stream()
			    .map(c -> String.valueOf(c.getId()))
			    .collect(Collectors.joining(" "));
	}
    
    private List<Contacto> obtenerContactosIDs(String idsContactos){
		List<Contacto> contactos = new LinkedList<Contacto>();
		StringTokenizer strTok = new StringTokenizer(idsContactos, " ");
		while ( strTok.hasMoreElements()) {
			int id = Integer.parseInt((String) strTok.nextElement());
			Entidad eContacto = servicioPersistencia.recuperarEntidad(id);
	        String tipo = eContacto.getNombre();
	        if ("Grupo".equals(tipo)) {
	            try {
					contactos.add(FactoriaDAO.getInstancia("persistencia.FactoriaDAO_TDS").crearGrupoDAO().recuperarGrupo(id));
				} catch (Exception e) {
					e.printStackTrace();
				}
	        } else if ("ContactoIndividual".equals(tipo)) {
	        	try {
					contactos.add(FactoriaDAO.getInstancia("persistencia.FactoriaDAO_TDS").crearContactoIndividualDAO().recuperarContactoIndividual(id));
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
		}
		return contactos;
	}
}
