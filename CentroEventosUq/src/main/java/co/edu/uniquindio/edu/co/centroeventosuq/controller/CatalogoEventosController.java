package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Observer;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Compra;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;

public class CatalogoEventosController implements Observer{

    public TableColumn<Compra,String> ctvIdEvento2;
    public TableColumn<Compra,String> ctvTotalCompra;
    public TableColumn<Compra,String> ctvCodioCompra;
    public TableColumn<Compra,String> ctvMetodoPago;
    public TableView<Compra> tvCompras;
    @FXML
    private Button buscarEvento;

    @FXML
    private TableColumn<Evento, String> ctvFecha;

    @FXML
    private TableColumn<Evento, String> ctvHoraInicio;

    @FXML
    private TableColumn<Evento, String> ctvIdEvento;

    @FXML
    private TableColumn<Evento, String> ctvLugar;

    @FXML
    private TableColumn<Evento, String> ctvVoletasDisponibles;
    @FXML
    private TableColumn<Evento, String> ctvNombre;
    @FXML
    private TableView<Evento> tvEventos;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private Button limpiarCampos;



    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize() throws IOException {
        modelFactoryController = ModelFactoryController.getInstance();
        modelFactoryController.setCatalogoEventosController(this);
        modelFactoryController.inicializarObersevado(this);

        formatearColumnas();
        formatearColumnasCompra();
        llenarTabla();
        llenarTablaCompras();

    }

    private void llenarTablaCompras() {
        tvCompras.getItems().clear();
        tvCompras.getItems().addAll(modelFactoryController.obtenerComprasUsuarioLogeado());
    }

    private void formatearColumnasCompra() {
        ctvIdEvento2.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getEvento().getIdEvento()));
        ctvMetodoPago.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getMetodoPago())));
        ctvCodioCompra.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getCodigoCompra()));
        ctvTotalCompra.setCellValueFactory(cellData-> new SimpleStringProperty(String.valueOf(cellData.getValue().calcularPrecio())));
    }

    private void formatearColumnas() {
        ctvFecha.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getFecha())));
        ctvNombre.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getNombre()));
        ctvLugar.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getLugar()));
        ctvIdEvento.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getIdEvento()));
        ctvHoraInicio.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getHoraInicioEvento())));
        ctvVoletasDisponibles.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().numeroVoletasTotales())));
    }

    private void llenarTabla() {
        tvEventos.getItems().clear();
        tvEventos.getItems().addAll(modelFactoryController.getCentroEventos().getEventos());
    }

    @FXML
    void buscarEvento(ActionEvent event) {
        tvEventos.getItems().clear();
        tvEventos.getItems().addAll(modelFactoryController.filtrarEvetos(dpFecha.getValue()));
    }

    @FXML
    void limpiarCampos(ActionEvent event) {
        dpFecha.setValue(null);
        dpFecha.setPromptText("ingresar la fecha por la que desea filtrar");
        llenarTabla();
    }

    @FXML
    void verPrecioBoletas(ActionEvent event) {
        Evento evento= tvEventos.getSelectionModel().getSelectedItem();
        if(evento!=null){
            FXMLLoader loader= modelFactoryController.navegar("PrecioBoletas.fxml");
            PrecioBoletasController controller= loader.getController();
            controller.cambiarPrecios(evento);
        }

    }

    @FXML
    void verPresentadores(ActionEvent event) {
        Evento evento= tvEventos.getSelectionModel().getSelectedItem();
        if(evento!=null){
            FXMLLoader loader= modelFactoryController.navegar("InformacionArtistas.fxml");
            InformacionDelPersonalController controller=loader.getController();
            controller.llenarLista(evento);
        }else {
            System.out.println("no seleciono nada");
        }

    }


    @FXML
    void comprarBoleta(ActionEvent event) {
        Evento evento= tvEventos.getSelectionModel().getSelectedItem();

            if(evento!=null){
                if(evento.isTaquillaAbierta()){
                    FXMLLoader loader= modelFactoryController.navegar("comprarBoletas.fxml");
                    ComprarBoletasController controller=loader.getController();
                    controller.inicializarDatos(modelFactoryController.getCentroEventos().getUsuarioLogeado().getCorreo(),evento.getIdEvento(),this);
                    controller.encolarCompra(evento);
                }else {
                    modelFactoryController.mostrarAlerta("lo sentimos el evento todavia no abre su taquilla", Alert.AlertType.INFORMATION);
                }
            } else {
                modelFactoryController.mostrarAlerta("no seleciono ningun evento", Alert.AlertType.INFORMATION);
            }


    }

    @FXML
    void cerrarSeccion(ActionEvent event) {
        modelFactoryController.navegar("InicioGeneral.fxml");
        modelFactoryController.cerrarSeccion();
        modelFactoryController.cerrarVentana(tvEventos);
    }

    @Override
    public void notificar() {
        llenarTablaCompras();
        llenarTabla();
    }


}
