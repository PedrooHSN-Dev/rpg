package ACZG_RPG.model;

import java.util.Random;

public class Monstro {
    private final String nome;
    private int pontosVida;
    private final int pontosVidaMaxima;
    private final int quantidadeSorteiosTurno;
    private final int numeroSecreto;

    public Monstro(String nome, int pontosVida, int quantidadeSorteiosTurno) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.pontosVidaMaxima = pontosVida;
        this.quantidadeSorteiosTurno = quantidadeSorteiosTurno;
        this.numeroSecreto = new Random().nextInt(pontosVida) + 1;
    }

    public String getNome() {
        return nome;
    }

    public int getPontosVida() {
        return pontosVida;
    }

    public int getPontosVidaMaxima() {
        return pontosVidaMaxima;
    }

    public int getQuantidadeSorteiosTurno() {
        return quantidadeSorteiosTurno;
    }

    public int getNumeroSecreto() {
        return numeroSecreto;
    }

    public boolean isVivo() {
        return pontosVida > 0;
    }

    public void receberDano(int dano) {
        this.pontosVida = Math.max(0, this.pontosVida - dano);
    }

    public void curar(int cura) {
        this.pontosVida = Math.min(this.pontosVidaMaxima, this.pontosVida + cura);
    }

    public void adicionarVidaMaxima(int valor) {
        this.pontosVida += valor;
    }
}