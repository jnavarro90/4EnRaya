/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg4enrayaservidor;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
/**
 *
 * @author Javi
 */

public class HiloConectorSv extends Thread {
    
    private final String PEDIR_NOMBRE = "pedir nombre";
    private final String COMPROBAR_NOMBRE = "comprobar nombre";
    private final String OK_NOMBRE = "ok nombre";
    private final String NO_OK_NOMBRE = "no ok nombre";
    private final String PEDIR_LISTA_CONECTADOS = "pedir lista conectados";
    private final String INVITAR_OPONENTE = "invitar oponente";
    private final String INVITACION_JUGAR = "invitacion jugar";
    private final String OK_JUGAR = "ok jugar";
    private final String NO_OK_JUGAR = "no ok jugar";
    private final String ENVIAR_LISTA_CONECTADOS = "enviar lista conectados";
    private final String ENVIAR_NOMBRE_OPONENTE ="enviar nombre oponente";
    private final String ENVIAR_IP_OPONENTE ="enviar ip oponente";
    private final String ACABAR ="acabar";
    
    private Socket socket;
    private ConectorSv conectorSv;
    private int idSesion;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private String recibido = "";
    public HiloConectorSv(Socket _socket, int _id, ConectorSv _conectorSv) throws IOException {
        this.socket = _socket;
        this.conectorSv = _conectorSv;
        this.idSesion = _id;
        try {
            obtenerFlujos();
            
        } catch (IOException ex) {
            Logger.getLogger(HiloConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void desconnectar() {
        try {
            socket.close();
            entrada.close();
            salida.close();
        } catch (IOException ex) {
            Logger.getLogger(HiloConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarConectorSv(ConectorSv _conectorSv){
        this.conectorSv = _conectorSv; 
    }
    private void obtenerFlujos() throws IOException{
        
        //Creacion de salida de datos
        this.salida = new ObjectOutputStream(socket.getOutputStream());
        this.salida.flush();

        //Creacion de entrada de datos
        this.entrada = new ObjectInputStream(socket.getInputStream());

    }
    
    public void enviarMensajeCliente(Object obj){
        try {
            salida.writeObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(HiloConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Object recibirMensajeCliente(){
        try {
            return entrada.readObject();
        } catch (IOException ex) {
            Logger.getLogger(HiloConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean comprobarNombre(String _nombre){
        
        return true;
    }
    
    private HashMap<String, Integer> listaJugadoresConectados (){
        return conectorSv.getJugadoresConectados();
    }
    
    @Override
    public void run() {
        String nombre = "";
        while(!socket.isClosed()){
            recibido = (String)recibirMensajeCliente();
            if(recibido.equals(COMPROBAR_NOMBRE)){
                nombre = (String)recibirMensajeCliente();
                System.out.println(nombre);
                if(comprobarNombre(nombre)){
                    enviarMensajeCliente(OK_NOMBRE);
                    conectorSv.setIdSesionJugador(nombre, idSesion);
                }else{
                    enviarMensajeCliente(NO_OK_NOMBRE);
                }
            }else if(recibido.equals(PEDIR_LISTA_CONECTADOS)){
                enviarMensajeCliente(listaJugadoresConectados());
            }else if(recibido.equals(INVITAR_OPONENTE)){
                int idSesionOponente = conectorSv.getOponenteIdSesion((String)recibirMensajeCliente());
                enviarMensajeCliente(conectorSv.getIpOponente(idSesionOponente, idSesion));
            }else if(recibido.equals(ACABAR)){
                desconnectar();
            }
        }
    }
}