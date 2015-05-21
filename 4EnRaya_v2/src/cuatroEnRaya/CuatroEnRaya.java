package cuatroEnRaya;

import controlador.ControlMenus;
import controlador.Partida;
import java.io.IOException;
import vista.IUGrafica;
/**
 *
 * @author Javi Navarro
 * @version 1.2
 */
public class CuatroEnRaya {
    public static Partida partidaActual;
    public static IUGrafica vista;
    public static ControlMenus menu;
    public static void main(String[] args) throws IOException {
        vista = new IUGrafica();
        menu = new ControlMenus();
        
        partidaActual = new Partida(vista, menu);
        vista.addObservadorTablero(partidaActual);
        vista.addObservadorMenus(menu);
        
    }
}
