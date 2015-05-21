package controlador;

import static cuatroEnRaya.CuatroEnRaya.menu;
import static cuatroEnRaya.CuatroEnRaya.partidaActual;
import static cuatroEnRaya.CuatroEnRaya.vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;
import modelo.Casilla;
import modelo.Jugador;
import modelo.Tablero;
import vista.CasillaVista;
import vista.IUGrafica;
import vista.Observador;

/**
 *
 * @author Javi Navarro
 * @version 1.2
 */
public class Partida implements Observador{
    private final String CARGAR_PARTIDA = "Cargar Partida";
    private final String GUARDAR_PARTIDA = "Guardar Partida";
    private final String VOLVER = "Volver";
    private final String ERROR = "Error";
    private static final String BOTON_GUARDAR = "BOTON_GUARDAR";
    private static final String BOTON_CHECKPOINT = "BOTON_CHECKPOINT";
    private final int ERROR_NUM = -1;
    private final String NUEVA_PARTIDA = "Nueva Partida";
    private final String SALIR = "Salir";
    private final String RUTA_SAVE = "./guardados/";
    private IUGrafica vista;
    private Tablero tablero;
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean salir = false;
    private Jugador jugadorActual;
    private int nombre;
    private Scanner leer;
    private String opcionMenu = " ";
    private ControlMenus menu;
    
    public Partida(IUGrafica _vista, ControlMenus _menu){
        this.vista = _vista;
        this.menu = _menu;

        this.jugador1 = new Jugador(vista.preguntarNombre(
                "Jugador 1"), "#", true);
        this.jugador2 = new Jugador(vista.preguntarNombre(
                "Jugador 2"), "o", false);

        this.nombre = jugador1.hashCode() + jugador2.hashCode();
        this.tablero = new Tablero();
        this.tablero.nuevoObservador(vista.obtenerTableroSwing());
        this.menu.actualizarVista(vista);
        this.menu.actualizarPartida(this);
        this.vista.setNombresJugadores(jugador1, jugador2);
        this.menu.menuInicial();
    }
    
    /**
     * El metodo jugar lleva toda la lógica de control del juego
     * @return True o false segun quiera el usuario en menuFinPartida
     */
    public void jugar(){
        //Si es la primera partida
        if(jugador1.getGanadas() == 0 && jugador2.getGanadas() == 0){
                switch (opcionMenu){
                    case SALIR:
                        salir = true;
                        break;
                    case CARGAR_PARTIDA:
                        if(this.cargar()){
                            this.mensaje("La partida se ha cargado "
                                    + "correctamente.");
                            vista.pintarTablero();
                        }else{
                            this.mensajeError("No tienes ninguna "
                                    + "partida guardada.\n"
                                    + "Se ha empezado una partida nueva.");
                            vista.pintarTablero();
                        }
                        break;
                    case NUEVA_PARTIDA:
                        vista.pintarTablero();
                        break;
                    default:
                        this.mensaje("Esperando respuesta");
                }
        }else{
            tablero.vaciar();
            vista.pintarTablero();
        }
    }
    /**
     * El metodo guardar serializa el tablero y los dos jugadores, 
     * con eso esta controlado todo lo que hay en la
     * partida en ficheros con un codigo hash conjunto de
     * los jugadores con cada uno de sus nombres y 
     * con el tablero, si dos jugadores ya tenian una 
     * partida guardada la sobreescribe.
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
            System.out.println(ex);
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
            System.out.println(ex);
            return false;
        }
    return true;
    }
    /**
     * Cambia la variable turno del jugador1 de valor, 
     * aunque los dos tienen una variable turno se puede
     * controlar solo con uno de los jugadores.
     */
    public void cambiarTurno(){
        this.jugador1.setTurno(!this.jugador1.miTurno());
    }
    
    /**
     * Cambia la opcion del menu
     * @param _opcion 
     */
    public void cambiarOpcionMenu(String _opcion){
        opcionMenu = _opcion;
        jugar();
    }
    
    public void seguirJugando(Boolean seguir){
        if(seguir){
            jugar();
        }else{
            vista.cerrarTablero();
        }
    }

    /**
     * Escribe un mensaje por pantalla 
     * @param mensaje 
     */
    
    public void mensaje(String mensaje){
        vista.mensaje(mensaje);
    }
    
    /**
     * Escribe un mensaje de error por pantalla
     * @param error 
     */
    public void mensajeError(String error){
        vista.mensajeError(error);
    }

    @Override
    public void actualiza(Object obj) {
        int columna;
        int fila;    
        if (obj instanceof CasillaVista){
            CasillaVista casillaVista = (CasillaVista)obj;
            columna = casillaVista.getCol();
            this.vista.setNombresJugadores(jugador1, jugador2);
            System.out.println(columna);
            if(this.jugador1.miTurno()){
                jugadorActual = this.jugador1;
            }else{
                jugadorActual = this.jugador2;
            }
            fila = tablero.esJugadaValida(columna);
            if(fila != ERROR_NUM){
                    this.tablero.ponerFicha(fila, columna, 
                            jugadorActual.getSimbolo());
                    this.cambiarTurno();
                    if(this.tablero.finDePartida(fila, columna)){
                        this.mensaje("¡Ganador: "
                                +jugadorActual.getNombre()+"!");
                        jugadorActual.victoria();
                        vista.ocultarTablero();
                        vista.VaciarTablero();
                        menu.menuFinal();
                    }else if(this.tablero.esEmpate()){
                        this.mensaje("La partida termina en empate.");
                        vista.ocultarTablero();
                        vista.VaciarTablero();
                        menu.menuFinal();
                    }
            }else{
                this.mensajeError("No se pudo realizar su jugada,"
                        + " vuelve a intentarlo.");
            }
//            }else{
//                
//                //MENU OPCIONES
//                
//               // switch (this.menuOpciones()){
//                
//            }
        }else if (obj instanceof String){
            String opcion = (String)obj;
            switch (opcion) {
                case SALIR:
                    salir = true;
                case BOTON_GUARDAR:
                    if(this.guardar()){
                        this.mensaje("La partida se ha guardado "
                                + "correctamente.");
//                        this.tablero.dibujar(this.jugador1, 
//                                this.jugador2);
                    }else{
                        this.mensajeError("La partida no se ha guardado"
                                + " correctamente.");
                    }
                    break;
                case BOTON_CHECKPOINT:
                    if(this.cargar()){
                        this.mensaje("La partida ha vuelto a el"
                                + " ultimo punto de control.");
//                        this.tablero.dibujar(this.jugador1, 
//                                this.jugador2);
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
}