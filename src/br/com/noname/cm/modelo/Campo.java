package br.com.noname.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
    private final int linha;  //final -> n pode ter o valor alterado depois de atribuído.
    private final int coluna;

    private boolean minado; //false -> padrão
    private boolean aberto;
    private boolean marcado;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    public Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        this.minado = false;
        this.aberto = false;
        this.marcado = false;
    }

    public void registrarObservador(CampoObservador obs){
        observadores.add(obs);
    }

    private void notificarObservadores(CampoEvento e){
        observadores.stream().forEach(o->o.eventoOcorreu(this, e));
    }

    boolean adicionarVizinho(Campo vizinho){
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        if(deltaGeral == 1 && !diagonal){
            vizinhos.add(vizinho);
            return true;
        }else if(deltaGeral == 2 && diagonal){
            vizinhos.add(vizinho);
            return true;
        }else {
            return false;
        }
    }
    public void alternarMarcacao(){
        if(!aberto){
            marcado = !marcado;
        if(marcado){
            notificarObservadores(CampoEvento.MARCAR);
        }else{
            notificarObservadores(CampoEvento.DESMARCAR);
        }
        }
    }

    public boolean abrir() {
        if (!aberto && !marcado) {
            setAberto(true);
            if (minado) {
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }
            notificarObservadores(CampoEvento.ABRIR);
            if (vizinhancaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    void minar(){
        minado=true;
    }

    public boolean vizinhancaSegura(){
        return vizinhos.stream().noneMatch(v->v.minado);
    }

    boolean objetivoAlcancado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }
    void reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }
    public int minasNaVizinhanca(){
        return (int) vizinhos.stream().filter(v->v.minado).count();
    }


    public boolean isMarcado(){
        return marcado;
    }
//    public boolean isDesmarcado(){return !marcado;}
//    public boolean isAberto(){
//        return aberto;
//    }
//    public boolean isFechado(){
//        return !isAberto();
//    }
    public boolean isMinado(){return minado;}

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
        if(aberto){
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

    public int getLinha() {
        return this.linha;
    }

    public int getColuna() {
        return this.coluna;
    }
}
