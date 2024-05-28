package co.edu.uniquindio.edu.co.centroeventosuq.sokets;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.ModelFactoryController;
import co.edu.uniquindio.edu.co.centroeventosuq.hilos.XMLFileMonitor;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import co.edu.uniquindio.edu.co.centroeventosuq.utils.Persistencia;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ServerTaquillas {

    private final ConcurrentHashMap<String, Socket> listaSokets;
    private ArrayList<Evento> eventos = new ArrayList<>();

    public ServerTaquillas() throws IOException {
        listaSokets = new ConcurrentHashMap<>();
        obtenerEventos();
        XMLFileMonitor xmlFileMonitor = new XMLFileMonitor("src/main/resources/Persistencia/Evento.xml", this);
        xmlFileMonitor.monitor();
    }

    public void obtenerEventos() throws IOException {
        eventos = Persistencia.cargarEventosXML();
        System.out.println(eventos.size());
    }

    public void start(int puerto) {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor de taquillas iniciado en el puerto " + puerto + "...");
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket);
                listaSokets.put(String.valueOf(clienteSocket.hashCode()), clienteSocket);
                new Thread(new ClientHandler(clienteSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket clienteSocket;

        public ClientHandler(Socket socket) {
            this.clienteSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectOutputStream flujoSalida = new ObjectOutputStream(clienteSocket.getOutputStream());
                 ObjectInputStream flujoEntrada = new ObjectInputStream(clienteSocket.getInputStream())) {

                while (true) {
                    String comando;
                    try {
                        comando = flujoEntrada.readUTF();
                        System.out.println("Comando recibido: " + comando);
                    } catch (EOFException e) {
                        System.out.println("me solto exection");
                        System.out.println("Cliente desconectado: " + clienteSocket);
                        listaSokets.remove(String.valueOf(clienteSocket.hashCode()));
                        break;
                    }
                    if(comando!=null){
                        procesarComando(comando, flujoSalida);
                    }

                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void procesarComando(String comando, ObjectOutputStream flujoSalida) throws IOException, InterruptedException {
            if (comando.startsWith("comprar:")) {
                manejarCompra(comando, flujoSalida);
            } else if (comando.startsWith("liberar:")) {
                manejarLiberacion(comando, flujoSalida);
            } else if (comando.startsWith("Posicion:")) {
                manejarPosicion(comando, flujoSalida);
            } else if (comando.equals("salir")) {
                listaSokets.remove(String.valueOf(clienteSocket.hashCode()));
            } else {
                flujoSalida.writeUTF("Comando no reconocido");
                flujoSalida.flush();
            }
        }

        private void manejarCompra(String comando, ObjectOutputStream flujoSalida) throws IOException, InterruptedException {
            String idEvento = comando.substring(8);
            Evento evento = ModelFactoryController.getInstance().obtenerEvento(idEvento);
            if (evento != null) {
                CountDownLatch latch = new CountDownLatch(1);
                evento.ingresarTaquilla(clienteSocket.hashCode(), ServerTaquillas.this, latch);
                latch.await();
                if (evento.getTaqui().getPersonasEnCola() <= 1) {
                    System.out.println(evento.getTaqui().getPersonasEnCola());
                    flujoSalida.writeObject("Comprando");
                } else {
                    int posicionActual = evento.getTaqui().obtenerPocionActual(String.valueOf(clienteSocket.hashCode()));
                    flujoSalida.writeUTF("Agregado:" + posicionActual);
                }
                flujoSalida.flush();
            } else {
                flujoSalida.writeUTF("Evento no encontrado: " + idEvento);
                flujoSalida.flush();
            }
        }

        private void manejarLiberacion(String comando, ObjectOutputStream flujoSalida) throws IOException {
            String idEvento = comando.substring(8);
            Evento evento = ModelFactoryController.INSTANCE.obtenerEvento(idEvento);
            if (evento != null) {
                evento.getTaqui().liberar();
                flujoSalida.writeUTF("espacio liberado");
            } else {
                flujoSalida.writeUTF("Evento no encontrado: " + idEvento);
            }
            flujoSalida.flush();
        }

        private void manejarPosicion(String comando, ObjectOutputStream flujoSalida) throws IOException {
            String idEvento = comando.substring(9);
            Evento evento = ModelFactoryController.INSTANCE.obtenerEvento(idEvento);
            int resultado = evento.getPoscionColaCompras();
            if (resultado <= 3) {
                flujoSalida.writeUTF("Comprar");
            } else {
                flujoSalida.writeUTF(String.valueOf(resultado));
            }
            flujoSalida.flush();
        }
    }

    public void enviarPersonasEspera(int hashCode, int numeroEspera) {
        Socket clienteSocket = listaSokets.get(String.valueOf(hashCode));
        try (ObjectOutputStream salida = new ObjectOutputStream(clienteSocket.getOutputStream())) {
            String comando = "personasEspera:" + numeroEspera;
            salida.writeObject(comando);
            salida.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerTaquillas serverTaquillas = new ServerTaquillas();
        serverTaquillas.start(8081);
    }
}