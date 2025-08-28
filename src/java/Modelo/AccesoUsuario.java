package Modelo;

import java.sql.Timestamp;

public class AccesoUsuario {
    private int id_acceso;
    private int id_qr_usuario;
    private Timestamp fecha_hora;
    private String tipo;
    private String resultado;
    private String estado;

    public AccesoUsuario() {}

    public AccesoUsuario(int id_acceso, int id_qr_usuario, Timestamp fecha_hora, String tipo, String resultado, String estado) {
        this.id_acceso = id_acceso;
        this.id_qr_usuario = id_qr_usuario;
        this.fecha_hora = fecha_hora;
        this.tipo = tipo;
        this.resultado = resultado;
        this.estado = estado;
    }

    public int getId_acceso() { return id_acceso; }
    public void setId_acceso(int id_acceso) { this.id_acceso = id_acceso; }

    public int getId_qr_usuario() { return id_qr_usuario; }
    public void setId_qr_usuario(int id_qr_usuario) { this.id_qr_usuario = id_qr_usuario; }

    public Timestamp getFecha_hora() { return fecha_hora; }
    public void setFecha_hora(Timestamp fecha_hora) { this.fecha_hora = fecha_hora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
