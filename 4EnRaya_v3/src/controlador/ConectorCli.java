
package controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javi
 */
public class ConectorCli {
    private Socket s;
    private final int PUERTO = 9000;
    private final String IP = "192.168.1.100";
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    public ConectorCli(){
        try {
            iniciarConexion();
            obtenerFlujos();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println(" envía saludo");
        try {
            salida.writeObject("hola");
            String respuesta="";
            respuesta = (String)entrada.readObject();
            System.out.println(" Servidor devuelve saludo: " + respuesta);
        } catch (IOException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void iniciarConexion() throws IOException{
        s = new Socket(IP, PUERTO);
    }
    
    private void obtenerFlujos() throws IOException{
        
        //Creacion de salida de datos
        this.salida = new ObjectOutputStream(s.getOutputStream());
        this.salida.flush();
        
        //Creacion de entrada de datos
        this.entrada = new ObjectInputStream(s.getInputStream());
    }
    
    public void desconnectar() {
        try {
            s.close();
                        entrada.close();
            salida.close();
        } catch (IOException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
