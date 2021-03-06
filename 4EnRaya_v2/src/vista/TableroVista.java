/**
 * TableroVista.java
 * @date 22-mayo-2015
 * @author Javi Navarro
 *
 * @version 2 
 * Una clase donde está el tablero perteneciente a la vista recoge un
 * evento de ratón que le pasa a Partida.java en el paquete del 
 * controlador, y le pasa la casilla que ha elegido el usuario.
 */
package vista;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import utilidades.RecursosAppSwing;

public class TableroVista extends Observable implements ActionListener {

    private static final int ALTURA_FILA = 70;
    private static final int ANCHURA_COLUMNA = 50;
    private CasillaVista casillas[][];
    private RecursosAppSwing recSwingApp;
    private JPanel TVista;
    public static final boolean RECIBIR_EVENTOS_RATON = true;
    public static final boolean NO_RECIBIR_EVENTOS_RATON = false;
    private static final String BOTON_GUARDAR = "BOTON_GUARDAR";
    private static final String BOTON_CHECKPOINT = "BOTON_CHECKPOINT";

    /**
     * TableroVista
     */
    TableroVista(int filas, int columnas,
            boolean recibe_eventos_raton, String rutaRecursos, 
            Icon icono, RecursosAppSwing recSwingApp) {
        crearTableroVista(filas, columnas, recibe_eventos_raton, 
                rutaRecursos, icono, recSwingApp);

    }

    private void crearTableroVista(int filas, int columnas,
            boolean recibe_eventos_raton, String rutaRecursos, 
            Icon icono, RecursosAppSwing recSwingApp) {
        TVista = new JPanel();
        this.recSwingApp = recSwingApp;
        TVista.setLayout(new GridLayout(filas, columnas));
        casillas = new CasillaVista[filas][columnas];

        for (int fil = 0; fil < filas; fil++) {
            for (int col = 0; col < columnas; col++) {
                casillas[fil][col] = new CasillaVista(fil, col, icono);
                TVista.add(casillas[fil][col]);
                if (recibe_eventos_raton) {
                    casillas[fil][col].addMouseListener(new MouseAdapter(){
                        @Override
                        public void mousePressed(MouseEvent e) {
                            setChanged();
                            notifyObservers((CasillaVista) e.getSource());
                        }
                    });
                }
            }
        }
        TVista.setPreferredSize(new Dimension(filas * ALTURA_FILA,
                columnas * ANCHURA_COLUMNA));
    }

    public JPanel getTVista() {
        return TVista;
    }

    /**
     * dimensionCasilla
     */
    public Dimension dimensionCasilla() {
        return casillas[0][0].getSize();
    }

    Icon getIconCasilla(CasillaVista casilla) {
        return casilla.getIcon();
    }

    /**
     * poner
     */
    public void ponerIconoCasilla(int fil, int col, Icon icono) {
        casillas[fil][col].setIcon(icono);
    }

    public CasillaVista[][] getCasillas() {
        return casillas;
    }

    /**
     * actionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        JButton boton = (JButton) e.getSource();
        if (boton.getText().equals(
                recSwingApp.getEtiqueta(BOTON_GUARDAR))) {
            setChanged();
            notifyObservers(BOTON_GUARDAR);
        } else if (boton.getText().equals(
                recSwingApp.getEtiqueta(BOTON_CHECKPOINT))) {
            setChanged();
            notifyObservers(BOTON_CHECKPOINT);
        }
    }
}
