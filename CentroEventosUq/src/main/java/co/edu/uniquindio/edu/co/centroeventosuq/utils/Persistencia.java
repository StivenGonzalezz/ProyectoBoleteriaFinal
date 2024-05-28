package co.edu.uniquindio.edu.co.centroeventosuq.utils;

import co.edu.uniquindio.edu.co.centroeventosuq.model.Boleta;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Compra;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Usuario;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Persistencia {

    public static final String RUTA_BOLETAS_EVENTOS="src/main/resources/BoletasEventos/";
    public static final String RUTA_ARCHIVO_EVENTOS="src/main/resources/Persistencia/Evento.xml";
    public static final String RUTA_ARCHIVO_LOG="src/main/resources/MyLog/";
    private static final String RUTA_ARCHIVO_COMPRADORES = "src/main/resources/Persistencia/compradores.xml";
    private static final String RUTA_ARCHIVO_COMPRA ="src/main/resources/Persistencia/compras.xml" ;
    private static final String RUTA_ARCHIVO_USUARIO ="src/main/resources/Persistencia/usuarios.xml" ;

    public static void guardarCompradoresXML(ArrayList<Usuario> compradores) throws IOException {
        ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_COMPRADORES,compradores);
    }
    public static void guardarEventosXML(ArrayList<Evento> eventos) throws IOException {
        ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_EVENTOS,eventos);
    }

    public static void guardarLog(String mensajeLog, int nivel, String accion){
        ArchivoUtil.guardarRegistroLog(mensajeLog,nivel,accion,RUTA_ARCHIVO_LOG);
    }

    public static void guardarComprasXML(ArrayList<Compra> compras) throws IOException {
        ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_COMPRA,compras);
    }

    public static void guardarUsuarioXML(ArrayList<Usuario> usuarios) throws IOException {
        ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_USUARIO,usuarios);
    }


    public static ArrayList<Usuario> cargarUsuariosXML() throws IOException {
        return (ArrayList<Usuario>) ArchivoUtil.cargarRecursoSerializadoXML(RUTA_ARCHIVO_USUARIO);
    }

    public static ArrayList<Evento> cargarEventosXML()throws IOException{
        return (ArrayList<Evento>) ArchivoUtil.cargarRecursoSerializadoXML(RUTA_ARCHIVO_EVENTOS);
    }

    public static ArrayList<Compra> cargarComprasXML()throws IOException{
        return (ArrayList<Compra>) ArchivoUtil.cargarRecursoSerializadoXML(RUTA_ARCHIVO_COMPRA);
    }



    public static void crearDirectorioYArchivoXML(String nombreDirectorio, String nombreArchivo, ArrayList<Boleta>boletas) throws IOException {
        // Crear el directorio
        File dir = new File(RUTA_BOLETAS_EVENTOS+nombreDirectorio);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Directorio creado: " + nombreDirectorio);
            } else {
                System.out.println("Error al crear el directorio: " + nombreDirectorio);
            }
        } else {
            File xml= new File(dir.getAbsolutePath(),nombreArchivo);
            if(xml.exists()){
                ArchivoUtil.salvarRecursoSerializadoXML(xml.getAbsolutePath(),boletas);
            }
        }

        // Crear el archivo XML dentro del directorio
        File archivoXML = new File(dir, nombreArchivo);
        try {
            // Crear el documento XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Elemento raíz
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // Ejemplo de elemento hijo
            Element elementoHijo = doc.createElement("elemento");
            elementoHijo.appendChild(doc.createTextNode("Contenido del elemento"));
            rootElement.appendChild(elementoHijo);

            // Escribir el contenido en el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivoXML);

            transformer.transform(source, result);

            ArchivoUtil.salvarRecursoSerializadoXML(archivoXML.getAbsolutePath(),boletas);



            System.out.println("Archivo XML creado: " + archivoXML.getAbsolutePath());
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boleta buscarBoleta(String idBoleta) throws IOException {
        Boleta boleta=null;
        File directorio = new File(RUTA_BOLETAS_EVENTOS);
        File[] subdirectorios = directorio.listFiles(File::isDirectory);

        if (subdirectorios != null) {
            for (File subdirectorio : subdirectorios) {
                File archivoXML = new File(subdirectorio, "boletas.xml");
                if (archivoXML.exists()) {
                    boolean contieneIdEvento = contieneIdBoleta(archivoXML, idBoleta);
                    if (contieneIdEvento) {
                        ArrayList<Boleta> boletas=(ArrayList<Boleta>) ArchivoUtil.cargarRecursoSerializadoXML(archivoXML.getAbsolutePath());
                         boleta= boletas.stream().filter(boleta1 -> boleta1.getIdBoleta().equals(idBoleta)).findFirst().get();
                         break;
                    } else {
                        System.out.println("El archivo " + archivoXML.getPath() + " no contiene el idEvento: " + idBoleta);
                    }
                }
            }
        }
        return boleta;
    }

    public static boolean contieneIdBoleta(File archivoXML, String idBoletaBuscado) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(archivoXML);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression expression = xPath.compile("//void[@property='idBoleta']/string[text()='" + idBoletaBuscado + "']");

            NodeList nodes = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

            return nodes.getLength() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void sobrescribirXML( File archivoNuevo) throws IOException {
        Path rutaExistente = Path.of("C:\\Users\\ANDREY\\Downloads\\CentroEventos\\CentroEventosUq\\src\\main\\resources\\Persistencia\\Evento.xml");
        Path rutaArchivoNuevo = archivoNuevo.toPath();
        Files.copy(rutaArchivoNuevo, rutaExistente, StandardCopyOption.REPLACE_EXISTING);
    }
}

