
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

    private final String MENSAJE_INICIO = "Mensaje inicio";
    private final String INICIAR_SERVIDOR = "Iniciar servidor";
    private final String APAGAR_SERVIDOR = "Apagar servidor";
    private final String CONECTADO = "Conectado";
    private final String SALIR = "Salir";
    
    private Socket s;
    private ServerSocket ss;
    private final int puerto = 9000;
    private int idSesion;
    
    public ConectorSv() throws IOException{
        this.ss = new ServerSocket(puerto);
    }
    
    public void iniciar() throws IOException{
        idSesion = 0;
        while(true){
            try{
                esperarConexion();
                procesarConexion();
                
            }catch ( Exception e ) {
                System.err.println( "El servidor terminó la conexión" );
            }
        }
    }
    private void esperarConexion() throws IOException {
        
        this.s = ss.accept();
        mensaje("Conexión con: "+s.getInetAddress().getHostName());
    }
    
    private void desconectar(){
        try {
            s.close();
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(ConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void procesarConexion(){
        ((HiloConectorSv) new HiloConectorSv(s, idSesion)).start();
        idSesion++;
    }
    
    public void mensaje(String _mensaje){
        System.out.println(_mensaje);
        setChanged();
        notifyObservers(_mensaje);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            String opcion = (String)arg;
            if (opcion.equals(MENSAJE_INICIO)) {
                mensaje("Esperando conexion...");
                mensaje(CONECTADO);
            }else if (opcion.equals(INICIAR_SERVIDOR)){
                try {
                    iniciar();
                } catch (IOException ex) {
                    Logger.getLogger(ConectorSv.class.getName()).log(Level.SEVERE, null, ex);
                }
        } else if (opcion.equals(APAGAR_SERVIDOR)) {
            desconectar();
        }else if (opcion.equals(SALIR)) {
            System.exit(0);
        }
        }
    }
}
