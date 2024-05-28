package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Encolado;
import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Observer;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Boleta;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Locacion;
import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.MetodosPago;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class ComprarBoletasController implements Observer,Runnable,Encolado {
    public TableColumn<Locacion,String> ctbTipoLocacion;
    public Text txtTotalPagar;
    public ComboBox<MetodosPago> cbMetodoPago;


    @FXML
    private TableColumn<Locacion, String> ctbCapacidadMax;
    @FXML
    private TableColumn<Locacion, String> ctbDireccion;
    @FXML
    private TableColumn<Locacion, String> ctbPrecioEntrada;
    @FXML
    private TableView<Locacion> tbResidencias;
    @FXML
    private TableColumn<Boleta,String> ctvCategoriaBoleta;

    @FXML
    private TableColumn<Boleta,String>ctvCedulaComprador;

    @FXML
    private TableColumn<Boleta,String> ctvIdBoleta;

    @FXML
    private TableColumn<Boleta,String> ctvIdEvento;

    @FXML
    private TableColumn<Boleta,String> ctvIdNombreComprador;

    @FXML
    private TextField textFNumeroEntradas;

    @FXML
    private TableView<Boleta> tvboletas;
    ModelFactoryController modelFactoryController;

    boolean enCola;
    int personasEnEspera;

    @FXML
    private void initialize() throws IOException, InterruptedException {
        modelFactoryController = ModelFactoryController.getInstance();
        modelFactoryController.setComprarBoletasController(this);
        enCola=true;
        formatearColumnasLocacion();
        formatearColumnasBoletas();
        llenarCombox();
        calcularPrecio();
    }

    private void llenarCombox() {
        cbMetodoPago.getItems().clear();
        cbMetodoPago.getItems().addAll(MetodosPago.values());
    }

    private void formatearColumnasBoletas() {
        ctvIdBoleta.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getIdBoleta()));
        ctvCategoriaBoleta.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getCategoria())));
        ctvCedulaComprador.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getIdComprador()));
        ctvIdNombreComprador.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getNombreComprador()));
        ctvIdEvento.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getEvento().getIdEvento()));
    }

    private void formatearColumnasLocacion() {
        ctbCapacidadMax.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroAsientos()-cellData.getValue().getNumeroVoletasVendidas())));
        ctbPrecioEntrada.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getPrecio())));
        ctbTipoLocacion.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().getCategoriaLocalizacion())));
    }

    Evento evento;
    VentanEspera ventanEsperaController;
    public void encolarCompra(Evento evento) {
        this.evento=evento;
        modelFactoryController.encolarCompra(evento,this);
        FXMLLoader loader = modelFactoryController.navegar("ventanEspera.fxml"); //cargamos la ventana
        ventanEsperaController= loader.getController(); //inicializamos el controlador
       Thread thread= new Thread(this); //hilo que constantemente va estar preguntandole al servidor en que posicion se encuentra
        thread.start(); //iniciamos
    }




    @FXML
    void comprar(ActionEvent event) {
        Locacion locacion= tbResidencias.getSelectionModel().getSelectedItem();
        if(locacion!=null){
            FXMLLoader loader= modelFactoryController.navegar("llenarInformacionBoleta.fxml");
            llenarInformacionBoleta controller= loader.getController();
            controller.inicializarDatos(correoCuentaComprador,idEvento,locacion,this);
        }
    }

    String correoCuentaComprador;
    String idEvento;
    Observer observer;
    public void inicializarDatos(String correo, String idEvento, Observer catalogoEventosController){
        this.correoCuentaComprador=correo;
        this.idEvento=idEvento;
        this.observer=catalogoEventosController;
        llenarTablaLocaciones();

    }

    private void llenarTablaLocaciones() {
        tbResidencias.getItems().clear();
        tbResidencias.getItems().addAll(modelFactoryController.obtenerEvento(idEvento).getLocalizaciones());
    }
    public void addBoleta(Boleta boleta){
        tvboletas.getItems().addAll(boleta);
        txtTotalPagar.setText(txtTotalPagar.getText()+calcularPrecio());
        try {
            ModelFactoryController.getInstance().getCentroEventos().guardarDatos();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void notificar() {
        llenarTablaLocaciones();
    }


    public void terminarCompra(ActionEvent actionEvent) {
        if(cbMetodoPago.getValue()!=null){
            if(!tvboletas.getItems().isEmpty()){
                ArrayList<Boleta>boletas= new ArrayList<>();
                boletas.addAll(tvboletas.getItems());

                if(modelFactoryController.enviarQRS(boletas,correoCuentaComprador,idEvento)){
                    modelFactoryController.mostrarAlerta("felicidades por tu compra", Alert.AlertType.INFORMATION);
                    //despues de generar la compra se realiza el registro de la transaccion en el log
                    modelFactoryController.registrarTransaccion(cbMetodoPago.getValue(),txtTotalPagar.getText(),boletas,idEvento);
                    //notificamos al catalogo para que se vean reflejados el cambio en la tabla (debe disminuir las boletas)
                    observer.notificar();
                    //desconecta del soket para sacarlp de la cola
               //    modelFactoryController.sacarDeCola(idEvento);
                   //una vez terminado todo cerramos la ventana
                   modelFactoryController.cerrarVentana(tvboletas);
                }else {
                    modelFactoryController.mostrarAlerta("lo siento no se pudo realizar la compra", Alert.AlertType.INFORMATION);
                }
                //salir de la cola y generar los datos
            }else {
                modelFactoryController.mostrarAlerta("debe comprar por lo menos alguna boleta", Alert.AlertType.ERROR);
            }
        }else {
            modelFactoryController.mostrarAlerta("debe selecionar un metodo de pago", Alert.AlertType.INFORMATION);
        }

    }

    private double calcularPrecio() {
        double precio=0;
        for (Boleta boleta:tvboletas.getItems()){
            precio+=boleta.getEvento().getLocalizaciones().stream().filter(c->c.getCategoriaLocalizacion().toString().equals(boleta.getCategoria())).findFirst().get().getPrecio();
        }
        return precio;
    }

    int posicionAntigua=0;
    @Override
    public void run() {
        while (enCola) {
            if ((Integer) modelFactoryController.getPosicionCola() != null) {

                int posicion= modelFactoryController.getPosicionCola();
                System.out.println("la posicion es "+ posicion);
                if((Integer)posicionAntigua==null){
                    posicionAntigua=posicion;
                }
                if(posicion<=3){
                    enCola=false;
                }else {
                   cambiarPersonasEnEspera(posicion);
                   //modelFactoryController.pedirPosicionCola(idEvento);
                    if(posicion<posicionAntigua){//significa que alguien cambio en la cola
                        posicionAntigua=posicion;
                        cambiarPersonasEnEspera(posicion);
                        try {
                            Thread.sleep(200000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }

                }

            }
        }

        // Asegurarse de que la ventana se cierre en el hilo de JavaFX
        Platform.runLater(() -> ventanEsperaController.cerrarVentan());
    }

    @Override
    public void cambiarPersonasEnEspera(int personasEnEspera) {
        ventanEsperaController.ponerPersonaEspera(personasEnEspera);
        System.out.println("se enviaron las personas en espera");

    }
}
