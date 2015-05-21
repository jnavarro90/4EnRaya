/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import vista.IUGrafica;
import vista.Observador;

/**
 *
 * @author Javi
 */

public class ControlMenus implements Observador{
    private IUGrafica vista;
    private Partida partida;

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

    @Override
    public void actualiza(Object obj) {
        if (obj instanceof String) { 
            this.partida.cambiarOpcionMenu((String)obj); 
        }else if(obj instanceof Boolean){
            this.partida.seguirJugando((Boolean)obj);
        }   
    }
}
