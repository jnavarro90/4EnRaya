/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg4enrayaservidor;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Javi
 */
public class HiloConexion extends Thread {
    private ServerSocket ss;
    private ConectorSv conector;
    public HiloConexion(ConectorSv _conector, ServerSocket _ss) {
        this.conector = _conector;
        this.ss = _ss;
    }
    
   public void run(){
        while(!ss.isClosed()){
            try{
                conector.mensaje("Esperando conexion...");
                conector.esperarConexion();
                conector.procesarConexion();
                
            }catch ( Exception e ) {
                System.err.println( "El servidor terminó la conexión" );
                e.printStackTrace();
            }
        }
   }
    
}
