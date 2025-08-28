package Modelo;

import java.sql.Timestamp;

public class QrUsuario {
    private int id_qr_usuario;
    private String codigo_qr_usuario;
    private int id_usuario;
    private Timestamp fecha_hora_generada;
    private String tipo;
    private String estado;

    public QrUsuario() {}

    public QrUsuario(int id_qr_usuario, String codigo_qr_usuario, int id_usuario, Timestamp fecha_hora_generada, String tipo, String estado) {
        this.id_qr_usuario = id_qr_usuario;
        this.codigo_qr_usuario = codigo_qr_usuario;
        this.id_usuario = id_usuario;
        this.fecha_hora_generada = fecha_hora_generada;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId_qr_usuario() { return id_qr_usuario; }
    public void setId_qr_usuario(int id_qr_usuario) { this.id_qr_usuario = id_qr_usuario; }

    public String getCodigo_qr_usuario() { return codigo_qr_usuario; }
    public void setCodigo_qr_usuario(String codigo_qr_usuario) { this.codigo_qr_usuario = codigo_qr_usuario; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public Timestamp getFecha_hora_generada() { return fecha_hora_generada; }
    public void setFecha_hora_generada(Timestamp fecha_hora_generada) { this.fecha_hora_generada = fecha_hora_generada; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
