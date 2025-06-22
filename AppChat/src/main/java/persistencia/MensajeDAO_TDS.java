package persistencia;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.Mensaje;
import dominio.TipoMensaje;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class MensajeDAO_TDS implements MensajeDAO {
	
	private static final String MENSAJE = "Mensaje";
	private static final String TEXTO = "Texto";
	private static final String FECHA = "Nacimiento";
	private static final String EMISOR = "Emisor";
	private static final String RECEPTOR = "Receptor";
	private static final String TIPO = "Tipo";
	private static final String EMOTICONO = "Emoticono";
	
	private ServicioPersistencia servicioPersistencia;
	private static MensajeDAO_TDS instancia ;
	
	public static MensajeDAO_TDS getInstancia() {
		if(instancia == null) instancia = new MensajeDAO_TDS();
		return instancia;
	}
	
	public MensajeDAO_TDS() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	private Mensaje entidadToMensaje(Entidad eMensaje){
		String texto = servicioPersistencia.recuperarPropiedadEntidad(eMensaje, TEXTO);
		LocalDateTime fecha = LocalDateTime.parse(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, FECHA), DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));
		String emisor = servicioPersistencia.recuperarPropiedadEntidad(eMensaje, EMISOR);
		String receptor = servicioPersistencia.recuperarPropiedadEntidad(eMensaje, RECEPTOR);
		String tipo = servicioPersistencia.recuperarPropiedadEntidad(eMensaje, TIPO);
		int emoticono = Integer.parseInt(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO));
		
		Mensaje mensaje = new Mensaje(emisor, receptor, texto, emoticono, TipoMensaje.valueOf(tipo));
		mensaje.setFecha(fecha);
		mensaje.setId(eMensaje.getId());
		
		return mensaje;
	}
	
	private Entidad mensajeToEntidad(Mensaje mensaje) {
		Entidad eMensaje = new Entidad();
		eMensaje.setNombre(MENSAJE);
		eMensaje.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad(TEXTO, mensaje.getTexto()),
						new Propiedad(EMOTICONO, String.valueOf(mensaje.getEmoticono())),
						new Propiedad(FECHA, mensaje.getFechaEnvio().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"))),
						new Propiedad(EMISOR, mensaje.getEmisor()),
						new Propiedad(RECEPTOR, mensaje.getReceptor()),
						new Propiedad(TIPO, mensaje.getTipoMensaje().name()))));
		
		return eMensaje;
	}
	
    @Override
    public void registrarMensaje(Mensaje mensaje) {
    	Entidad eMensaje = null;
    	try {
			eMensaje = servicioPersistencia.recuperarEntidad(mensaje.getId());
			
			if (eMensaje != null) {
		        throw new Exception("Error: el mensaje ya existe.");
		    }
		} catch (Exception e) {
			eMensaje = null;
		}

		eMensaje=servicioPersistencia.registrarEntidad(mensajeToEntidad(mensaje));
		mensaje.setId(eMensaje.getId());
    }

    @Override
    public void borrarMensaje(Mensaje mensaje) {
    	Entidad eMensaje = servicioPersistencia.recuperarEntidad(mensaje.getId());
		servicioPersistencia.borrarEntidad(eMensaje);
    }

    @Override
    public void modificarMensaje(Mensaje mensaje) {
    	Entidad eMensaje = servicioPersistencia.recuperarEntidad(mensaje.getId());
		for (Propiedad prop : eMensaje.getPropiedades()) {
			if (prop.getNombre().equals(TEXTO)) {
				prop.setValor(mensaje.getTexto());
			}
			else if (prop.getNombre().equals(EMOTICONO)) {
				prop.setValor(String.valueOf(mensaje.getEmoticono()));
			}
			else if (prop.getNombre().equals(FECHA)) {
				prop.setValor(mensaje.getFechaEnvio().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
			} else if (prop.getNombre().equals(EMISOR)) {
				prop.setValor(mensaje.getEmisor());
			} else if (prop.getNombre().equals(RECEPTOR)) {
				prop.setValor(mensaje.getReceptor());
            } else if (prop.getNombre().equals(TIPO)) {
				prop.setValor(mensaje.getTipoMensaje().name());
			}
			
			servicioPersistencia.modificarPropiedad(prop);
		}
		if (PoolDAO.getInstance().contains(mensaje.getId())) {
			PoolDAO.getInstance().changeObject(mensaje.getId(), mensaje);
		}
    }

    @Override
    public Mensaje recuperarMensaje(int id) {
		if (PoolDAO.getInstance().contains(id)) {
			return (Mensaje) PoolDAO.getInstance().getObject(id);
		}
    	
    	Entidad eMensaje = servicioPersistencia.recuperarEntidad(id);
        if(eMensaje==null) return null;
        Mensaje m = entidadToMensaje(eMensaje);
		
        PoolDAO.getInstance().addObject(id, m);
		
        return m;
    }

    @Override
    public List<Mensaje> recuperarTodosMensajes() {
    	return servicioPersistencia.recuperarEntidades(MENSAJE).stream()
				.map(e -> entidadToMensaje(e))
				.collect(Collectors.toList());
    }
}
