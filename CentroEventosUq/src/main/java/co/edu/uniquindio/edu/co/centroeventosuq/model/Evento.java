package co.edu.uniquindio.edu.co.centroeventosuq.model;

import co.edu.uniquindio.edu.co.centroeventosuq.hilos.Cola;
import co.edu.uniquindio.edu.co.centroeventosuq.hilos.Taqui;
import co.edu.uniquindio.edu.co.centroeventosuq.sokets.ServerTaquillas;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

@Getter
@Setter
@NoArgsConstructor
public class Evento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private boolean taquillaAbierta=false;

    public boolean isTaquillaAbierta() {
        return taquillaAbierta;
    }

    public void setTaquillaAbierta(boolean taquillaAbierta) {
        this.taquillaAbierta = taquillaAbierta;
    }

    private Semaphore semaphore;
    private Cola cola;
    public Evento(String nombre, String lugar, String idEvento, LocalDate fecha, ArrayList<String> nombreArtistas, ArrayList<Locacion> localizaciones, LocalTime horaInicioEvento) {
        this.nombre = nombre;
        this.lugar = lugar;
        this.idEvento = idEvento;
        this.fecha = String.valueOf(fecha);
        this.nombreArtistas = nombreArtistas;
        this.localizaciones = localizaciones;
        this.horaInicioEvento = String.valueOf(horaInicioEvento);
        this.semaphore= new Semaphore(1,true);
        System.out.println("el semaforo ya no es nulo");
    }
    private String nombre, lugar,idEvento;
   private String fecha;
   private Taqui taqui;


    private ArrayList<String> nombreArtistas= new ArrayList<>();
    private ArrayList<Locacion> localizaciones = new ArrayList<>();
    private String horaInicioEvento;

   private ArrayList<Boleta> boletasVendidad= new ArrayList<>();

    public int numeroVoletasTotales(){
        int voletasTotales= 0;
        for (Locacion locacion: localizaciones){
            voletasTotales+=locacion.getNumeroAsientos()- locacion.getNumeroVoletasVendidas();
        }
        return voletasTotales;
    }

    public void ingresarTaquilla(int hashCode, ServerTaquillas serverTaquillas, CountDownLatch latch) {
        if(this.semaphore==null){
            semaphore= new Semaphore(3,true);
        }
        if(this.cola!=null){
            this.cola= new Cola();
        }
        String idSoket=String.valueOf(hashCode);
        System.out.println("va a ingresar");
        if(taqui==null){
            taqui=new Taqui(this.semaphore,idSoket,this.idEvento,serverTaquillas,this.fecha,this.horaInicioEvento,this.cola,latch);
        }
        taqui= new Taqui(this.semaphore,idSoket,this.idEvento,serverTaquillas,this.fecha,this.horaInicioEvento,this.cola,latch);
        Thread thread= new Thread(taqui);
        thread.start();

    }


    public int getPoscionColaCompras() {
        if(taqui!=null){
            System.out.println("hay "+taqui.getPersonasEnCola()+"conectadas");
            return taqui.getPersonasEnCola();
        }else {
            System.out.println("taqui no existe");
            return 0;
        }
    }
}
