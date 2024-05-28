package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.model.Boleta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class InformacionBoletasController {
    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize() throws IOException {
        modelFactoryController = ModelFactoryController.getInstance();
    }

    @FXML
    private TextField textFCodigoBoleta;

    @FXML
    private Text txtCedulaComprador;

    @FXML
    private Text txtNombreComprador;

    @FXML
    private Text txtNombreEvento;

    @FXML
    private Text txtPrecio;

    @FXML
    private Text txtTipoAsiento;

    @FXML
    void consultarBoleta(ActionEvent event) throws IOException {
        if(!textFCodigoBoleta.getText().isEmpty()){
            Boleta boleta= modelFactoryController.buscarBoleta(textFCodigoBoleta.getText());
            if(boleta==null){
                modelFactoryController.mostrarAlerta("la boleta no existe", Alert.AlertType.INFORMATION);
            }else {
                llenarDatos(boleta);
            }
        }else {
            modelFactoryController.mostrarAlerta("debe ingresar un codigo para buscar la boleta", Alert.AlertType.ERROR);
        }
    }

    private void llenarDatos(Boleta boleta) {
        txtCedulaComprador.setText(boleta.getIdComprador());
        txtNombreComprador.setText(boleta.getNombreComprador());
        txtNombreEvento.setText(boleta.getEvento().getNombre());
        txtTipoAsiento.setText(boleta.getCategoria());
        txtPrecio.setText(boleta.getPrecio());
    }

}
