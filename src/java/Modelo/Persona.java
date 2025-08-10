package Modelo;

public class Persona {
    private int id;
    private String dpi;
    private String nom;
    private String rol;
    private String contrasena;


    public Persona() {
    }

    public Persona(String dpi, String nom, String rol, String contrasena) {
        this.dpi = dpi;
        this.nom = nom;
        this.rol = rol;
        this.contrasena = contrasena;
    }

    public int getId() {
         return id; }
    public void setId(int id) {
         this.id = id; }

    public String getDpi() { 
        return dpi; }
    public void setDpi(String dpi) { 
        this.dpi = dpi; }

    public String getNom() { 
        return nom; }
    public void setNom(String nom) { 
        this.nom = nom; }

    public String getRol() { 
        return rol; }
    public void setRol(String rol) { 
        this.rol = rol; }

    public String getContrasena() { 
        return contrasena;}
    public void setContrasena(String contra) { 
        this.contrasena = contra; }
}
