package com.nullbank.model;

public class ContaBonus extends Conta {
    private int pontuacao;

    public ContaBonus(int numero) {
        super(numero);
        this.pontuacao = 10; 
    }

    public void adicionarPontos(int pontos) {
        if (pontos > 0) {
            this.pontuacao += pontos;
        }
    }

    public int getPontuacao() {
        return pontuacao;
    }

    @Override
    public String toString() {
        return "Conta Bônus [numero=%d, saldo=%.2f, pontos=%d]"
                .formatted(getNumero(), getSaldo(), pontuacao);
    }
}