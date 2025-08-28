package Modelo;

import java.sql.Timestamp;

public class Notificacion {
    private int id_notificaciones;
    private int id_usuario;
    private String asunto;
    private String mensaje;
    private Timestamp fecha_envio;
    private String tipo_evento;

    public Notificacion() {}

    public Notificacion(int id_notificaciones, int id_usuario, String asunto, String mensaje, Timestamp fecha_envio, String tipo_evento) {
        this.id_notificaciones = id_notificaciones;
        this.id_usuario = id_usuario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fecha_envio = fecha_envio;
        this.tipo_evento = tipo_evento;
    }

    public int getId_notificaciones() { return id_notificaciones; }
    public void setId_notificaciones(int id_notificaciones) { this.id_notificaciones = id_notificaciones; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Timestamp getFecha_envio() { return fecha_envio; }
    public void setFecha_envio(Timestamp fecha_envio) { this.fecha_envio = fecha_envio; }

    public String getTipo_evento() { return tipo_evento; }
    public void setTipo_evento(String tipo_evento) { this.tipo_evento = tipo_evento; }
}
