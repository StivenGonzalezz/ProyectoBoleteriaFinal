package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.model.Boleta;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Locacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class llenarInformacionBoleta {
    String correoComprador;
    String idEvento;
    Locacion locacion;
    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize() throws IOException {
        modelFactoryController = ModelFactoryController.getInstance();
    }
    ComprarBoletasController controller;
    public void inicializarDatos(String correoCuentaComprador, String idEvento, Locacion locacion, ComprarBoletasController comprarBoletasController) {
        this.correoComprador=correoCuentaComprador;
        this.idEvento=idEvento;
        this.locacion=locacion;
        llenarCampos();
        controller=comprarBoletasController;
    }

    private void llenarCampos() {
        txtPrecio.setText(String.valueOf(locacion.getPrecio()));
        txtTipoAsiento.setText(String.valueOf(locacion.getCategoriaLocalizacion()));
    }

    @FXML
    private TextField textFCedula;

    @FXML
    private TextField textFNombre;

    @FXML
    private Text txtPrecio;

    @FXML
    private Text txtTipoAsiento;

    @FXML
    void comprarBoleta(ActionEvent event) throws IOException {
        if(!textFNombre.getText().isBlank()){
            if(!textFCedula.getText().isBlank()){
                Boleta boleta= new Boleta(
                        modelFactoryController.generarIdBoleta(),
                        modelFactoryController.obtenerEvento(idEvento),
                        txtTipoAsiento.getText(),
                        textFNombre.getText(),
                        textFCedula.getText()
                        );
                locacion.setNumeroVoletasVendidas(locacion.getNumeroVoletasVendidas()+1);
                controller.addBoleta(boleta);
                controller.notificar();
                modelFactoryController.cerrarVentana(textFCedula);

            }else {
                modelFactoryController.mostrarAlerta("debe ingresar la cedula del dueño de la boleta", Alert.AlertType.ERROR);
            }
        }else {
            modelFactoryController.mostrarAlerta("debe ingresar el nombre del dueño de la boleta", Alert.AlertType.ERROR);
        }
    }
}
