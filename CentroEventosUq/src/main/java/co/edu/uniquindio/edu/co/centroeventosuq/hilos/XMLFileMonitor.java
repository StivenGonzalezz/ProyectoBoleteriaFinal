package co.edu.uniquindio.edu.co.centroeventosuq.hilos;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.ModelFactoryController;
import co.edu.uniquindio.edu.co.centroeventosuq.sokets.ServerTaquillas;

import java.io.*;
import java.nio.file.*;

public class XMLFileMonitor {
    private Path filePath;
    private long lastModifiedTime;

    ServerTaquillas serverTaquillas;

    public XMLFileMonitor(String filePath,ServerTaquillas serverTaquillas) {
        this.filePath = Paths.get(filePath);
        this.lastModifiedTime =this.filePath.toFile().lastModified();
        this.serverTaquillas=serverTaquillas;
    }

    public XMLFileMonitor(String filePath){
        this.filePath = Paths.get(filePath);
        this.lastModifiedTime =this.filePath.toFile().lastModified();
    }

    public void monitor() {
        new Thread(() -> {
            while (true) {
                try {
                    long currentModifiedTime = Files.getLastModifiedTime(filePath).toMillis();
                    if (currentModifiedTime > lastModifiedTime) {
                        System.out.println("se modifico");
                        lastModifiedTime = currentModifiedTime;
                        if(serverTaquillas!=null){
                            serverTaquillas.obtenerEventos();
                        }else {
                            ModelFactoryController.getInstance().getCentroEventos().cargarDatos();
                            ModelFactoryController.getInstance().actualizarVentas();
                        }

                    }
                    Thread.sleep(1000); // Verificar cada segundo
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

