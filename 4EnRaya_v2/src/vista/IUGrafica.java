/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import modelo.Casilla;
import modelo.Jugador;
import utilidades.RecursosAppSwing;

/**
 *
 * @author javi
 */
public class IUGrafica extends JFrame{
  
    private MenuVista menuJugador;
    private TableroSwing tablero;
    public final String CARGAR_PARTIDA = "Cargar Partida";
    public final String GUARDAR_PARTIDA = "Guardar Partida";
    public final String VOLVER = "Volver";
    public final String ERROR = "Error";
    public final String NUEVA_PARTIDA = "Nueva Partida";
    public final String SALIR = "Salir";
    public final String ACERCA_DE = "Acerca de ..";
    public IUGrafica(){
        menuJugador = new MenuVista("Menu Inicial");
        RecursosAppSwing recSwingApp = RecursosAppSwing.instancia("es.xml");
        tablero = new TableroSwing(6, 7, recSwingApp);
        tablero.setVisible(false);
    }

    public void menuInicial(){
        menuJugador.obtenerOpcionMenuInicial();
    }
    
    public void menuFinal(){
//        menuJugador = new MenuVista("Menu Final");
        menuJugador.obtenerOpcionMenuFinal();
    }
    public void preguntarNombre(){
        menuJugador.obtenerNombreJugador();
    }
    
    public void setNombresJugadores(Jugador j1, Jugador j2){
        tablero.setJugadores(j1, j2);
    }
    public void pintarTablero(){
        tablero.setVisible(true);
    }
    public TableroSwing obtenerTableroSwing(){
        return tablero;
    }
    public void ocultarTablero(){
        tablero.ocultar();
    }
    public void cerrarTablero(){
        tablero.acaba();
    }
    public void vaciarTablero(){
        tablero.vaciar();
    }
    
    public void repintarTablero(Casilla[][]casillas){
        tablero.repintar(casillas);
    }
    
    public void addObservadorMenus(Observer controlador){
           menuJugador.addObserver(controlador);
    }
    
    public void addObservadorTablero(Observer controlador){
        tablero.nuevoObservador(controlador);
    }
    
    public void mensaje(String texto) {
        JOptionPane.showMessageDialog(null, texto);
    }

    public void mensajeError(String texto) {
        JOptionPane.showMessageDialog(null, texto, "Mensaje de Error", JOptionPane.ERROR_MESSAGE);
    }
}
