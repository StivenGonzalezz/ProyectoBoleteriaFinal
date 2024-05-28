package co.edu.uniquindio.edu.co.centroeventosuq.controller;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.service.Observer;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Locacion;
import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.CategoriaLocalizacion;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CrearEventosController {

    @FXML
    public ComboBox<String>cbHorasDIa;
    @FXML
    private TableColumn<Locacion, Integer> ctvCapacidadMax;

    @FXML
    private TableColumn<String, String> ctvNombreParticipantes;

    @FXML
    private TableView<String> tvNombresParticipantes;

    @FXML
    private TableColumn<Locacion, Double> ctvPrecioEntrada;

    @FXML
    private TableColumn<Locacion, CategoriaLocalizacion> ctvTipoLocalidad;

    @FXML
    private DatePicker dpFechaEvento;

    @FXML
    private TextField textFCapacidadCobre;

    @FXML
    private TextField textFCapacidadMaxPlata;

    @FXML
    private TextField textFCapacidadOro;

    @FXML
    private TextField textFDireccionEvento;

    @FXML
    private TextField textFNombreEvento;

    @FXML
    private TextField textFPrecio;

    @FXML
    private TextField textFnombreParticipante;

    @FXML
    private TableView<Locacion> tvLocalidades;
    ModelFactoryController modelFactoryController;
    @FXML
    private void initialize() throws IOException {
        modelFactoryController = ModelFactoryController.getInstance();
        formatearColumnasLocaciones();
        formatearColumnasParticipantes();
        llenarComboxHoraDia();
    }

    private void llenarComboxHoraDia() {
        cbHorasDIa.getItems().addAll(generarHorasDelDia());
    }

    private List<String> generarHorasDelDia() {
        List<String> horasDelDia = new ArrayList<>();

        // Define el formato de hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Genera las 24 horas del día
        for (int hora = 0; hora < 24; hora++) {
            for (int minuto = 0; minuto < 60; minuto += 45) { // Incrementa los minutos en 15
                LocalTime horaActual = LocalTime.of(hora, minuto);
                horasDelDia.add(horaActual.format(formatter));
            }
        }

        return horasDelDia;
    }

    public LocalTime stringToSystemTime(String inputTime) {
        // Define el formato de la cadena de entrada
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            // Parsea la cadena de entrada en un objeto LocalTime usando el formato definido
            LocalTime systemTime = LocalTime.parse(inputTime, formatter);
            return systemTime;
        } catch (DateTimeParseException e) {
            // En caso de que la cadena no pueda ser parseada correctamente
            System.err.println("Error: La cadena de entrada no está en el formato correcto (HH:mm:ss).");
            e.printStackTrace();
            return null;
        }
    }

    private void formatearColumnasParticipantes() {
        ctvNombreParticipantes.setCellValueFactory(cellData -> {
            String nombre = cellData.getValue();
            return new SimpleStringProperty(nombre);
        });
    }

    private void formatearColumnasLocaciones() {
        ctvCapacidadMax.setCellValueFactory(new PropertyValueFactory<>("numeroAsientos"));
        ctvPrecioEntrada.setCellValueFactory(new PropertyValueFactory<>("precio"));
        ctvTipoLocalidad.setCellValueFactory(new PropertyValueFactory<>("categoriaLocalizacion"));
    }

    @FXML
    void addEvento(ActionEvent event) {
        Evento evento= crearEvento();
        if(modelFactoryController.addEvento(evento)){
           modelFactoryController.mostrarAlerta("evento agregado correctamente", Alert.AlertType.INFORMATION);
           observer.notificar();
           modelFactoryController.cerrarVentana(textFPrecio);
        }else {
            modelFactoryController.mostrarAlerta("el evento no se pudo agregar", Alert.AlertType.ERROR);
        }
    }

    private Evento crearEvento() {
        ArrayList<Locacion>locacions= new ArrayList<>();
        locacions.addAll(tvLocalidades.getItems());
        ArrayList<String> participantes=new ArrayList<>();
        participantes.addAll(tvNombresParticipantes.getItems());
        return new Evento(textFNombreEvento.getText(),textFDireccionEvento.getText(), modelFactoryController.generarIdEvento(),dpFechaEvento.getValue(),participantes,locacions,stringToSystemTime(cbHorasDIa.getValue()));
    }

    @FXML
    void addParticipante(ActionEvent event) {
        if(textFCapacidadMaxPlata.getText()!=null&&!textFnombreParticipante.getText().isBlank()){
            tvNombresParticipantes.getItems().add(textFnombreParticipante.getText());
            textFnombreParticipante.setText("");
        }
    }

    @FXML
    void agregarLocalidad(ActionEvent event) {
      ArrayList<Locacion>locaciones=  crearLocalidades();
      tvLocalidades.getItems().addAll(locaciones);
    }

    private ArrayList<Locacion> crearLocalidades() {
        tvLocalidades.getItems().clear();
        ArrayList<Locacion> localidads=new ArrayList<>();
        if(!textFCapacidadCobre.getText().isBlank()){
            Locacion localidadGeneral= new Locacion(Double.parseDouble(textFPrecio.getText()),Integer.parseInt(textFCapacidadCobre.getText()),CategoriaLocalizacion.COBRE);
            localidads.add(localidadGeneral);
            if(!textFCapacidadOro.getText().isBlank()){
                Locacion localidadVip= new Locacion(Double.parseDouble(textFPrecio.getText())+(Double.parseDouble(textFPrecio.getText())*0.2),Integer.parseInt(textFCapacidadOro.getText()),CategoriaLocalizacion.ORO);
                localidads.add(localidadVip);
            }
            if(!textFCapacidadMaxPlata.getText().isBlank()){
                Locacion localidadPlatino= new Locacion(Double.parseDouble(textFPrecio.getText())+(Double.parseDouble(textFPrecio.getText())*0.25),Integer.parseInt(textFCapacidadMaxPlata.getText()),CategoriaLocalizacion.PLATA);
                localidads.add(localidadPlatino);
            }
            return localidads;
        }else {
           // mostrarMensaje("Localidad Alert","No es posible crear","debe agregar almenos los puestos general", Alert.AlertType.INFORMATION);
            return localidads;
        }
    }


    @FXML
    void cancelar(ActionEvent event) {
        modelFactoryController.cerrarVentana(textFCapacidadCobre);
    }

    @FXML
    public void deleteParticipante(ActionEvent actionEvent) {
        String elemento= tvNombresParticipantes.getSelectionModel().getSelectedItem();
        if(elemento!=null){
            tvNombresParticipantes.getItems().remove(elemento);
        }
    }

    Observer observer;
    public void inicializar(Observer panelAdministrador) {
        this.observer=panelAdministrador;
    }
}
