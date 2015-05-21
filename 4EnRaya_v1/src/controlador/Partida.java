package controlador;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;
import modelo.Jugador;
import modelo.Tablero;
import modelo.Casilla;

/**
 *
 * @author Javi Navarro
 * @version 1.2
 */
public class Partida{
    private final int SALIR_JUEGO = 1;
    private final int CARGAR_PARTIDA = 2;
    private final int GUARDAR_PARTIDA = 3;
    private final int VOLVER = 4;
    private final int ERROR = -1;
    private final int NUEVA_PARTIDA = 5;
    private final String RUTA_SAVE = "./guardados/";
    private Tablero tablero;
    private Jugador jugador1;
    private Jugador jugador2;
    private int nombre;
    private Scanner leer;
    public Partida(){
        this.leer = new Scanner(System.in);
        this.mensaje("Introduce el nombre del Jugador 1: ");
        this.jugador1 = new Jugador(this.leer.nextLine(), "#", true);
        this.mensaje("Introduce el nombre del Jugador 2: ");
        this.jugador2 = new Jugador(this.leer.nextLine(), "o", false);
        nombre = jugador1.hashCode() + jugador2.hashCode();
        this.tablero = new Tablero();
    }
    
    /**
     * El metodo jugar lleva toda la lógica de control del juego
     * @return True o false segun quiera el usuario en menuFinPartida
     */
    public boolean jugar(){
        int columna;
        int fila;
        boolean salir = false;
        Jugador jugadorActual;
        //Si es la primera partida
        if(jugador1.getGanadas() == 0 && jugador2.getGanadas() == 0){
            //MENU INICIAL 
                switch (this.menuInicial()){
                    case SALIR_JUEGO:
                        salir = true;
                        break;
                    case CARGAR_PARTIDA:
                        if(this.cargar()){
                            this.mensaje("La partida se ha cargado "
                                    + "correctamente.");
                            this.tablero.dibujar(
                                    this.jugador1, this.jugador2);
                        }else{
                            this.mensajeError("No tienes ninguna "
                                    + "partida guardada.\n"
                                    + "Se ha empezado una partida nueva.");
                            this.tablero.dibujar(
                                    this.jugador1, this.jugador2);
                        }
                        break;
                    case NUEVA_PARTIDA:
                        this.tablero.dibujar(
                                this.jugador1, this.jugador2);
                        break;
                }
        }else{
            tablero.vaciar();
            this.tablero.dibujar(jugador1, jugador2);
        }
            
        while(!salir){
            if(this.jugador1.miTurno()){
                jugadorActual = this.jugador1;
            }else{
                jugadorActual = this.jugador2;
            }
           
            columna = pedirJugada(jugadorActual);
            /**
             * Si la opcion no es -1 no hay error y si hay error 
             * seguira sin saltar turno
            */
            if(columna != ERROR){
                /**
                 * Si la columna no es correcta dara error y seguira 
                 * sin saltar turno
                 */
                fila = this.tablero.esJugadaValida(columna-1);
                if( fila != ERROR){
                    /**
                    * Si la fila no es correcta (porque esta lleno) 
                    * dara error y seguira 
                    * sin saltar turno
                    */
                    this.tablero.ponerFicha(fila, columna-1, 
                            jugadorActual.getSimbolo());
                    this.cambiarTurno();
                    
                    this.tablero.dibujar(this.jugador1, this.jugador2);
                    if(this.tablero.finDePartida(fila, columna-1)){
                        this.mensaje("¡Ganador: "
                                +jugadorActual.getNombre()+"!");
                        jugadorActual.victoria();
                        break;
                    }else if(this.tablero.esEmpate()){
                        this.mensaje("La partida termina en empate.");
                        break;
                    }
                }else{
                    this.mensajeError("No se pudo realizar su jugada,"
                            + " vuelve a intentarlo.");
                }
            }else{
                
                //MENU OPCIONES
                switch (this.menuOpciones()){
                case SALIR_JUEGO:
                    salir = true;
                case GUARDAR_PARTIDA:
                    if(this.guardar()){
                        this.mensaje("La partida se ha guardado "
                                + "correctamente.");
                        this.tablero.dibujar(this.jugador1, this.jugador2);
                    }else{
                        this.mensajeError("La partida no se ha guardado"
                                + " correctamente.");
                    }
                    break;
                case CARGAR_PARTIDA:
                    if(this.cargar()){
                        this.mensaje("La partida ha vuelto a el ultimo punto"
                                + " de control.");
                        this.tablero.dibujar(this.jugador1, this.jugador2);
                    }else{
                        this.mensajeError("La partida no se ha cargado"
                                + " correctamente.");
                    }
                    break;
                case VOLVER:
                    break;  
                }
            }
        }
        if(salir) return false;
        //MENU FIN PARTIDA
        return this.menuFinPartida();
    }
    /**
     * El metodo guardar serializa el tablero y los dos jugadores, con eso 
     * esta controlado todo lo que hay en la partida en ficheros
     * con un codigo hash conjunto de los jugadores con cada 
     * uno de sus nombres y con el tablero, si dos jugadores
     * ya tenian una partida guardada la sobreescribe.
     * @return si hay algun error al guardar devuelve false
     */
    public boolean guardar(){
        int codigoJ1 = nombre + jugador1.getNombre().hashCode();
        int codigoJ2 = nombre + jugador2.getNombre().hashCode();
        int codigoT = nombre + "tablero".hashCode();
        try{
        ObjectOutputStream outJ1 = new ObjectOutputStream(
        new FileOutputStream(RUTA_SAVE+codigoJ1+".dat"));
        ObjectOutputStream outJ2;
        outJ2 = new ObjectOutputStream(
        new FileOutputStream(RUTA_SAVE+codigoJ2+".dat"));
        ObjectOutputStream outT = new ObjectOutputStream(
        new FileOutputStream(RUTA_SAVE+codigoT+".dat"));
        
        outJ1.writeObject(jugador1);
        outJ2.writeObject(jugador2);
        outT.writeObject(tablero);
        
        outJ1.close();
        outJ2.close();
        outT.close();
        } catch (Exception ex) {
            return false;
        }
    return true;
    }
    /**
     * El metodo cargar solo cargara si los dos nombres coinciden con 
     * el de una partida ya guardada con el codigo hash usado
     * en la clase guardar.
     * @return si hay algun error al cargar devuelve false
     */
    public boolean cargar(){
        int codigoJ1 = nombre + jugador1.getNombre().hashCode();
        int codigoJ2 = nombre + jugador2.getNombre().hashCode();
        int codigoT = nombre + "tablero".hashCode();
        
        try{
        ObjectInputStream inJ1 = new ObjectInputStream(
        new FileInputStream(RUTA_SAVE+codigoJ1+".dat"));
        ObjectInputStream inJ2 = new ObjectInputStream(
        new FileInputStream(RUTA_SAVE+codigoJ2+".dat"));
        ObjectInputStream inT = new ObjectInputStream(
        new FileInputStream(RUTA_SAVE+codigoT+".dat"));
        
        jugador1 = (Jugador)inJ1.readObject();
        jugador2 = (Jugador)inJ2.readObject();
        tablero = (Tablero)inT.readObject();
        
        inJ1.close();
        inJ2.close();
        inT.close();
        } catch (Exception ex) {
            return false;
        }
    return true;
    }
    /**
     * Cambia la variable turno del jugador1 de valor, aunque los dos tienen
     * una variable turno se puede controlar solo con uno de los jugadores.
     */
    public void cambiarTurno(){
        this.jugador1.setTurno(!this.jugador1.miTurno());
    }
    
    public Casilla [][] obtenerTablero(){
        return tablero.getCasillas();
    }
    
    /**
     * Pide a un jugador una jugada para que se realize en el juego
     * Este método lo haría la vista
     * @param j
     * @return opcion
     */
    public int pedirJugada(Jugador j){
        int opcion;
        mensaje("Introduce una columna o pulsa una tecla no numerica para "
                + "ver opciones.\nTurno de "+j.getNombre()+": \n");
        try {
            opcion = this.leer.nextInt();
        } catch (InputMismatchException e) {
            leer.next();
            return ERROR;
        }
        return opcion;
    }
    
    /**
     * Pide al usuario una opcion para el menu de inicio
     * Este método lo haría la vista
     * @return pasa de letras a int que uso con constantes
     */
    public int menuInicial(){
        this.leer = new Scanner(System.in);
        this.mensaje("----------------------\n"
                       + "OPCIONES: \n"
                       + "n: Nueva partida\n"
                       + "c: Cargar patrida  \n"
                       + "s: Salir del juego\n"
                       + "----------------------\n");
        mensaje("Introduce opcion: ");
        while(true){   
            switch (this.leer.nextLine()){
                    case "s":
                        return SALIR_JUEGO;
                    case "n":
                        return NUEVA_PARTIDA;
                    case "c":
                        return CARGAR_PARTIDA;
                    default:
                        this.mensajeError("Opcion incorrecta");
                        break;
            }
        }
    }
    /**
     * Pide al usuario una opcion para el menu de fin de partida
     * Este método lo haría la vista
     * @return true o false ya que solo hay dos opciones
     */
    public boolean menuFinPartida(){
        this.leer = new Scanner(System.in);
        this.mensaje("----------------------\n"
                       + "OPCIONES: \n"
                       + "v: Volver a jugar  \n"
                       + "s: Salir del juego\n"
                       + "----------------------\n");
        mensaje("Introduce opcion: ");
        while(true){
            switch (this.leer.nextLine()){
                    case "s":
                        return false;
                    case "v":
                        return true;
                    default:
                        this.mensajeError("Opcion incorrecta");
                        break;
            }
        }
    }
    /**
     * Pide al usuario una opcion para el menu de opciones
     * Este método lo haría la vista
     * @return pasa de letras a int que uso con constantes
     */
    public int menuOpciones(){
        this.leer = new Scanner(System.in);
        this.mensaje("----------------------\n"
                       + "OPCIONES: \n"
                       + "g: Guardar partida\n"
                       + "c: Cargar checkpoint  \n"
                       + "v: Volver\n"
                       + "s: Salir del juego\n"
                       + "----------------------\n");
        mensaje("Introduce opcion: ");
        switch (this.leer.nextLine()){
                case "s":
                    return SALIR_JUEGO;
                case "g":
                    return GUARDAR_PARTIDA;
                case "c":
                    return CARGAR_PARTIDA;
                case "v":
                    return VOLVER;
                default:
                        this.mensajeError("Opcion incorrecta");
                        break;
        }
        return ERROR;
    }
    /**
     * Escribe un mensaje por pantalla 
     * Este método lo haría la vista
     * @param mensaje 
     */
    
    public void mensaje(String mensaje){
        System.out.println(mensaje);
    }
    
    /**
     * Escribe un mensaje de error por pantalla 
     * Este método lo haría la vista
     * @param error 
     */
    public void mensajeError(String error){
        System.out.println("ERROR: "+error);
    }
}