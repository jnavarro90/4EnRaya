/**
 * ControlMenus.java 
 * @date 22-mayo-2015
 * @author Javi Navarro
 *
 * @version 2 
 * Nuevo controlador con respecto a la version 1 para controlar de una
 * forma más ordenada los menus que mostrara la vista
 */
package controlador;

import java.util.Observable;
import java.util.Observer;
import vista.IUGrafica;

public class ControlMenus implements Observer {

    public final String OPCION_NOMBRE = "opcion nombre";
    private IUGrafica vista;
    private Partida partida;
    private String opcion = "";

    public ControlMenus() {

    }

    public void actualizarVista(IUGrafica vista) {
        this.vista = vista;
    }

    public void actualizarPartida(Partida partida) {
        this.partida = partida;
    }

    public void menuInicial() {
        this.vista.menuInicial();
    }

    public void menuFinal() {
        this.vista.menuFinal();
    }

    public void preguntarNombre(String opcion) {
        this.opcion = opcion;
        vista.preguntarNombre();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean) {
            this.partida.seguirJugando((Boolean) arg);
        } else if (arg instanceof String && opcion.equals(OPCION_NOMBRE)) {
            this.partida.asignarNombreJugador((String) arg);
            opcion = "";
        } else {
            this.partida.cambiarOpcionMenu((String) arg);
        }
    }
}
