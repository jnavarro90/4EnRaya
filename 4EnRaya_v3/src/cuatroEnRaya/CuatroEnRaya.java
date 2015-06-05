/**
 * CuatroEnRaya.java
 * @date 22-mayo-2015
 * @author Javi Navarro
 *
 * @version 2 
 * La clase que contiene el main
 */
package cuatroEnRaya;

import controlador.ControlMenus;
import controlador.Partida;
import java.io.IOException;
import vista.IUGrafica;

public class CuatroEnRaya {

    public static Partida partidaActual;
    public static IUGrafica vista;
    public static ControlMenus menu;

    public static void main(String[] args) throws IOException {
        vista = new IUGrafica();
        menu = new ControlMenus();

        partidaActual = new Partida(vista, menu);
    }
}
