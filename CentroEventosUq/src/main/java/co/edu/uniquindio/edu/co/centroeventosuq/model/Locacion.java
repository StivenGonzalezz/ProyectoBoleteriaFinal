package co.edu.uniquindio.edu.co.centroeventosuq.model;

import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.CategoriaLocalizacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Locacion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private double precio;
    private int numeroAsientos;
    private CategoriaLocalizacion categoriaLocalizacion;
    private int numeroVoletasVendidas;

    public Locacion(double precio, int numeroAsientos, CategoriaLocalizacion categoriaLocalizacion) {
        this.precio = precio;
        this.numeroAsientos = numeroAsientos;
        this.categoriaLocalizacion = categoriaLocalizacion;
        this.numeroVoletasVendidas=0;
    }











}
