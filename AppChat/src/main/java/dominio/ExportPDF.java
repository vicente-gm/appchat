package dominio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class ExportPDF {	
	public boolean crearPDF(String ruta, String nombre) {
	    try {
	    	List<Mensaje> mensajes = AppChat.INSTANCE.getMensajesContacto(nombre);
	        Contacto contacto = AppChat.INSTANCE.getContacto(nombre);
	        
	        if(contacto == null || mensajes == null) {
	        	return false;
	        }
	        
	        PdfWriter writer = new PdfWriter(new FileOutputStream(ruta));
	        PdfDocument pdf = new PdfDocument(writer);
	        Document document = new Document(pdf);

	        // Título
	        document.add(new Paragraph("Mensajes con: " + nombre).setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));

	        if(contacto instanceof Grupo) {
	        	document.add(new Paragraph("Componentes del grupo " + nombre + ":").setBold().setFontSize(15));
	        	((Grupo) contacto).getMiembros().stream()
	        		.forEach(m -> document.add(new Paragraph(m.getNombre() + ": " + m.getTelefono()).setFontSize(15)));
	        }
	        
	        // Espacio antes de los mensajes
	        document.add(new Paragraph().setMarginTop(10f));
	        	        
	        // Mensajes
	        mensajes.stream().forEach(m -> {
	        	String texto = m.getTexto();
	        	
	        	if (m.getEmoticono() != -1) {
	        		texto = "*emoji*";
	        	}
	        	
	        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
	        	String fecha = m.getFechaEnvio().format(formatter);
	        	TipoMensaje tipo = m.getTipoMensaje();
	        	
	        	TextAlignment alinea;
	        	String emisor;
	        	
	        	if(tipo == TipoMensaje.ENVIADO || tipo == TipoMensaje.ENVIADO_GRUPO) {
	        		alinea = TextAlignment.RIGHT;
	        		emisor = "Tú";
	        	} else {
	        		alinea = TextAlignment.LEFT;
	        		emisor = nombre;
	        	}
	        	
	        	document.add(new Paragraph(emisor + " - " + fecha).setBold().setFontSize(12).setTextAlignment(alinea));
	        	document.add(new Paragraph(texto).setFontSize(12).setTextAlignment(alinea));
	        	
	        	document.add(new Paragraph().setMarginTop(10f));
	        });
	        
	        document.close();	// Cerramos el documento
	        
	        return true;
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}