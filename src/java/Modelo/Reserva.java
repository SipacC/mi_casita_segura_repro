package Modelo;

public class Reserva {
    private int id_reserva;
    private int id_usuario;
    private int id_area;
    private String nombreArea;
    private String fecha_reserva;
    private String hora_inicio;
    private String hora_fin;
    private String estado;

    public int getId_reserva() { return id_reserva; }
    public void setId_reserva(int id_reserva) { this.id_reserva = id_reserva; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public int getId_area() { return id_area; }
    public void setId_area(int id_area) { this.id_area = id_area; }

    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }

    public String getFecha_reserva() { return fecha_reserva; }
    public void setFecha_reserva(String fecha_reserva) { this.fecha_reserva = fecha_reserva; }

    public String getHora_inicio() { return hora_inicio; }
    public void setHora_inicio(String hora_inicio) { this.hora_inicio = hora_inicio; }

    public String getHora_fin() { return hora_fin; }
    public void setHora_fin(String hora_fin) { this.hora_fin = hora_fin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
