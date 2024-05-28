package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    public TextField textFCorreo;
    @FXML
    public PasswordField textFContraseña;
    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize() throws IOException {
        modelFactoryController = ModelFactoryController.getInstance();
    }
    Observer observer;
    public void incializarObserver(Observer inicioController) {
        this.observer=inicioController;
    }


    public void Logear(ActionEvent actionEvent) {
        int caso= modelFactoryController.verificar(textFCorreo.getText(),textFContraseña.getText());
        switch (caso) {
            case 1:
                modelFactoryController.navegar("CatalogoEventos.fxml");
                modelFactoryController.cerrarVentana(textFContraseña);
                break;
            case 2:
                modelFactoryController.navegar("panelAdministrador.fxml");
                modelFactoryController.cerrarVentana(textFContraseña);
                break;
            case 0:
                System.out.println("no existe");
        }


    }

    public void cancelar(ActionEvent actionEvent) {
        modelFactoryController.cerrarVentana(textFContraseña);
    }

}
