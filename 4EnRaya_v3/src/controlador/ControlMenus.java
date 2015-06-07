/**
 * ControlMenus.java 
 * @date 22-mayo-2015
 * @author Javi Navarro
 *
 * @version 2 
 * Nuevo controlador con respecto a la version 1 para controlar de una
 * forma más ordenada los menus que mostrara la vista
 */
package controlador;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import vista.IUGrafica;

public class ControlMenus implements Observer {

    public final String OPCION_NOMBRE = "opcion nombre";
    public final String BOTON_REFRESCAR = "Refrescar";
    private final String NOMBRE_VACIO = "Nombre vacio";
    private final String BOTON_INVITAR = "Invitar";
    private final String CARGAR_PARTIDA = "Cargar Partida";
    private final String NUEVA_PARTIDA = "Nueva Partida";
    private final String SALIR = "Salir";
    private IUGrafica vista;
    private Partida partida;
    private String opcion = "";

    public ControlMenus() {

    }

    public void actualizarVista(IUGrafica vista) {
        this.vista = vista;
    }

    public void actualizarPartida(Partida partida) {
        this.partida = partida;
    }

    public void menuInicial() {
        this.vista.menuInicial();
    }

    public void elegirOponente(HashMap _lista){
        this.vista.seleccionarOponente(_lista);
        this.partida.esperarInvitacionJugar();
    }
    
    public void refrescarLista(HashMap _lista){
        this.vista.refrescarLista(_lista);
    }
    
    public void menuFinal() {
        this.vista.menuFinal();
    }

    public void preguntarNombre(String opcion) {
        this.opcion = opcion;
        vista.preguntarNombre();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean) {
            this.partida.seguirJugando((Boolean) arg);
        } else if (arg instanceof String && opcion.equals(OPCION_NOMBRE)) {
            this.partida.asignarNombreJugador((String) arg);
            opcion = "";
        } else {
            String cadena = (String)arg;
            if(cadena.equals(BOTON_REFRESCAR)){
                this.partida.actualizarListaConectados();
            }else if(cadena.equals(NUEVA_PARTIDA)){
                this.partida.cambiarOpcionMenu(opcion);
            }else if(cadena.equals(CARGAR_PARTIDA)){
                this.partida.cambiarOpcionMenu(opcion);
            }else if(cadena.equals(SALIR)){
                this.partida.cambiarOpcionMenu(opcion);
            }else{
                if(!this.partida.asignarNombreJugador(cadena)){
                    this.partida.actualizarListaConectados();
                }
            }
        }
    }
}
