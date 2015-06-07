/**
 * MenuVista.java 
 * @date 22-mayo-2015
 * @author Javi Navarro
 * 
 * @version 2 
 * Una vista para los diferentes menus de la aplicación será
 * Observable por ControlMenus.java en el paquete del controlador
 */
package vista;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MenuVista extends Observable implements ActionListener {

    public final String CARGAR_PARTIDA = "Cargar Partida";
    public final String GUARDAR_PARTIDA = "Guardar Partida";
    public final String NUEVA_PARTIDA = "Nueva Partida";
    public final String SALIR = "Salir";
    public final String BOTON_SALIR = "Salir";
    public final String BOTON_REFRESCAR = "Refrescar";
    private final String NOMBRE_VACIO = "Nombre vacio";
    private final String BOTON_INVITAR = "Invitar";
    private String titulo;
    private JPanel mVista;
    private JList list;
    private DefaultListModel model;

    public MenuVista(String titulo) {
        mVista = new JPanel();
        this.titulo = titulo;
    }

    public void seleccionOponente(HashMap _lista){
        mVista.setLayout(new BorderLayout());
        model = new DefaultListModel();
        list = new JList(model);
        JScrollPane pane = new JScrollPane(list);
        JButton botonInvitar = new JButton(BOTON_INVITAR);
        JButton botonSalir = new JButton(BOTON_SALIR);
        JButton botonRefrescar = new JButton(BOTON_REFRESCAR);
        
        Iterator it = _lista.keySet().iterator();
        while(it.hasNext()){
            String key = (String)it.next();
            model.addElement(key);
        }
        
        botonInvitar.addActionListener(this);
        botonRefrescar.addActionListener(this);
        botonSalir.addActionListener(this);
        
        mVista.add(pane, BorderLayout.NORTH);
        mVista.add(botonInvitar, BorderLayout.WEST);
        mVista.add(botonSalir, BorderLayout.EAST);
        mVista.add(botonRefrescar, BorderLayout.CENTER);
        JFrame frame = new JFrame("Jugadores OnLine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mVista);
        frame.setSize(260, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void refrescarOponentes(HashMap _lista){
        model = new DefaultListModel();
        Iterator it = _lista.keySet().iterator();
        while(it.hasNext()){
            String key = (String)it.next();
            model.addElement(key);
        }
    }
    
    public void obtenerNombreJugador() {
        String nombre = JOptionPane.showInputDialog(
                mVista, "Introduce tu nombre", titulo,
                JOptionPane.QUESTION_MESSAGE);
        if (nombre.isEmpty()) {
            setChanged();
            notifyObservers(NOMBRE_VACIO);
        } else {
            setChanged();
            notifyObservers(nombre);
        }
    }

    public void obtenerOpcionMenuInicial() {
        String[] options = {NUEVA_PARTIDA, CARGAR_PARTIDA, SALIR};
        int opcion = 0;

        opcion = JOptionPane.showOptionDialog(mVista,
                "¿Qué desea hacer?", "4 En Raya", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, null);
        if (opcion == 0) {
            setChanged();
            notifyObservers(NUEVA_PARTIDA);
        } else if (opcion == 1) {
            setChanged();
            notifyObservers(CARGAR_PARTIDA);
        } else if (opcion == 2) {
            setChanged();
            notifyObservers(SALIR);
        }
    }

    public void obtenerOpcionMenuFinal() {

        int opcion = 0;

        opcion = JOptionPane.showOptionDialog(mVista, 
                "¿Volver a jugar?", "4 En Raya", JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (opcion == JOptionPane.YES_OPTION) {
            setChanged();
            notifyObservers(true);
        } else {
            setChanged();
            notifyObservers(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton boton = (JButton) e.getSource();
        if (boton.getText().equals(BOTON_INVITAR)) {
            setChanged();
            notifyObservers(list.getSelectedValue());
        } else if (boton.getText().equals(BOTON_REFRESCAR)) {
            setChanged();
            notifyObservers(BOTON_REFRESCAR);
        } else if (boton.getText().equals(BOTON_SALIR)){
            setChanged();
            notifyObservers(BOTON_SALIR);
        }
    }
}
