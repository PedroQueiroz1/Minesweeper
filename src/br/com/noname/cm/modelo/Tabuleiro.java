package br.com.noname.cm.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
    private final int linhas;
    private final int colunas;
    private final int minas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        this.gerarCampos();
        this.associarVizinhos();
        this.sortearMinas();
    }
    public void registrarObservador(Consumer<ResultadoEvento> observador){
        observadores.add(observador);
    }
    private void notificarObservadores(boolean res){
        observadores.stream().forEach(o->o.accept(new ResultadoEvento(res)));
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    private void gerarCampos(){
        for(int l=0; l<linhas; l++){
            for(int c=0; c<colunas; c++){
                Campo campo = new Campo(l, c);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }
    }
//    public void abrir2(int linha, int coluna){
//           campos.parallelStream()
//                   .filter(c->c.getLinha() == linha && c.getColuna() == coluna)
//                   .findFirst()
//                   .ifPresent(c->c.abrir());
//    }

    public void paraCada(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }
//    public void alternarMarcacao(int linha, int coluna){
//        campos.parallelStream()
//                .filter(c->c.getLinha() == linha && c.getColuna() == coluna)
//                .findFirst()
//                .ifPresent(c->c.alternarMarcacao());
//    }
    private void associarVizinhos(){
        for(Campo c1:campos) {
            for(Campo c2: campos){
                c1.adicionarVizinho(c2);
            }
        }
        }
    private void sortearMinas(){
        long minasArmadas=0;
        Predicate<Campo> minado = c -> c.isMinado();
        do{
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();
        }while(minasArmadas<minas);
    }

    public boolean objetivoAlcancado2(){
        Predicate<Campo> campo = c-> c.objetivoAlcancado();
        return campos.stream().allMatch(campo);
    }
    public void reiniciar(){
        campos.stream().forEach(c->c.reiniciar());
        sortearMinas();
    }

    @Override
    public void eventoOcorreu(Campo c, CampoEvento e) {
        if(e==CampoEvento.EXPLODIR){
            this.mostrarMinas();
            notificarObservadores(false);
        }else if(objetivoAlcancado2()){
            notificarObservadores(true);
        }
    }
    private void mostrarMinas(){
        campos.stream().filter(c->c.isMinado())
                .filter(c->!c.isMarcado())
                .forEach(c -> c.setAberto(true));
    }
}
