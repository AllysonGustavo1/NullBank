package com.nullbank.model;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(int numero) {
        super(numero);
    }

    /**
     * Aplica juros ao saldo atual.
     * Exemplo: Saldo 200, Taxa 10.5% -> Saldo 221.
     * @param taxa taxa percentual (ex: 10.5)
     */
    public void renderJuros(double taxa) {
        if (taxa > 0) {
            double juros = getSaldo() * (taxa / 100);
            creditar(juros);
        }
    }

    @Override
    public String toString() {
        return "Conta Poupança [numero=%d, saldo=%.2f]".formatted(getNumero(), getSaldo());
    }
}