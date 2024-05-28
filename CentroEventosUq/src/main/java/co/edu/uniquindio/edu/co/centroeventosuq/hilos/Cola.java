package co.edu.uniquindio.edu.co.centroeventosuq.hilos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

@Getter
public class Cola {
    private final ArrayList<String> idSokets;

    private   boolean nuevoElemento;

    public Cola() {
        idSokets = new ArrayList<>();
        nuevoElemento=false;
    }

    public synchronized void addCola(String idSoket) {
        System.out.println("va agregar a la cola"+idSoket);
        idSokets.add(idSoket);
        nuevoElemento=true;
        System.out.println("en estos momentos hay"+idSokets.size()+"en el metodo cola");
    }

    public synchronized void removeCola(String idSoket) {
        Iterator<String> iterator = idSokets.iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            if (id.equals(idSoket)) {
                iterator.remove();
                break;  // Asumimos que cada idSoket es único, entonces podemos salir del bucle
            }
        }
        System.out.println("se eliminó la id " + idSoket);
    }

    // Método para verificar si se ha agregado un nuevo elemento
    public synchronized boolean hayNuevosElementos() {

        return nuevoElemento;
    }

    // Método para resetear el estado de nuevo elemento
    public synchronized void resetearNuevoElemento() {
        nuevoElemento = false;
    }


    public synchronized int obtenerPersonasEnCola() {
        int contador=idSokets.size();
        return contador;
    }
}
