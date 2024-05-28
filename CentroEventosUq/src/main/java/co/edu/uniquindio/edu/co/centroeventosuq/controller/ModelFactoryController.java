package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.CentroEventosApplication;
import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Encolado;
import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Observer;
import co.edu.uniquindio.edu.co.centroeventosuq.model.*;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Service.CentroEventosService;
import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.MetodosPago;
import co.edu.uniquindio.edu.co.centroeventosuq.sokets.ClienteTaquilla;
import co.edu.uniquindio.edu.co.centroeventosuq.hilos.XMLFileMonitor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModelFactoryController implements CentroEventosService {
    public static final int PuertoServer=8081;
    CentroEventos centroEventos;
    ClienteTaquilla clienteTaquilla;
    XMLFileMonitor xmlFileMonitor;

    public ClienteTaquilla getClienteTaquilla() {
        return clienteTaquilla;
    }

    public ModelFactoryController() throws IOException {
        centroEventos= new CentroEventos();
        clienteTaquilla= new ClienteTaquilla();
        clienteTaquilla.conectar("localhost",PuertoServer);
        System.out.println("se genero otra instancia del controlador principal");
        xmlFileMonitor= new XMLFileMonitor("src/main/resources/Persistencia/Evento.xml");
        xmlFileMonitor.monitor();
    }

    public static ModelFactoryController INSTANCE;

    public static ModelFactoryController getInstance() throws IOException {
        if(INSTANCE==null){
            INSTANCE= new ModelFactoryController();
        }
        return INSTANCE;
    }



    public CentroEventos getCentroEventos() {
        return centroEventos;
    }

    @Override
    public boolean registrarLocaciones(Locacion locacion) {
        return false;
    }

    @Override
    public boolean addEvento(Evento evento) {
        return centroEventos.addEvento(evento);
    }

    @Override
    public boolean deleteEvento(String idEvento) {
        return false;
    }

    @Override
    public int registrarUsuario(Usuario usuario) {
        return centroEventos.registrarUsuario(usuario);
    }

    @Override
    public int loguear(String correo, String contrasena) {
        return 0;
    }

    public String generarIdEvento() {
        return centroEventos.generarIdEvento();
    }

    @Override
    public List<Evento> filtrarEvetos(LocalDate fecha) {
        return centroEventos.filtrarEvetos(fecha);
    }


    public FXMLLoader navegar(String nombreVista){
        try {

            FXMLLoader loader = new FXMLLoader(CentroEventosApplication.class.getResource(nombreVista));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            // Mostrar la nueva ventana
            stage.show();

            return loader;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void cerrarVentana(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    Encolado encolado;
    int posicionCola;
    public void encolarCompra(Evento evento, Encolado comprarBoletasController) {
        this.encolado=comprarBoletasController;
        clienteTaquilla.enviarSolicitudCompra(evento.getIdEvento());
    }

    public Encolado getEncolado() {
        return encolado;
    }

    public int getPosicionCola() {
        return posicionCola;
    }

    public void setPosicionCola(int posicionCola) {
        this.posicionCola = posicionCola;
    }

    public void cambiarEstadoEncolado(){
        System.out.println(" se puede comprar");
    //    encolado.cambiarEstadoEncolado();
    }

    public void DecirPersonasEnEspera(int personasEnEspera) {
        System.out.println("personas en cola");
      //  encolado.cambiarPersonasEnEspera(personasEnEspera);
    }

    public void ComprarBoletas(){
        clienteTaquilla.finalizarCompra("Evento1");

        //  clienteTaquilla.cerrarConexion();
    }

    public Evento obtenerEvento(String idEvento) {
        return centroEventos.obtenerEvento(idEvento);
    }

    public int verificar(String correo, String contrasena) {
        return centroEventos.verificar(correo,contrasena);
    }

    private Observer observer;

    public Observer getObserver() {
        return observer;
    }

    public void inicializarObersevado(Observer catalogoEventosController) {
        this.observer= catalogoEventosController;
    }

    public void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle("Alerta - Hotel");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public String generarIdBoleta() {
        return centroEventos.generarIdBoleta();
    }

    public boolean eliminarEvento(Evento evento) {
        return centroEventos.eliminarEvento(evento);
    }

    public boolean enviarQRS(ArrayList<Boleta> boletas, String correoCuentaComprador, String idEvento) {
        return centroEventos.enviarQRS(boletas,correoCuentaComprador,idEvento);
    }

    public void sacarDeCola(String idEvento) {
        clienteTaquilla.cerrarConexion();
    }

    PanelAdministrador panelAdministrador;
    CatalogoEventosController catalogoEventosController;
    InicioController inicioController;
    ComprarBoletasController comprarBoletasController;
    public void actualizarVentas() {
        if(panelAdministrador!=null){
            panelAdministrador.notificar();
        }
        if(catalogoEventosController!=null){
            catalogoEventosController.notificar();
        }
        if(inicioController!=null){
            inicioController.llenarTabla(); //notificar no porque lo que eso haca es cerrar la ventana
        }
        if(comprarBoletasController!=null){
            comprarBoletasController.notificar();
        }
    }

    public void setPanelAdministrador(PanelAdministrador panelAdministrador) {
        this.panelAdministrador = panelAdministrador;
    }

    public void setCatalogoEventosController(CatalogoEventosController catalogoEventosController) {
        this.catalogoEventosController = catalogoEventosController;
    }

    public void setInicioController(InicioController inicioController) {
        this.inicioController = inicioController;
    }

    public ComprarBoletasController getComprarBoletasController() {
        return comprarBoletasController;
    }

    public void setComprarBoletasController(ComprarBoletasController comprarBoletasController) {
        this.comprarBoletasController = comprarBoletasController;
    }

    public void cerrarSeccion() {
        centroEventos.setUsuarioLogeado(null);
    }

    public void registrarTransaccion(MetodosPago metodoPago, String totalTransaccion, ArrayList<Boleta> numeroVoletas, String idEvento) {
        centroEventos.registrarTransaccion(metodoPago,totalTransaccion,numeroVoletas,idEvento);
    }

    public void pedirPosicionCola(String idEvento) {
        posicionCola=clienteTaquilla.pedirPosiconCola(idEvento);
    }

    public ArrayList<Compra> obtenerComprasUsuarioLogeado() {
        return centroEventos.obteneComprasUsuarioLogeado();
    }

    public Boleta buscarBoleta(String codigoBoleta) throws IOException {
        return centroEventos.obtenerBoleta(codigoBoleta);
    }
}
