package co.edu.uniquindio.edu.co.centroeventosuq.model;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.ModelFactoryController;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Service.CentroEventosService;
import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.CategoriaLocalizacion;
import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.MetodosPago;
import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.TipoUsuario;
import co.edu.uniquindio.edu.co.centroeventosuq.utils.EnviosEmail;
import co.edu.uniquindio.edu.co.centroeventosuq.utils.GenerarQr;
import co.edu.uniquindio.edu.co.centroeventosuq.utils.Persistencia;
import com.google.zxing.WriterException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Getter
@Setter
public class CentroEventos implements CentroEventosService {

    private ArrayList<Compra>compras= new ArrayList<>();
    private ArrayList<Evento> eventos=new ArrayList<>();
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    private Usuario usuarioLogeado=null;

    public CentroEventos() throws IOException {
        cargarDatos();
        if(usuarios.isEmpty()){
            Usuario usuario = new Usuario("admin","admin123","admin123",TipoUsuario.ADMINISTRADOR);
            Usuario usuario1 = new Usuario("usuario","usuario123","usuario123",TipoUsuario.USUARIO);
            usuarios.add(usuario1);
            usuarios.add(usuario);
            guardarDatos();
        }

        if(eventos.isEmpty()){

            Evento evento= new Evento();
            evento.setIdEvento("123");
            evento.setNombre("mi primer evento");
            evento.setHoraInicioEvento("11:00");
            evento.setFecha(LocalDate.now().toString());
            evento.setLugar("lugar");
            evento.setSemaphore(new Semaphore(3,true));
            evento.getNombreArtistas().add("JUAN PEREZ");
            evento.getLocalizaciones().add(new Locacion(123,123, CategoriaLocalizacion.COBRE));
            evento.getLocalizaciones().add(new Locacion(1233,123, CategoriaLocalizacion.PLATA));
            evento.getLocalizaciones().add(new Locacion(1234,12,CategoriaLocalizacion.ORO));

            eventos.add(evento);
            guardarDatos();

        }
    }

    public void cargarDatos() throws IOException {



        ArrayList<Usuario>usuariosXML= Persistencia.cargarUsuariosXML();
        if(usuariosXML!=null){
            usuarios.clear();
            usuarios.addAll(usuariosXML);
        }
        ArrayList<Evento>eventosXML= Persistencia.cargarEventosXML();
        if(eventosXML!=null){
            eventos.clear();
            eventos.addAll(eventosXML);

        }
        ArrayList<Compra>comprasXML=new ArrayList<>();
        if(comprasXML!=null){
            compras.clear();
            compras.addAll(comprasXML);
        }
    }


    @Override
    public boolean registrarLocaciones(Locacion locacion) {
        return false;
    }

    @Override
    public boolean addEvento(Evento evento) {
        try {
            if(evento==null){
                throw new RuntimeException("el evento que se desea agregar es nulo");
            }
            eventos.add(evento);
            guardarDatos();
            return true;
        }catch (RuntimeException e){
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarDatos() throws IOException {
        Persistencia.guardarUsuarioXML(usuarios);
        Persistencia.guardarEventosXML(eventos);
        Persistencia.guardarComprasXML(compras);
    }

    @Override
    public List<Evento> filtrarEvetos(LocalDate fecha) {
        if(fecha!=null){
            return eventos.stream().filter(evento -> evento.getFecha().equals(String.valueOf(fecha))).collect(Collectors.toList());
        }else {
            return eventos;
        }

    }

    @Override
    public boolean deleteEvento(String idEvento) {
        try {
            Evento evento= obtenerEvento(idEvento);
            if(evento==null){
                throw new RuntimeException("el evento a eliminar es nulo");
            }
            eventos.remove(evento);
            guardarDatos();
            return true;
        }catch (RuntimeException e){
            e.getMessage();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int registrarUsuario(Usuario usuario) {
        if(verificarExisteCorreo(usuario.getCorreo())){
            return 1;
        }else{
            usuarios.add(usuario);
            try {
                guardarDatos();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 2;
        }
    }

    private boolean verificarExisteCorreo(String correo) {
        boolean existe = false;
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equals(correo)) {
                existe = true;
            }
        }
        return existe;
    }

    @Override
    public int loguear(String correo, String contrasena) {
        return 0;
    }

    public String generarIdEvento() {

        Random random = new Random();
        int codigo = random.nextInt(10000);
        String codigoString = String.format("%04d", codigo);
        if(codigoExiste(codigoString)){
            generarIdEvento();
        }
        return codigoString;
    }

    private boolean codigoExiste(String codigoString) {
        boolean existe=false;
        for (Evento evento: eventos){
            if(evento.getIdEvento().equals(codigoString)){
                existe=true;
                break;
            }
        }
        return existe;

    }

    public Evento obtenerEvento(String idEvento) {
        return eventos.stream().filter(c->c.getIdEvento().equals(idEvento)).findFirst().get();
    }

    public int verificar(String correo, String contrasena) {
        for (Usuario usuario : usuarios){
            if(usuario.getCorreo().equals(correo)&& usuario.getContrasena().equals(contrasena)){
                this.usuarioLogeado=usuario;
                try {
                    ModelFactoryController.getInstance().getClienteTaquilla().setUsuarioAsociado(usuario);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(usuario.getTipoUsuario().equals(TipoUsuario.USUARIO)){
                    return 1;
                }else {
                    return 2;
                }

            }
        }

        return 0;

    }

    public String generarIdBoleta() {
        String idBoleta= generarIdEvento();
        if(codigoExisteBoleta(idBoleta)){
            return generarIdBoleta();
        }
        return idBoleta;
    }

    private boolean codigoExisteBoleta(String idBoleta) {
        ArrayList<Boleta>boletas= crearListaBoletas();
        for (Boleta boleta:boletas){
            if(boleta.getIdBoleta().equals(idBoleta)){
                return true;
            }
        }
        return false;
    }

    private ArrayList<Boleta> crearListaBoletas() {
        ArrayList<Boleta> boletas= new ArrayList<>();
        for (Compra compra:compras){
            boletas.addAll(compra.getBoletas());
        }
        return boletas;
    }

    public boolean eliminarEvento(Evento evento) {
        try {
            if(evento==null){
                throw new RuntimeException("el evento a eliminar es nulo");
            }
            if(eventos.contains(evento)){
                eventos.remove(evento);
                guardarDatos();
                return true;
            }else {
                return false;
            }
        }catch (RuntimeException | IOException e){

            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean enviarQRS(ArrayList<Boleta> boletas, String correoCuentaComprador, String idEvento) {
        try {
            Usuario usuario = obtenerUsuario(correoCuentaComprador);
            Evento evento = obtenerEvento(idEvento);
            if (boletas == null || boletas.isEmpty()) {
                throw new RuntimeException("para enviar los qrs debe al menos tener una boleta lista vacia o nula");
            }
            if (usuario == null) {
                throw new RuntimeException("el evento con id" + idEvento + " no existe");
            }
            if (evento == null) {
                throw new RuntimeException("el usuario con correo" + correoCuentaComprador + "no existe");
            }
            guardarDatos();
           // de generan qrs por cada una de las boletas que hallas sido guardados y se guardan en una carpeta con todos los qrs
            for (Boleta boleta:boletas){
                GenerarQr.generateQRCode(boleta.getIdBoleta(),boleta.getIdBoleta());
                String rutaQR=GenerarQr.RUTA_QRS+boleta.getIdBoleta()+".jpg";
                EnviosEmail enviosEmail= new EnviosEmail(correoCuentaComprador,"entrega de entrada electronica","felicidades por su compra en la zona"+ boleta.getCategoria(),rutaQR);
                enviosEmail.enviarNotificacionConImagen();
            }
            Persistencia.crearDirectorioYArchivoXML(boletas.get(0).getEvento().getNombre(),"boletas.xml",boletas);
            return true;
        }catch (RuntimeException e) {
            e.getMessage();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

    }

    private Usuario obtenerUsuario(String correoCuentaComprador) {
        return usuarios.stream().filter(usuario -> usuario.getCorreo().equals(correoCuentaComprador)).findFirst().get();
    }

    public void registrarTransaccion(MetodosPago metodoPago, String totalTransaccion, ArrayList<Boleta> numeroVoletas, String idEvento) {
        //el mensaje que se va registrar en el archivo log
        String mensaje= "el usuario "+usuarioLogeado.getNombre()+"\n"
                +", con el correo" + usuarioLogeado.getCorreo()+"\n"
                +"reliazo una compra con un "+totalTransaccion+"\n"
                +"por comprar "+ numeroVoletas.size() +"boletas"+"\n"
                +"y realizo el pago por "+ metodoPago + "\n"
                +"el evento con la id "+ idEvento
                ;

        //guardamos las comprass
        Compra compra=new Compra(usuarioLogeado,obtenerEvento(idEvento),numeroVoletas,metodoPago,generarCodCompra());
        Usuario usuario=obtenerUsuario(usuarioLogeado.getCorreo());
        usuario.getListaDeCompras().add(compra);
        usuarioLogeado=usuario;
        compras.add(compra);
        try {
            guardarDatos();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //nivel 2 porque es informacion
        Persistencia.guardarLog(mensaje,2,"TRANSACCION");
    }

    private String generarCodCompra() {

        Random random = new Random();
        int codigo = random.nextInt(10000);
        String codigoString = String.format("%04d", codigo);
        if(codigoExiste(codigoString)){
            generarIdEvento();
        }
        return codigoString;
    }

    public ArrayList<Compra> obteneComprasUsuarioLogeado() {
        ArrayList<Compra>compras1= new ArrayList<>();
        if(usuarioLogeado!=null){
            return usuarioLogeado.getListaDeCompras();
        }else {
            return compras1;
        }
    }

    public Boleta obtenerBoleta(String codigoBoleta) throws IOException {
       return Persistencia.buscarBoleta(codigoBoleta);
    }
}
