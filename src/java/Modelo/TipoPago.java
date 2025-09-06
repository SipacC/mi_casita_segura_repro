package Modelo;

public class TipoPago {
    private int id_tipo;
    private String nombre;
    private double monto;

    public TipoPago() {}

    public TipoPago(int id_tipo, String nombre, double monto) {
        this.id_tipo = id_tipo;
        this.nombre = nombre;
        this.monto = monto;
    }

    public int getId_tipo() { return id_tipo; }
    public void setId_tipo(int id_tipo) { this.id_tipo = id_tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
