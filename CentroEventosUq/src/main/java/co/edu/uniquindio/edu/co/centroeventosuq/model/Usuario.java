package co.edu.uniquindio.edu.co.centroeventosuq.model;

import co.edu.uniquindio.edu.co.centroeventosuq.model.enums.TipoUsuario;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private TipoUsuario tipoUsuario;
    private String nombre,direccion,correo,contrasena;
    private ArrayList<Compra>listaDeCompras;
    public Usuario(String nombre, String email, String contrasena, TipoUsuario tipoUsuario) {
        this.tipoUsuario=tipoUsuario;
        this.nombre = nombre;
        this.correo = email;
        this.contrasena = contrasena;
        this.listaDeCompras= new ArrayList<>();
    }


}
