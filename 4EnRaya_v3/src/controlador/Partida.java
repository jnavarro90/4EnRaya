/**
 * Partida.java
 * @date 22-mayo-2015
 * @author Javi Navarro
 *
 * @version 2 
 * La clase controlador que tiene toda la lógica del juego, con
 * pequeños cambios en relación a versión 1 la má importante es que la 
 * mayor parte del método jugar() ha pasado a ser parte del metodo 
 * del Observer update() ya que tiene que recibir la Casilla que 
 * le pase la clase a la que observa que es la clase 
 * TableroVista del paquete de la vista.
 */
package controlador;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import modelo.Jugador;
import modelo.Tablero;
import vista.CasillaVista;
import vista.IUGrafica;

public class Partida implements Observer {

    private final String CARGAR_PARTIDA = "Cargar Partida";
    private final String VOLVER = "Volver";
    private final String NOMBRE_VACIO = "Nombre vacio";
    private static final String BOTON_GUARDAR = "BOTON_GUARDAR";
    private static final String BOTON_CHECKPOINT = "BOTON_CHECKPOINT";
    private final int ERROR_NUM = -1;
    private final String NO_OK_JUGAR = "no ok jugar";
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
    private ConectorCli conector;
    private String opcionMenu = " ";
    private ControlMenus menu;
    private String ipOponente = "";

    public Partida(IUGrafica _vista, ControlMenus _menu) {
        this.vista = _vista;
        this.menu = _menu;
        conector = new ConectorCli();
        conector.start();
        this.tablero = new Tablero();
        this.tablero.addObserver(vista.obtenerTableroSwing());
        this.menu.actualizarVista(vista);
        this.menu.actualizarPartida(this);

        vista.addObservadorTablero(this);
        vista.addObservadorMenus(menu);
        preguntarNombreJugador();
        menu.elegirOponente(conector.getListaJugadoresConectados());
//        this.vista.actualizarPanelDerecha(jugador1, jugador2);
        //this.menu.menuInicial();
    }

    /**
     * El metodo jugar lleva toda la lógica de control del juego
     *
     * @return True o false segun quiera el usuario en menuFinPartida
     */
    public void jugar() {
        //Si es la primera partida
        if (jugador1.getGanadas() == 0 && jugador2.getGanadas() == 0) {
            switch (opcionMenu) {
                case SALIR:
                    salir = true;
                    System.exit(0);
                    break;
                case CARGAR_PARTIDA:
                    if (this.cargar()) {
                        this.mensaje("La partida se ha cargado "
                                + "correctamente.");
                        vista.actualizarPanelDerecha(jugador1, jugador2);
                        vista.pintarTablero();
                        vista.repintarTablero(tablero.getCasillas());

                    } else {
                        this.mensajeError("No tienes ninguna "
                                + "partida guardada.\n"
                                + "Se ha empezado una partida nueva.");
                        vista.vaciarTablero();
                        vista.pintarTablero();
                    }
                    break;
                case NUEVA_PARTIDA:
                    vista.pintarTablero();
                    break;
                default:
                    this.mensaje("Esperando respuesta");
            }
        } else {
            tablero.vaciar();
            vista.pintarTablero();
        }
    }

    /**
     * El metodo guardar serializa el tablero y los dos jugadores, con 
     * eso esta controlado todo lo que hay en la partida en ficheros 
     * con un codigo hash conjunto de los jugadores con cada uno 
     * de sus nombres y con el tablero, si dos jugadores ya tenian
     * una partida guardada la sobreescribe.
     *
     * @return si hay algun error al guardar devuelve false
     */
    public boolean guardar() {

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(RUTA_SAVE + nombre + ".dat"));

            out.writeObject(jugador1);
            out.writeObject(jugador2);
            out.writeObject(tablero);

            out.close();
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    /**
     * El metodo cargar solo cargara si los dos nombres coinciden 
     * con el de una partida ya guardada con el codigo 
     * hash usado en la clase guardar.
     *
     * @return si hay algun error al cargar devuelve false
     */
    public boolean cargar() {

        try {
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(RUTA_SAVE + nombre + ".dat"));

            jugador1 = (Jugador) in.readObject();
            jugador2 = (Jugador) in.readObject();
            tablero = (Tablero) in.readObject();

            in.close();
            this.tablero.addObserver(vista.obtenerTableroSwing());
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    private void preguntarNombreJugador() {
        
        this.jugador1 = new Jugador(NOMBRE_VACIO);
        this.jugador2 = new Jugador(NOMBRE_VACIO);

        /**
         * Control para que el nombre no sea vacio, junto con menuVista que
         * envia NOMBRE_VACIO si el jugador no ha introducido nada
         */
        while (jugador1.getNombre().equals(NOMBRE_VACIO)) {
            menu.preguntarNombre(menu.OPCION_NOMBRE);
        }
        if(!conector.comprobarNombre(jugador1.getNombre())){
            preguntarNombreJugador();
        }
    }

    public boolean asignarNombreJugador(String nombre) {
        if (jugador1.getNombre().equals(NOMBRE_VACIO)) {
            jugador1.setNombre(nombre);
            return true;
        } else {
            jugador2.setNombre(nombre);
            this.nombre = jugador1.hashCode() + jugador2.hashCode();
            System.out.println("Jugadores :" + jugador1.getNombre()+ " - "+jugador2.getNombre());
            ipOponente = conector.invitarOponente(nombre);
            System.out.println(ipOponente);
            if(ipOponente.equals(NO_OK_JUGAR)){
                return false;
            }
            return true;
        }
    }

    public void esperarInvitacionJugar(){
        conector.comprobarInvitacionJugar();
    }
    /**
     * Cambia la variable turno del jugador1 de valor, aunque los dos 
     * tienen una variable turno se puede controlar solo 
     * con uno de los jugadores.
     */
    public void cambiarTurno() {
        this.jugador1.setTurno(!this.jugador1.miTurno());
    }

    /**
     * Cambia la opcion del menu
     *
     * @param _opcion
     */
    public void cambiarOpcionMenu(String _opcion) {
        opcionMenu = _opcion;
        jugar();
    }

    public void seguirJugando(Boolean seguir) {
        if (seguir) {
            jugar();
        } else {
            vista.cerrarTablero();
        }
    }

    public void actualizarListaConectados(){
        menu.refrescarLista(conector.getListaJugadoresConectados());
    }
    /**
     * Escribe un mensaje por pantalla
     *
     * @param mensaje
     */
    public void mensaje(String mensaje) {
        vista.mensaje(mensaje);
    }

    /**
     * Escribe un mensaje de error por pantalla
     *
     * @param error
     */
    public void mensajeError(String error) {
        vista.mensajeError(error);
    }

    @Override
    public void update(Observable o, Object arg) {
        int columna;
        int fila;
        if (arg instanceof CasillaVista) {
            CasillaVista casillaVista = (CasillaVista) arg;
            columna = casillaVista.getCol();
            this.vista.actualizarPanelDerecha(jugador1, jugador2);
            if (this.jugador1.miTurno()) {
                jugadorActual = this.jugador1;
            } else {
                jugadorActual = this.jugador2;
            }
            fila = tablero.esJugadaValida(columna);
            if (fila != ERROR_NUM) {
                this.tablero.ponerFicha(fila, columna,
                        jugadorActual.getSimbolo());
                this.cambiarTurno();

                /**
                 * Comprueba si es ganador o empate
                 */
                if (this.tablero.finDePartida(fila, columna)) {
                    this.mensaje("¡Ganador: "
                            + jugadorActual.getNombre() + "!");
                    jugadorActual.victoria();
                    vista.actualizarPanelDerecha(jugador1, jugador2);
                    vista.ocultarTablero();
                    vista.vaciarTablero();
                    menu.menuFinal();
                } else if (this.tablero.esEmpate()) {
                    this.mensaje("La partida termina en empate.");
                    vista.ocultarTablero();
                    vista.vaciarTablero();
                    menu.menuFinal();
                }
            } else {
                this.mensajeError("No se pudo realizar su jugada,"
                        + " vuelve a intentarlo.");
            }

        } else if (arg instanceof String) {
            String opcion = (String) arg;
            switch (opcion) {
                case SALIR:
                    salir = true;
                case BOTON_GUARDAR:
                    if (this.guardar()) {
                        this.mensaje("La partida se ha guardado "
                                + "correctamente.");
                    } else {
                        this.mensajeError("La partida no se ha guardado"
                                + " correctamente.");
                    }
                    break;
                case BOTON_CHECKPOINT:
                    if (this.cargar()) {
                        this.mensaje("La partida ha vuelto a el"
                                + " ultimo punto de control.");
                        /**
                         * Para que se vuelva a ver bien hay que vaciar y
                         * repintarlo si solo se repinta deja las fichas
                         * antiguas
                         */
                        vista.vaciarTablero();
                        vista.repintarTablero(tablero.getCasillas());

                    } else {
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
