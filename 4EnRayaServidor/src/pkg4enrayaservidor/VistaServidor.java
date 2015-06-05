/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg4enrayaservidor;

/**
 *
 * @author Javi
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


public class VistaServidor extends Observable implements Observer, ActionListener{

    
    private final String MENSAJE_INICIO = "Mensaje inicio";
    private final String INICIAR_SERVIDOR = "Iniciar servidor";
    private final String APAGAR_SERVIDOR = "Apagar servidor";
    private final String TITULO = "Servidor 4 en Raya";
    private final String DESCONECTADO = "Desconectado";
    private final String CONECTADO = "Conectado";
    private final String SALIR = "Salir";
    
    private JFrame vServidor;
    private JTextArea textJugadores;
    private JLabel statusbar;
    
    public VistaServidor() {

        initUI();
    }

    public void initUI() {
        vServidor = new JFrame();

        JButton botonSalir = new JButton(SALIR);
        botonSalir.setBorder(new EmptyBorder(0 ,0, 0, 0));

        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
        vertical.setFloatable(false);
        vertical.setMargin(new Insets(10,10,10,10));

        JButton botonIniciar = new JButton(INICIAR_SERVIDOR);
        botonIniciar.setBorder(new EmptyBorder(3, 0, 3, 0));
        JButton botonApagar = new JButton(APAGAR_SERVIDOR);
        botonApagar.setBorder(new EmptyBorder(3, 0, 3, 0));

        botonIniciar.addActionListener(this);
        botonApagar.addActionListener(this);
        botonSalir.addActionListener(this);
        
        vertical.add(botonIniciar);
        vertical.add(botonApagar);
        vertical.add(botonSalir);

        textJugadores= new JTextArea();
        textJugadores.setEditable(false);
        
        vServidor.add(vertical, BorderLayout.WEST);

        vServidor.add(textJugadores, BorderLayout.CENTER);

        statusbar = new JLabel(DESCONECTADO);
        statusbar.setPreferredSize(new Dimension(-1, 22));
        statusbar.setBorder(LineBorder.createGrayLineBorder());
        vServidor.add(statusbar, BorderLayout.SOUTH);
        vServidor.setSize(500, 600);
        vServidor.setResizable(false);
        vServidor.setTitle(TITULO);
        vServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vServidor.setLocationRelativeTo(null);
        vServidor.setVisible(true);
    }

    
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            String texto = (String)arg;
            if(arg.equals(CONECTADO)){
                statusbar.setText(CONECTADO);
            }else{
                texto = textJugadores.getText()+"\n"+texto;
                textJugadores.setText(texto);
            }
        }
    }
    
    /**
     * actionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton boton = (JButton) e.getSource();
        if (boton.getText().equals(INICIAR_SERVIDOR)) {
            setChanged();
            notifyObservers(MENSAJE_INICIO);
            setChanged();
            notifyObservers(INICIAR_SERVIDOR);
        } else if (boton.getText().equals(APAGAR_SERVIDOR)) {
            setChanged();
            notifyObservers(APAGAR_SERVIDOR);
        }else if (boton.getText().equals(SALIR)) {
            setChanged();
            notifyObservers(SALIR);
        }
    }
}