package co.edu.uniquindio.edu.co.centroeventosuq.model.Service;

import co.edu.uniquindio.edu.co.centroeventosuq.model.Evento;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Locacion;
import co.edu.uniquindio.edu.co.centroeventosuq.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface CentroEventosService {
    boolean registrarLocaciones(Locacion locacion);
    boolean addEvento(Evento evento);
    boolean deleteEvento(String idEvento);
    int registrarUsuario(Usuario usuario);
    int loguear(String correo,String contrasena);

    List<Evento> filtrarEvetos(LocalDate fecha);
}