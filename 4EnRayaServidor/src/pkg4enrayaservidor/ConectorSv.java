
package pkg4enrayaservidor;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
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
    private final String INVITACION_JUGAR = "invitacion jugar";
    private final String OK_JUGAR = "ok jugar";
    private final String NO_OK_JUGAR = "no ok jugar";
    private final String SALIR = "Salir";
    
    private Socket s;
    private ServerSocket ss;
    private final int puerto = 9000;
    private int idSesion = 0;
    private HashMap<String, Integer> idSesionJugadores;
    private HiloConexion conexion;
    private boolean primeraConexion =true;
    private HashMap<Integer, HiloConectorSv> jugadoresConectados;
    
    public ConectorSv() throws IOException{
        this.ss = new ServerSocket(puerto);
        this.conexion = new HiloConexion(this, ss);
        jugadoresConectados = new HashMap<>();
        idSesionJugadores = new HashMap<>();
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

    public HashMap<String, Integer> getJugadoresConectados() {
        return idSesionJugadores;
    }
    
    public int getOponenteIdSesion(String _nombre){
        return idSesionJugadores.get(_nombre);
    }
    
    public String getIpOponente(int _idSesion, int _miIdSesion){
        /**
         * Se envia un mensaje al hilo guardado para saber si quiere jugar
         */
        jugadoresConectados.get(_idSesion).enviarMensajeCliente(INVITACION_JUGAR);
        if(jugadoresConectados.get(_idSesion).recibirMensajeCliente().equals(OK_JUGAR)){
            jugadoresConectados.get(_idSesion).enviarMensajeCliente(getIp(_miIdSesion));
            return "192.168.1.100";
        }
        return NO_OK_JUGAR;
    }
    public String getIp(int _idSesion){
        //jugadoresConectados.get(_idSesion).
        return "192.168.1.101";
    }
    
    private void actualizarHilos(){
        Iterator it = jugadoresConectados.keySet().iterator();
        while(it.hasNext()){
          Integer key = (Integer)it.next();
          jugadoresConectados.get(key).actualizarConectorSv(this);
        }
    }
    
    public void setIdSesionJugador(String _nombre, int _idSesion){
        idSesionJugadores.put(_nombre, _idSesion);
        actualizarHilos();
//        Iterator it = idSesionJugadores.keySet().iterator();
//        while(it.hasNext()){
//          String key = (String)it.next();
//            System.out.println("Nombre: " + key + "--> idSesion: " + idSesionJugadores.get(key));
//        }
    }
    
    public void procesarConexion() throws IOException{
//        ((HiloConectorSv) new HiloConectorSv(s, idSesion)).start();
//        idSesion++;
        
        jugadoresConectados.put(idSesion, (HiloConectorSv) new HiloConectorSv(s, idSesion, this));
        jugadoresConectados.get(idSesion).start();
        actualizarHilos();
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
