package co.edu.uniquindio.edu.co.centroeventosuq.model.enums;

public enum CategoriaBoleta {
    COBRE(0), PLATA(0), ORO(0);

    private double precio;
    CategoriaBoleta(double precio) {
        this.precio=precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
