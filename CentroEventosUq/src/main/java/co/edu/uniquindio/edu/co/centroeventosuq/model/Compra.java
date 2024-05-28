package co.edu.uniquindio.edu.co.centroeventosuq.model;

import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.MetodosPago;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Compra  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    //usuario, el evento, la
    //cantidad de boletas adquiridas, el método de pago utilizado, entre otros
    private Usuario usuario;
    private Evento evento;
    private ArrayList<Boleta> boletas;
    private MetodosPago metodoPago;
    private String codigoCompra;

    public Compra(Usuario usuario, Evento evento, ArrayList<Boleta> boletas,MetodosPago metodoPago,String codigoCompra) {
        this.usuario = usuario;
        this.evento = evento;
        this.boletas = boletas;
        this.metodoPago=metodoPago;
        this.codigoCompra=codigoCompra;
    }

    public double calcularPrecio() {
        double precio=0;
        for (Boleta boleta:boletas){
            precio+=boleta.getEvento().getLocalizaciones().stream().filter(c->c.getCategoriaLocalizacion().toString().equals(boleta.getCategoria())).findFirst().get().getPrecio();
        }
        return precio;
    }
}
