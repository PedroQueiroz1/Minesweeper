package br.com.noname.cm.visao;

import br.com.noname.cm.modelo.Campo;
import br.com.noname.cm.modelo.CampoEvento;
import br.com.noname.cm.modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton implements CampoObservador, MouseListener {
    private final Color BG_PADRAO = new Color(184,184,184);
    private final Color BG_MARCAR = new Color(8,179,247);
    private final Color BG_EXPLODIR = new Color(189,66,68);
    private final Color TEXTO_VERDE = new Color(0,100,0);
    private Campo campo;

    public BotaoCampo(Campo campo) {
        this.campo = campo;
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        campo.registrarObservador(this);
        addMouseListener(this);
    }

    @Override
    public void eventoOcorreu(Campo c, CampoEvento e) {
        switch(e){
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            default:
                aplicarEstiloPadrao();
        }

    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.white);
        setText("X");
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
        setBorder(BorderFactory.createBevelBorder(0));
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCAR);
        setForeground(Color.black);
        setText("M");
    }

    private void aplicarEstiloAbrir() {

        setBorder(BorderFactory.createLineBorder(Color.GRAY ));
        if(campo.isMinado()){
            setBackground(BG_EXPLODIR);
            setText("X");
            return;
        }

        setBackground(BG_PADRAO);
        SwingUtilities.invokeLater(()->{
        switch(campo.minasNaVizinhanca()){
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                setForeground(Color.blue);
                break;
            case 3:
                setForeground(Color.yellow);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.red);
                break;
            default:
                setForeground(Color.pink);
        }
        });
        String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
        setText(valor);
    }



    // EVENTOS DO MOUSE \/
    //                  \/


    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    if(mouseEvent.getButton()==1){
        campo.abrir();
    }else {
        campo.alternarMarcacao();
    }
    }


    public void mouseReleased(MouseEvent mouseEvent) {}
    public void mouseEntered(MouseEvent mouseEvent) {}
    public void mouseClicked(MouseEvent mouseEvent) {}
    public void mouseExited(MouseEvent mouseEvent) {}
}
