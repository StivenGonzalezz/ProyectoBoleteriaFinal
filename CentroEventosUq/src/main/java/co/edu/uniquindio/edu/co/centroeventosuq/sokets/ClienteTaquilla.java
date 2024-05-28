package co.edu.uniquindio.edu.co.centroeventosuq.sokets;

import co.edu.uniquindio.edu.co.centroeventosuq.controller.ModelFactoryController;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Usuario;
import co.edu.uniquindio.edu.co.centroeventosuq.utils.Persistencia;

import java.io.*;
import java.net.Socket;

public class ClienteTaquilla implements Runnable {
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    private Usuario usuarioAsociado;

    public Usuario getUsuarioAsociado() {
        return usuarioAsociado;
    }

    public void setUsuarioAsociado(Usuario usuarioAsociado) {
        this.usuarioAsociado = usuarioAsociado;
    }

    public void conectar(String servidorHost, int puertoServidor) {
        try {
            socket = new Socket(servidorHost, puertoServidor);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            Thread treat= new Thread(this);
            treat.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarSolicitudCompra(String idEvento) {
        try {
            salida.writeUTF("comprar:" + idEvento);
            salida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalizarCompra(String idEvento) {
        try {
            salida.writeUTF("liberar:" + idEvento);
            salida.flush();
            String respuesta = entrada.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
    }

    public void cerrarConexion() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
            System.out.println("Se desconectó del servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int pedirPosiconCola(String idEvento) {
//        conectar("localHost",8081);
        try {
            System.out.println("ahora no se fue directamente al cath");
            salida.writeObject("Posicion:"+idEvento);
            System.out.println("si envia el objeto");
            salida.flush();
            System.out.println("si se envia");
            String respuesta= entrada.readUTF();
            System.out.println(respuesta);
            if(respuesta=="Comprar"){
                return 0;
            }else {
                return Integer.parseInt(respuesta);
            }
        }catch (IOException e){
            System.out.println("es por aca que llega?");
            return 0;
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Object object=  entrada.readObject();
                System.out.println("si esta esperando algo");
                if(object instanceof String){
                    String respuesta= (String) object;
                    System.out.println(respuesta);
                    if(respuesta.startsWith("Agregado:")){
                        System.out.println("si agrega");
                        int poscion=Integer.parseInt(respuesta.substring(9));
                        System.out.println(poscion);
                        ModelFactoryController.getInstance().setPosicionCola(poscion);
                    } else if(respuesta.equals("Comprando")){
                        System.out.println("si los pone a comprar");
                        System.out.println("esta en la cola");
                        ModelFactoryController.getInstance().setPosicionCola(0);
                    }
                    else if (respuesta.equals("espacio liberado")) {
                        System.out.println("Espacio liberado en la taquilla del evento");
                    } else {
                        System.out.println("Error al liberar espacio: " + respuesta);
                    }
                }else  if(object instanceof File){
                    File eventos= (File) object;
                    if(eventos!=null){
                        Persistencia.sobrescribirXML(eventos);
                    }


                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}