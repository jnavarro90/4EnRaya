/**
 * IUGrafica.java
 * @date 22-mayo-2015
 * @author Javi Navarro
 *
 * @version 2 
 * Una clase principal para la vista donde los controladores que
 * quieran acceder a las diferentes vistas llamaran y esta lo 
 * gestionará todo
 */
package vista;

import java.util.HashMap;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import modelo.Casilla;
import modelo.Jugador;
import utilidades.RecursosAppSwing;

public class IUGrafica extends JFrame {

    private MenuVista menuJugador;
    private TableroSwing tablero;

    public IUGrafica() {
        RecursosAppSwing recSwingApp = RecursosAppSwing.instancia(
                "es.xml");

        menuJugador = new MenuVista("Menu Inicial");
        tablero = new TableroSwing(6, 7, recSwingApp);
        tablero.setVisible(false);
    }

    public void seleccionarOponente(HashMap _lista){
        menuJugador.seleccionOponente(_lista);
    }
    public void refrescarLista(HashMap _lista){
        menuJugador.refrescarOponentes(_lista);
    }
    
    public void menuInicial() {
        menuJugador.obtenerOpcionMenuInicial();
    }

    public void menuFinal() {
//        menuJugador = new MenuVista("Menu Final");
        menuJugador.obtenerOpcionMenuFinal();
    }

    public void preguntarNombre() {
        menuJugador.obtenerNombreJugador();
    }

    public void actualizarPanelDerecha(Jugador j1, Jugador j2) {
        tablero.actualizarPanelDerecha(j1, j2);
    }

    public void pintarTablero() {
        tablero.setVisible(true);
    }

    public TableroSwing obtenerTableroSwing() {
        return tablero;
    }

    public void ocultarTablero() {
        tablero.ocultar();
    }

    public void cerrarTablero() {
        tablero.acaba();
    }

    public void vaciarTablero() {
        tablero.vaciar();
    }

    public void repintarTablero(Casilla[][] casillas) {
        tablero.repintar(casillas);
    }

    public void addObservadorMenus(Observer controlador) {
        menuJugador.addObserver(controlador);
    }

    public void addObservadorTablero(Observer controlador) {
        tablero.nuevoObservador(controlador);
    }

    public void mensaje(String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }

    public void mensajeError(String texto) {
        JOptionPane.showMessageDialog(null, texto, "Mensaje de Error", 
                JOptionPane.ERROR_MESSAGE);
    }
}
