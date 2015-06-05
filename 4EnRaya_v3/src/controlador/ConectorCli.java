
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
    private final int puerto = 9000;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    public ConectorCli(){
        try {
            s = new Socket("127.0.0.1", puerto);
            obtenerFlujos();
        } catch (IOException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(" envía saludo");
        try {
            salida.writeUTF("hola");
            String respuesta="";
            respuesta = entrada.readUTF();
            System.out.println(" Servidor devuelve saludo: " + respuesta);
        } catch (IOException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void obtenerFlujos() throws IOException{
        //Creacion de entrada de datos
        this.entrada = new ObjectInputStream(s.getInputStream());

        //Creacion de salida de datos
        this.salida = new ObjectOutputStream(s.getOutputStream());
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
