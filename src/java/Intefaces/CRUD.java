
package Intefaces;

import Modelo.Persona;
import java.util.List;


public interface CRUD {
    public List<Persona> listar();
    public Persona list(int id);
    //public boolean add(Persona per);
    public int add(Persona per);
    //public boolean generarQRUsuario(int idUsuario, String tipo);
    public boolean edit(Persona per);
    public boolean eliminar(int id);
    public Persona findByLogin(String nom, String contrasena);
    public boolean existeUsuarioOCorreo(String usuario, String correo);
}
