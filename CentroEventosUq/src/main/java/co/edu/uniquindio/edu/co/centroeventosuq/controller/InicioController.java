package co.edu.uniquindio.edu.co.centroeventosuq.controller;


import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Observer;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.io.IOException;

public class InicioController implements Observer {

    @FXML
    public Text txtTitulo;
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
        modelFactoryController.setInicioController(this);
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

    public void llenarTabla() {
        tvEventos.getItems().clear();
        tvEventos.getItems().addAll(modelFactoryController.getCentroEventos().getEventos());
    }


    @Override
    public void notificar() {
        modelFactoryController.cerrarVentana(txtTitulo);
    }
}
