package cuatroEnRaya;

import controlador.Partida;
import java.io.IOException;
/**
 *
 * @author Javi Navarro
 * @version 1.2
 */
public class CuatroEnRaya {
    public static Partida partidaActual;
    public static void main(String[] args) throws IOException {
        partidaActual = new Partida();
            
        while(true){        
            if(partidaActual.jugar()){
                partidaActual.mensaje("Volver a jugar.");
            }else{
                partidaActual.mensaje("Salir del juego.");
                break;
            }
        } 
    } 
}
