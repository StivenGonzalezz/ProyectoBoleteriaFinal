package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Locacion;
import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.CategoriaLocalizacion;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

public class PrecioBoletasController {

    @FXML
    private Text txtPrecioCobre;

    @FXML
    private Text txtPrecioOro;

    @FXML
    private Text txtPrecioPlata;
    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize() throws IOException {
        modelFactoryController = ModelFactoryController.getInstance();
    }

    public void cambiarPrecios(Evento evento) {
        for (Locacion locacion:evento.getLocalizaciones()){
            if(locacion.getCategoriaLocalizacion().equals(CategoriaLocalizacion.ORO)){
                txtPrecioOro.setText(String.valueOf(locacion.getPrecio()));
            }
            if(locacion.getCategoriaLocalizacion().equals(CategoriaLocalizacion.COBRE)){
                txtPrecioCobre.setText(String.valueOf(locacion.getPrecio()));
            }
            if(locacion.getCategoriaLocalizacion().equals(CategoriaLocalizacion.PLATA)){
                txtPrecioPlata.setText(String.valueOf(locacion.getPrecio()));
            }
        }
    }
}

