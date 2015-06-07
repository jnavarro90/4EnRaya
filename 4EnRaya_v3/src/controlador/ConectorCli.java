
package controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Javi
 */
public class ConectorCli extends Thread{
    private Socket s;
    
    private final String PEDIR_NOMBRE = "pedir nombre";
    private final String COMPROBAR_NOMBRE = "comprobar nombre";
    private final String OK_NOMBRE = "ok nombre";
    private final String PEDIR_LISTA_CONECTADOS = "pedir lista conectados";
    private final String INVITAR_OPONENTE = "invitar oponente";
    private final String INVITACION_JUGAR = "invitacion jugar";
    private final String OK_JUGAR = "ok jugar";
    private final String NO_OK_JUGAR = "no ok jugar";
    private final String ENVIAR_LISTA_CONECTADOS = "enviar lista conectados";
    private final String ENVIAR_NOMBRE_OPONENTE = "enviar nombre opomemte";
    private final String ENVIA_IP_OPONENTE = "enviar ip oponente";
    private final String ACABAR = "acabar";
    private final int PUERTO = 9000;
    private final String IP = "192.168.1.109";
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private String ipOponente = "";
    private String recibido = "";

    public ConectorCli(){
        try {
            iniciarConexion();
            obtenerFlujos();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    private void iniciarConexion() throws IOException{
        s = new Socket(IP, PUERTO);
    }
    
    public int obtenerOpcion() {
        int opcion = 0;
        JFrame Vopcion = new JFrame();
        opcion = JOptionPane.showOptionDialog(Vopcion,
                "¿Qué desea hacer?", "4 En Raya", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
            System.out.println(opcion);
        return opcion;
    }
    
    public void comprobarInvitacionJugar(){
        recibido = (String)recibirMensajeServidor();
            if(recibido.equals(INVITACION_JUGAR)){
                if(obtenerOpcion() == 0){
                    enviarMensajeServidor(OK_JUGAR);
                    ipOponente = (String)recibirMensajeServidor();
                    System.out.println(ipOponente);
                }else{
                    enviarMensajeServidor(NO_OK_JUGAR);
                }
            }
    }

    public String getIpOponente() {
        return ipOponente;
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
    
    public void accionPedirListaConectados(){
        enviarMensajeServidor(PEDIR_LISTA_CONECTADOS);
    }
    
    public HashMap<String, Integer> getListaJugadoresConectados(){
        accionPedirListaConectados();
        return (HashMap)recibirMensajeServidor();
    }
    
    public void accionComprobarNombre(){
        enviarMensajeServidor(COMPROBAR_NOMBRE); 
    }
    public void accionInvitarOponente(){
        enviarMensajeServidor(INVITAR_OPONENTE); 
    }
    public boolean comprobarNombre(String _nombre){
        accionComprobarNombre();
        enviarMensajeServidor(_nombre);
        recibido = (String)recibirMensajeServidor();
        if(!recibido.equals(OK_NOMBRE)){
            return false;
        }
        return true;
    }
    
    public String invitarOponente(String _nombre){
        accionInvitarOponente();
        enviarMensajeServidor(_nombre);
        recibido = (String)recibirMensajeServidor();
        if(!recibido.equals(NO_OK_JUGAR)){
            ipOponente = recibido;
            System.out.println(ipOponente);
        }
        return NO_OK_JUGAR;
    }
    public void enviarMensajeServidor(Object obj){
        try {
            salida.writeObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Object recibirMensajeServidor(){
        try {
            return entrada.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConectorCli.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
