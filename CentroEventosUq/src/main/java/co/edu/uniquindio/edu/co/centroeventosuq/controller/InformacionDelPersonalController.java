package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class InformacionDelPersonalController {

    @FXML
    private TableColumn<String,String> ctvNombreParticipantes;

    @FXML
    private TableView<String> tvParticipantes;

    @FXML
    private void initialize(){
        formatearColuman();
    }

    private void formatearColuman() {
        ctvNombreParticipantes.setCellValueFactory(cellData -> {
            String nombre = cellData.getValue();
            return new SimpleStringProperty(nombre);
        });
    }

    public void llenarLista(Evento evento) {
        tvParticipantes.getItems().addAll(evento.getNombreArtistas());
    }
}
