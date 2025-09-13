package Gestion_bitacora;

import ModeloDAO.BitacoraDAO;
import Modelo.Persona;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class RegistroBitacora {

    private RegistroBitacora() {}

    /**
     * Registra una acción en la bitácora usando el usuario de la sesión.
     *
     * @param req    HttpServletRequest para obtener la sesión actual
     * @param accion Texto descriptivo de la acción (ej: "Editó al usuario con ID 7")
     * @param modulo Módulo donde ocurrió la acción (ej: "Usuarios", "Login")
     */
    public static void log(HttpServletRequest req, String accion, String modulo) {
        HttpSession ses = (req != null) ? req.getSession(false) : null;
        Persona actor = (ses != null) ? (Persona) ses.getAttribute("usuario") : null;
        int idActor = (actor != null) ? actor.getId_usuario() : 0;
        new BitacoraDAO().registrarAccion(idActor, accion, modulo);
    }
}
