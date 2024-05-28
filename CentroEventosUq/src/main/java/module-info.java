module co.edu.uniquindio.edu.co.centroeventosuq{
    requires jakarta.mail;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires lombok;
    requires com.google.zxing;
    requires java.xml.bind;


    opens co.edu.uniquindio.edu.co.centroeventosuq.controller to javafx.fxml;
    exports co.edu.uniquindio.edu.co.centroeventosuq.controller;
    opens co.edu.uniquindio.edu.co.centroeventosuq to javafx.fxml;
    exports co.edu.uniquindio.edu.co.centroeventosuq;
    exports co.edu.uniquindio.edu.co.centroeventosuq.model;
    exports co.edu.uniquindio.edu.co.centroeventosuq.model.enums;
    exports co.edu.uniquindio.edu.co.centroeventosuq.model.Service;
    exports co.edu.uniquindio.edu.co.centroeventosuq.utils;
    exports co.edu.uniquindio.edu.co.centroeventosuq.sokets;
    exports co.edu.uniquindio.edu.co.centroeventosuq.controller.service;
    exports co.edu.uniquindio.edu.co.centroeventosuq.hilos;
    opens co.edu.uniquindio.edu.co.centroeventosuq.controller.service to javafx.fxml;


}