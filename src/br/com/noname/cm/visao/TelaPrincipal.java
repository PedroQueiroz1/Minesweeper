package br.com.noname.cm.visao;

import br.com.noname.cm.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        var tabuleiro = new Tabuleiro(16,30, 50);
        add(new PainelTabuleiro(tabuleiro));

        this.setLocationByPlatform(true);
        setTitle("Campo Minado");
        setSize(690,425);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TelaPrincipal();
    }
}
