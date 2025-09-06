package Modelo;

import java.sql.Date;

public class Persona {
    private int id_usuario;
    private String dpi;
    private String nombres;
    private String usuario;
    private String correo;
    private String contrasena;
    private String rol;
    private String lote;
    private String numero_casa;
    private String estado; 
    private Date fecha_creacion; // ✅ nuevo campo

    public Persona() {
    }
    
    public Persona(int id_usuario, String dpi, String nombres, String usuario, String correo, String contrasena, String rol, String lote, String numero_casa, String estado, Date fecha_creacion) {
        this.id_usuario = id_usuario;
        this.dpi = dpi;
        this.nombres = nombres;
        this.usuario = usuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.lote = lote;
        this.numero_casa = numero_casa;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion; // ✅
    }

    // getters y setters

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public String getNumero_casa() { return numero_casa; }
    public void setNumero_casa(String numero_casa) { this.numero_casa = numero_casa; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFecha_creacion() { return fecha_creacion; } // ✅ getter
    public void setFecha_creacion(Date fecha_creacion) { this.fecha_creacion = fecha_creacion; } // ✅ setter
}
