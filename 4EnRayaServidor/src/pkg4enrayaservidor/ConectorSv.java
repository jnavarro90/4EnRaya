
package pkg4enrayaservidor;

import java.net.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javi
 */
public class ConectorSv extends Observable implements Observer{

    private final String INICIAR_SERVIDOR = "Iniciar servidor";
    private final String APAGAR_SERVIDOR = "Apagar servidor";
    private final String DESCONECTADO = "Desconectado";
    private final String CONECTADO = "Conectado"; 
    private final String SALIR = "Salir";
    
    private Socket s;
    private ServerSocket ss;
    private final int puerto = 9000;
    private int idSesion = 0;
    private HiloConexion conexion;
    private boolean primeraConexion =true;
    
    public ConectorSv() throws IOException{
        this.ss = new ServerSocket(puerto);
        this.conexion = new HiloConexion(this, ss);
    }

    public void esperarConexion() throws IOException {
        this.s = ss.accept();
        mensaje("Conexión con: "+s.getInetAddress().getHostName());
    }
    
    private void desconectarServidor(){
        try {
            ss.close();
            mensaje("Se ha desconectado el servidor");
        } catch (IOException ex) {
            Logger.getLogger(ConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void reconectar(){
        try {
            this.ss = new ServerSocket(puerto);
            this.conexion = new HiloConexion(this, ss);
        } catch (IOException ex) {
            Logger.getLogger(ConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void procesarConexion() throws IOException{
        ((HiloConectorSv) new HiloConectorSv(s, idSesion)).start();
        idSesion++;
    }
    
    public void mensaje(String _mensaje){
        setChanged();
        notifyObservers(_mensaje);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            String opcion = (String)arg;
            if (opcion.equals(INICIAR_SERVIDOR)){
                if(primeraConexion){
                    mensaje(CONECTADO);
                    conexion.start();
                    primeraConexion = false;
                }else{
                    mensaje(CONECTADO);
                    reconectar();
                    conexion.start(); 
                }
            } else if (opcion.equals(APAGAR_SERVIDOR)) {
                mensaje(DESCONECTADO);
                desconectarServidor();
            }else if (opcion.equals(SALIR)) {
                System.exit(0);
            }
        }
    }
}
