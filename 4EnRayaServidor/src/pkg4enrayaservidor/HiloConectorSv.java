/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg4enrayaservidor;

import java.io.*;
import java.net.*;
import java.util.Observable;
import java.util.logging.*;
/**
 *
 * @author Javi
 */

public class HiloConectorSv extends Thread {
    private Socket socket;

    private int idSessio;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    public HiloConectorSv(Socket socket, int id) throws IOException {
        this.socket = socket;

        this.idSessio = id;
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

    private void obtenerFlujos() throws IOException{
        
        //Creacion de salida de datos
        this.salida = new ObjectOutputStream(socket.getOutputStream());
        this.salida.flush();

        //Creacion de entrada de datos
        this.entrada = new ObjectInputStream(socket.getInputStream());

    }
    
    @Override
    public void run() {
        String accion = "";

        try {
            accion = (String)entrada.readObject();
            if(accion.equals("hola")){
                System.out.println("El cliente con idSesion "+this.idSessio+" saluda");
                salida.writeObject("adios");
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloConectorSv.class.getName()).log(Level.SEVERE, null, ex);
        }
        desconnectar();
    }
}