package com.nullbank.model;

/**
 * Entidade que representa uma conta bancária.
 *
 * <p>
 * Encapsula os atributos número e saldo, expondo operações de domínio
 * ({@link #creditar} e {@link #debitar}) em vez de setters genéricos,
 * seguindo o princípio Tell, Don't Ask.
 * </p>
 */
public class Conta {

    private final int numero;
    private double saldo;

    /**
     * Cria uma nova conta com saldo inicial zero.
     *
     * @param numero identificador único da conta
     */
    public Conta(int numero) {
        this.numero = numero;
        this.saldo = 0.0;
    }

    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    /**
     * Acrescenta um valor ao saldo da conta.
     *
     * @param valor valor a ser creditado (deve ser positivo)
     * @throws IllegalArgumentException se o valor não for positivo
     */
    public void creditar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de crédito deve ser positivo.");
        }
        this.saldo += valor;
    }

    /**
     * Subtrai um valor do saldo da conta.
     * A conta pode ficar com saldo negativo conforme regra de negócio.
     *
     * @param valor valor a ser debitado (deve ser positivo)
     * @throws IllegalArgumentException se o valor não for positivo
     */
    public void debitar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de débito deve ser positivo.");
        }
        this.saldo -= valor;
    }

    @Override
    public String toString() {
        return "Conta [numero=%d, saldo=%.2f]".formatted(numero, saldo);
    }
}
