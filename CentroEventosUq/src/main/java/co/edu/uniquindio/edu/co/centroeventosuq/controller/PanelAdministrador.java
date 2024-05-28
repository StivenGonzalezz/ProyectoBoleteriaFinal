package co.edu.uniquindio.edu.co.centroeventosuq.controller;



import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Observer;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.time.LocalDate;

public class PanelAdministrador implements Observer {

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

    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize() throws IOException {
        modelFactoryController = ModelFactoryController.getInstance();
        modelFactoryController.setPanelAdministrador(this);
        modelFactoryController.inicializarObersevado(this);
        llenarTabla();
        formatearColumnas();

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
    void agregar(ActionEvent event) {
        FXMLLoader loader= modelFactoryController.navegar("CrearEventos.fxml");
        CrearEventosController controller=loader.getController();
        controller.inicializar(this);
    }

    @FXML
    void eliminar(ActionEvent event) {
        Evento evento= tvEventos.getSelectionModel().getSelectedItem();
        if(evento!=null){
            if(modelFactoryController.eliminarEvento(evento)){
                modelFactoryController.mostrarAlerta("evento eliminado correctamente", Alert.AlertType.INFORMATION);
                notificar();
            }else {
                modelFactoryController.mostrarAlerta("el evento no pudo ser eliminado", Alert.AlertType.INFORMATION);
            }
        }
    }

    @Override
    public void notificar() {
        llenarTabla();
    }

    @FXML
    public void activarTaquilla(ActionEvent actionEvent) {
        Evento evento= tvEventos.getSelectionModel().getSelectedItem();
        if(evento!=null){
            if(LocalDate.parse(evento.getFecha()).equals(LocalDate.now())){
                evento.setTaquillaAbierta(true); //abre la taquilla
                try {
                    modelFactoryController.getCentroEventos().guardarDatos();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                modelFactoryController.mostrarAlerta("todavia no es el dia del evento", Alert.AlertType.INFORMATION);
            }
        }else {
            modelFactoryController.mostrarAlerta("debes selecionar un evento para poder activar su taquilla", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void actualizar(ActionEvent actionEvent) {
        llenarTabla();
    }

    @FXML
    void cerrarSeccion(ActionEvent event) {
        modelFactoryController.navegar("InicioGeneral.fxml");
        modelFactoryController.cerrarSeccion();
        modelFactoryController.cerrarVentana(tvEventos);
    }
}

