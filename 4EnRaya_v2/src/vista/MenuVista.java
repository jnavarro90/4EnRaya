/**
 * MenuVista.java 
 * @date 22-mayo-2015
 * @author Javi Navarro
 * 
 * @version 2 
 * Una vista para los diferentes menus de la aplicación será
 * Observable por ControlMenus.java en el paquete del controlador
 */
package vista;

import java.util.Observable;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuVista extends Observable {

    public final String CARGAR_PARTIDA = "Cargar Partida";
    public final String GUARDAR_PARTIDA = "Guardar Partida";
    public final String NUEVA_PARTIDA = "Nueva Partida";
    public final String SALIR = "Salir";
    private final String NOMBRE_VACIO = "Nombre vacio";
    private String titulo;
    private JPanel MVista;

    public MenuVista(String titulo) {
        MVista = new JPanel();
        this.titulo = titulo;
    }

    public void obtenerNombreJugador() {
        String nombre = JOptionPane.showInputDialog(
                MVista, "Introduce el nombre del jugador", titulo,
                JOptionPane.QUESTION_MESSAGE);
        if (nombre.isEmpty()) {
            setChanged();
            notifyObservers(NOMBRE_VACIO);
        } else {
            setChanged();
            notifyObservers(nombre);
        }
    }

    public void obtenerOpcionMenuInicial() {
        String[] options = {NUEVA_PARTIDA, CARGAR_PARTIDA, SALIR};
        int opcion = 0;

        opcion = JOptionPane.showOptionDialog(MVista, "  ¿Qué desea hacer?", "4 En Raya", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        if (opcion == 0) {
            setChanged();
            notifyObservers(NUEVA_PARTIDA);
        } else if (opcion == 1) {
            setChanged();
            notifyObservers(CARGAR_PARTIDA);
        } else if (opcion == 2) {
            setChanged();
            notifyObservers(SALIR);
        }
    }

    public void obtenerOpcionMenuFinal() {

        int opcion = 0;

        opcion = JOptionPane.showOptionDialog(MVista, "  ¿Volver a jugar?", "4 En Raya", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (opcion == JOptionPane.YES_OPTION) {
            setChanged();
            notifyObservers(true);
        } else {
            setChanged();
            notifyObservers(false);
        }
    }
}
