/**
 * TableroSwing2.java
 * 19-abr-2012
 * @author ccatalan
 * 
 * Version 1.0
 * Ejemplo tablero aplicación Swing
 * Demos de:
 *   - Uso de Manager Layouts
 *   - Empleo de JLabel para la representación en pantalla de un tablero.
 *   - Dimensionamiento de la ventana principal automático según el tamaño del
 *     tablero.
 *   - Indicación con el ratón de la tirada, visualización y selección de 
 *     contricante.
 *   - Empleo de un paquete para recursos (imágenes, textos, etc.)
 *   - Recursos internacionalizables mediante fichero XML, accedido con
 *     clases reutilizables.
 * 
 *  Version 1.1
 *  27-abr-2012
 *  Modificaciones para mejorar la separación MVC
 *    - TableroBarcos pasa a llamarse TableroVista
 *    - TableroVista es modificado en método actualiza 
 *    - Cambio en el main al inicializar los recursos
 */
package vista;

import controlador.Partida;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Casilla;
import modelo.Jugador;
import modelo.Tablero;
import utilidades.RecursosAppSwing;

/**
 * Tablero Swing
 */
public class TableroSwing extends JFrame implements ActionListener, Observer { 
  private final int  MAX_COLUMNAS = 8;
  private final int MAX_FILAS = 7;
  private RecursosAppSwing recSwingApp;
  private TableroVista tableroVista;
  private Jugador jugador1;
  private Jugador jugador2;
  private String turnoj1 = "";
  private String turnoj2 = "";
  private JLabel labelj1 = new JLabel();
  private JLabel labelj2 = new JLabel(); 
  private JLabel labelMovimientos = new JLabel(); 
  private JPanel panelDerecha = new JPanel();
  private JButton botonGuardar;
  private JButton botonCheckpoint;
  
  private JLabel estadoTablero;
  
  /** Identificadores de textos dependientes de idioma */
  private static final String MENU_FICHERO = "MENU_FICHERO";
  private static final String MENU_ITEM_SALIR = "MENU_ITEM_SALIR";
  private static final String MENU_AYUDA = "MENU_AYUDA";
  private static final String MENU_ITEM_ACERCA_DE = "MENU_ITEM_ACERCA_DE";
  private static final String ETIQUETA_OPCIONES = "OPCIONES";
  private static final String ETIQUETA_MOVIMIENTOS = "MOVIMIENTOS";
  private static final String ETIQUETA_MARCADOR = "MARCADOR";
  private static final String BOTON_GUARDAR = "BOTON_GUARDAR";
  private static final String BOTON_CHECKPOINT = "BOTON_CHECKPOINT";
  
  private static final String ESTADO_TABLERO = "ESTADO_TABLERO";
          
  /** Constantes para redimensionamiento */
  private static final int ANCHURA_LISTA_JUGADORES = 160;
  private static final int ALTURA_LISTA_JUGADORES = 200;
  private static final int MARGEN_HORIZONTAL = 50;
  private static final int MARGEN_VERTICAL = 100;
  
 /**
  * TableroSwing
  */  
  TableroSwing(int numFilTab, int numColTab, 
               final RecursosAppSwing recSwingApp) {
    super(recSwingApp.getGeneral(RecursosAppSwing.TITULO));
    this.recSwingApp = recSwingApp;
     
    jugador1 = new Jugador("", "", true);
    jugador2 = new Jugador("", "", true);
    
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        acaba();
      }
    });
    
    getContentPane().setLayout(new BorderLayout());
    
    // creamos elementos
    creaMenus();
    
    JPanel panelCentral = new JPanel();
    panelCentral.setLayout(new FlowLayout());
    creaTablero(panelCentral, numFilTab, numColTab);
    creaPanelDerecha(panelCentral);
    getContentPane().add(panelCentral);
    
    estadoTablero = new JLabel(this.recSwingApp.getEtiqueta(ESTADO_TABLERO));
    getContentPane().add(estadoTablero, BorderLayout.SOUTH);
 
    // hacemos visible con el tamaño y la posición deseados 
    setIconImage(this.recSwingApp.getIconoImagenApp());
    setResizable(false);
    setSize((int)(tableroVista.dimensionCasilla().getWidth() * 
                  numColTab + ANCHURA_LISTA_JUGADORES + MARGEN_HORIZONTAL), 
            (int)(tableroVista.dimensionCasilla().getHeight() * 
                  numFilTab + MARGEN_VERTICAL));
    
    pack();  // ajusta ventana y sus componentes
    //setVisible(true);
    setLocationRelativeTo(null);  // centra en la pantalla
  }  
  
  /**
  * creaMenus
  */  
  private void creaMenus() {
    JMenu menuFichero = new JMenu(this.recSwingApp.getTextoMenu(MENU_FICHERO));
    JMenuItem menuFicheroSalir = 
    new JMenuItem(this.recSwingApp.getTextoMenuItem(MENU_ITEM_SALIR),
                  this.recSwingApp.getAtajoMenuItem(MENU_ITEM_SALIR));
    menuFicheroSalir.addActionListener(this);

    menuFichero.add(menuFicheroSalir);

    JMenu menuAyuda = new JMenu(this.recSwingApp.getTextoMenu(MENU_AYUDA));
    JMenuItem menuAyudaAcercaDe = 
    new JMenuItem(this.recSwingApp.getTextoMenuItem(MENU_ITEM_ACERCA_DE),
                  this.recSwingApp.getAtajoMenuItem(MENU_ITEM_ACERCA_DE));
    menuAyudaAcercaDe.addActionListener(this);
    menuAyuda.add(menuAyudaAcercaDe);    

    JMenuBar menuPrincipal = new JMenuBar();
    menuPrincipal.add(menuFichero);
    menuPrincipal.add(menuAyuda);
    getContentPane().add(menuPrincipal,BorderLayout.NORTH);     
  }

 /**
  * creaTablero
  */   
  private void creaTablero(JPanel panel, int numFilTab, int numColTab) {
    tableroVista = new TableroVista(numFilTab, numColTab, 
                                      TableroVista.RECIBIR_EVENTOS_RATON, 
                                      RecursosAppSwing.RUTA_RECURSOS, recSwingApp.getIconoApp(), recSwingApp);
    tableroVista.addObserver(this);
    panel.add(tableroVista.getTVista());
    panel.add(new JPanel());
  }
  
  public void ocultar(){
      this.setVisible(false);
  }
  public void setJugadores(Jugador j1, Jugador j2){
      this.jugador1 = j1;
      this.jugador2 = j2;
      
      if(!j1.miTurno()){
            turnoj1 =">> ";
            turnoj2 = "";
        }else{
            turnoj1 = "";
            turnoj2 = " <<";
      }
      this.labelj1.setText(turnoj1+jugador1.getNombre()+" "+jugador1.getGanadas()+" -");
      this.labelj2.setText(jugador2.getGanadas()+" "+jugador2.getNombre()+turnoj2);
  }

 /**
  * creaListaJugadores
  */     
  private void creaPanelDerecha(JPanel panel) {
    JPanel panelMarcador = new JPanel();
    JPanel panelBotones = new JPanel();
    JPanel panelMovimientos = new JPanel();
    panelDerecha.setLayout(new BorderLayout());    
    botonGuardar = new JButton(this.recSwingApp.getEtiqueta(BOTON_GUARDAR));
    botonCheckpoint = new JButton(this.recSwingApp.getEtiqueta(BOTON_CHECKPOINT));
    
    botonGuardar.addActionListener(tableroVista);
    botonCheckpoint.addActionListener(tableroVista);
    panelBotones.add(botonGuardar, BorderLayout.SOUTH);
    panelBotones.add(botonCheckpoint,BorderLayout.NORTH);
    panelBotones.setBorder(javax.swing.BorderFactory.createTitledBorder(this.recSwingApp.getEtiqueta(ETIQUETA_OPCIONES)));
    
    panelMarcador.add(labelj1, BorderLayout.NORTH);
    panelMarcador.add(labelj2, BorderLayout.SOUTH);
    panelMarcador.setBorder(javax.swing.BorderFactory.createTitledBorder(this.recSwingApp.getEtiqueta(ETIQUETA_MARCADOR)));
    
    panelMovimientos.add(labelMovimientos);
    panelMarcador.setBorder(javax.swing.BorderFactory.createTitledBorder(this.recSwingApp.getEtiqueta(ETIQUETA_MOVIMIENTOS)));
    
    panelDerecha.add(panelMarcador, BorderLayout.NORTH);
    panelDerecha.add(panelBotones, BorderLayout.CENTER);
    panelDerecha.add(panelMovimientos, BorderLayout.SOUTH);
    panel.add(panelDerecha, BorderLayout.SOUTH);
    
  }
  
  /**
   * actionPerformed
   */
  @Override
  public void actionPerformed(ActionEvent e) {
       
       
        JMenuItem menuItem = (JMenuItem)e.getSource();
        if(menuItem.getText().equals(recSwingApp.getTextoMenuItem(MENU_ITEM_SALIR)))
          acaba();
        else if (menuItem.getText().equals(
          recSwingApp.getTextoMenuItem(MENU_ITEM_ACERCA_DE))) {
          JOptionPane.showMessageDialog(this,
            recSwingApp.getGeneral(RecursosAppSwing.TITULO) + "\n" + 
            recSwingApp.getGeneral(RecursosAppSwing.AUTOR)+ "\n" + 
            recSwingApp.getGeneral(RecursosAppSwing.VERSION), 
            recSwingApp.getTextoMenuItem(MENU_ITEM_ACERCA_DE), 
            JOptionPane.INFORMATION_MESSAGE,
            recSwingApp.getIconoApp());
        }
  }
  
  private int invertirNumero(int n){
     switch (n){ 
         case 1:
             return 5;
         case 2:
             return 4;
         case 3:
             return 3;
         case 4:
             return 2;
         case 5:
             return 1;
         case 6:
             return 0;
         case 7:
             return -1;
         default:
             return -1;
     }
  }
  
  public void vaciar(){
        for (int fil = 1; fil < MAX_FILAS-1; fil++){
            for(int col = 0; col < MAX_COLUMNAS-1; col++){
                tableroVista.ponerIconoCasilla(fil, col, 
                    recSwingApp.getIconoApp());     
        }
      }
        setJugadores(jugador1, jugador2);
  }
  
  public void repintar(Casilla[][] casillas){
      for (int fil = 1; fil < MAX_FILAS; fil++){
            for(int col = 0; col < MAX_COLUMNAS; col++){
                if(casillas[fil][col].getSimbolo().equals("#")){
                    tableroVista.ponerIconoCasilla(invertirNumero(fil), col, 
                                 recSwingApp.getIconoColor1());
                }else if(casillas[fil][col].getSimbolo().equals("o")){
                    tableroVista.ponerIconoCasilla(invertirNumero(fil), col, 
                                 recSwingApp.getIconoColor2());
                }            
        }
      }
  }
 public void nuevoObservador(Observer controlador){
        tableroVista.addObserver(controlador);
 }
  /**
   * acaba
   */  
  public void acaba() {
    // ... aquí puede ir código de finalización
    
    System.exit(0);  
  }

    @Override
    public void update(Observable o, Object arg) {

    setJugadores(jugador1, jugador2);
    if (arg instanceof Casilla[][]){
        Casilla[][] casillas = (Casilla[][])arg;
        for (int fil = 1; fil < MAX_FILAS; fil++){
            for(int col = 0; col < MAX_COLUMNAS; col++){
                if(casillas[fil][col].getSimbolo().equals("#")){
                    tableroVista.ponerIconoCasilla(invertirNumero(fil), col, 
                                 recSwingApp.getIconoColor1());
                }else if(casillas[fil][col].getSimbolo().equals("o")){
                    tableroVista.ponerIconoCasilla(invertirNumero(fil), col, 
                                 recSwingApp.getIconoColor2());
                }            
        }
      }
    }
    }
}
