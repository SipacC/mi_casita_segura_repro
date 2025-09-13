package Modelo;

import java.sql.Timestamp;

public class Pago {
    private int id_pago;
    private int id_usuario;
    private int id_tipo;
    private String metodo_pago;
    private Timestamp fecha_pago;
    private double monto;
    private double mora;
    private String observaciones;
    private String estado;

    public Pago() {}

    public Pago(int id_pago, int id_usuario, int id_tipo, String metodo_pago, Timestamp fecha_pago, double monto, double mora, String observaciones, String estado) {
        this.id_pago = id_pago;
        this.id_usuario = id_usuario;
        this.id_tipo = id_tipo; 
        this.metodo_pago = metodo_pago;
        this.fecha_pago = fecha_pago;
        this.monto = monto;
        this.mora = mora;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public int getId_pago() { return id_pago; }
    public void setId_pago(int id_pago) { this.id_pago = id_pago; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public int getId_tipo() { return id_tipo; } 
    public void setId_tipo(int id_tipo) { this.id_tipo = id_tipo; }

    public String getMetodo_pago() { return metodo_pago; }
    public void setMetodo_pago(String metodo_pago) { this.metodo_pago = metodo_pago; }

    public Timestamp getFecha_pago() { return fecha_pago; }
    public void setFecha_pago(Timestamp fecha_pago) { this.fecha_pago = fecha_pago; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public double getMora() { return mora; }
    public void setMora(double mora) { this.mora = mora; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
