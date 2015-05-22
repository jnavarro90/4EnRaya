/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.Observable;
import java.util.Observer;
import modelo.Jugador;
import vista.IUGrafica;

/**
 *
 * @author Javi
 */

public class ControlMenus implements Observer{
    public final String OPCION_NOMBRE = "opcion nombre";
    private IUGrafica vista;
    private Partida partida;
    private String opcion = "";

    public ControlMenus() {
        
    }
    
    public void actualizarVista(IUGrafica vista){
        this.vista = vista;
    }
    public void actualizarPartida(Partida partida){
        this.partida = partida;
    }
    public void menuInicial(){
        this.vista.menuInicial();
    }
    
    public void menuFinal(){
        this.vista.menuFinal();
    }
    
    public void preguntarNombre(String opcion){
        this.opcion = opcion;
        vista.preguntarNombre();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean) { 
            this.partida.seguirJugando((Boolean)arg);
        }else if(arg instanceof String && opcion.equals(OPCION_NOMBRE)){
            this.partida.asignarNombreJugador((String)arg);
            opcion = "";
        }else{
            this.partida.cambiarOpcionMenu((String)arg); 
        } 
    }
}
