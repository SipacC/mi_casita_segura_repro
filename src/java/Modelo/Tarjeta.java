package Modelo;

import java.sql.Date;

public class Tarjeta {
    private int id_tarjeta;
    private int id_usuario;
    private String numero_tarjeta;
    private Date fecha_vencimiento;
    private String cvv;
    private String nombre_titular;
    private String tipo_tarjeta;
    private double saldo;

    public Tarjeta() {}

    public Tarjeta(int id_tarjeta, int id_usuario, String numero_tarjeta, Date fecha_vencimiento, 
                   String cvv, String nombre_titular, String tipo_tarjeta, double saldo) {
        this.id_tarjeta = id_tarjeta;
        this.id_usuario = id_usuario;
        this.numero_tarjeta = numero_tarjeta;
        this.fecha_vencimiento = fecha_vencimiento;
        this.cvv = cvv;
        this.nombre_titular = nombre_titular;
        this.tipo_tarjeta = tipo_tarjeta;
        this.saldo = saldo;
    }

    public int getId_tarjeta() { return id_tarjeta; }
    public void setId_tarjeta(int id_tarjeta) { this.id_tarjeta = id_tarjeta; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getNumero_tarjeta() { return numero_tarjeta; }
    public void setNumero_tarjeta(String numero_tarjeta) { this.numero_tarjeta = numero_tarjeta; }

    public Date getFecha_vencimiento() { return fecha_vencimiento; }
    public void setFecha_vencimiento(Date fecha_vencimiento) { this.fecha_vencimiento = fecha_vencimiento; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getNombre_titular() { return nombre_titular; }
    public void setNombre_titular(String nombre_titular) { this.nombre_titular = nombre_titular; }

    public String getTipo_tarjeta() { return tipo_tarjeta; }
    public void setTipo_tarjeta(String tipo_tarjeta) { this.tipo_tarjeta = tipo_tarjeta; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
