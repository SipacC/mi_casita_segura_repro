package Modelo;

import java.sql.Timestamp;

public class Bitacora {
    private int id_bitacora;
    private int id_usuario;
    private String accion;
    private String modulo;
    private Timestamp fecha_hora;

    // Constructor vacío
    public Bitacora() {}

    // Constructor completo
    public Bitacora(int id_bitacora, int id_usuario, String accion, String modulo, Timestamp fecha_hora) {
        this.id_bitacora = id_bitacora;
        this.id_usuario = id_usuario;
        this.accion = accion;
        this.modulo = modulo;
        this.fecha_hora = fecha_hora;
    }

    // Getters y Setters
    public int getId_bitacora() {
        return id_bitacora;
    }

    public void setId_bitacora(int id_bitacora) {
        this.id_bitacora = id_bitacora;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public Timestamp getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Timestamp fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
}
