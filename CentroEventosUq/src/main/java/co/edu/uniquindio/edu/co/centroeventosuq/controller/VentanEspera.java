package co.edu.uniquindio.edu.co.centroeventosuq.controller;


import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;

public class VentanEspera {
    @FXML
    public AnchorPane archonCerrar;
    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize(){
        try {
            modelFactoryController = ModelFactoryController.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Text txtPersonaEnCola;


    @FXML
    private TextField textFCerrar;


    public void ponerPersonaEspera(int posicionCola) {
        txtPersonaEnCola.setText(String.valueOf(posicionCola));
    }

    public void cerrarVentan() {
        modelFactoryController.cerrarVentana(archonCerrar);
    }
}

