package co.edu.uniquindio.edu.co.centroeventosuq.model;

import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.CategoriaBoleta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.net.ServerSocket;


@Getter
@Setter
@NoArgsConstructor
public class Boleta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String idBoleta;
    private Evento evento;
    private String categoria;
    private String nombreComprador;
    private String idComprador;

    public Boleta(String idBoleta, Evento evento, String categoria, String nombreComprador, String idComprador) {
        this.idBoleta = idBoleta;
        this.evento = evento;
        this.categoria = categoria;
        this.nombreComprador = nombreComprador;
        this.idComprador = idComprador;
    }

    public String getPrecio() {
        return String.valueOf(0);
    }
}