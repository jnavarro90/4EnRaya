package vista;

/**
 *
 * @author Javi
 */
import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuVista extends Observable{
    public final String CARGAR_PARTIDA = "Cargar Partida";
    public final String GUARDAR_PARTIDA = "Guardar Partida";
    public final String SEGUIR_JUGANDO = "Seguir Jugando";
    public final String ERROR = "Error";
    public final String NUEVA_PARTIDA = "Nueva Partida";
    public final String SALIR = "Salir";
    public final String SALIR_B = "Acabar Partida";
    public final String ACERCA_DE = "Acerca de ..";
     private final String NOMBRE_VACIO = "Nombre vacio";
    private String titulo;
    private JPanel MVista;

    public MenuVista(String titulo){
        MVista = new JPanel();
        this.titulo = titulo;
    }
    
    public void obtenerNombreJugador(){
        String nombre = JOptionPane.showInputDialog(
                MVista, "Introduce el nombre del jugador", titulo, 
                JOptionPane.QUESTION_MESSAGE);
        if(nombre.isEmpty()){
            setChanged();
            notifyObservers(NOMBRE_VACIO);
        }else{
            setChanged();
            notifyObservers(nombre);
        }
    }
    
    public void obtenerOpcionMenuInicial(){
        String[] options = {NUEVA_PARTIDA, CARGAR_PARTIDA, SALIR};
        int opcion = 0; 
        
        opcion = JOptionPane.showOptionDialog(MVista, "  ¿Qué desea hacer?","4 En Raya", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        if (opcion == 0){
            setChanged();
            notifyObservers(NUEVA_PARTIDA);
        }else if(opcion == 1){
            setChanged();
            notifyObservers(CARGAR_PARTIDA);
        }else if(opcion == 2){
            setChanged();
            notifyObservers(SALIR);
        }
    }
    
    public void obtenerOpcionMenuFinal(){
        
        int opcion = 0;

        opcion = JOptionPane.showOptionDialog(MVista, "  ¿Volver a jugar?","4 En Raya", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if(opcion == JOptionPane.YES_OPTION){
            setChanged();
            notifyObservers(true);
        }else{
            setChanged();
            notifyObservers(false);
        }
    }
}
