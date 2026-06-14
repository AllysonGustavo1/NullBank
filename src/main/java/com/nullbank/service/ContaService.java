package com.nullbank.service;

import com.nullbank.model.Conta;
import com.nullbank.model.ContaBonus;
import com.nullbank.model.ContaPoupanca;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Camada de negócio responsável pelas operações bancárias.
 *
 * <p>
 * Toda a lógica de negócio do sistema fica centralizada nesta camada,
 * mantendo a camada de UI livre de regras.
 * </p>
 *
 * <p>
 * Utiliza {@link LinkedHashMap} para armazenamento das contas,
 * garantindo busca O(1) por número e preservando a ordem de inserção.
 * </p>
 */
@Service
public class ContaService {

    private final Map<Integer, Conta> contas = new LinkedHashMap<>();

    /**
     * Cadastra uma nova conta com o número informado e saldo zero.
     *
     * @param numero número da conta a ser cadastrada
     * @return a conta cadastrada
     * @throws IllegalArgumentException se já existir conta com o número informado
     */
    public Conta cadastrarConta(int numero, double saldoInicial) {
        if (contas.containsKey(numero)) {
            throw new IllegalArgumentException("Já existe uma conta com o número %d.".formatted(numero));
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        
        var conta = new Conta(numero);
        
        // Aplica o saldo inicial na criação
        if (saldoInicial > 0) {
            conta.creditar(saldoInicial);
        }
        
        contas.put(numero, conta);
        return conta;
    }

    public Conta cadastrarContaBonus(int numero) {
        if (contas.containsKey(numero)) {
            throw new IllegalArgumentException("Já existe uma conta com o número %d.".formatted(numero));
        }
        var conta = new ContaBonus(numero);
        contas.put(numero, conta);
        return conta;
    }

    public Conta cadastrarContaPoupanca(int numero, double saldoInicial) {
        if (contas.containsKey(numero)) {
            throw new IllegalArgumentException("Já existe uma conta com o número %d.".formatted(numero));
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        var conta = new ContaPoupanca(numero);
        if (saldoInicial > 0) {
            conta.creditar(saldoInicial);
        }
        contas.put(numero, conta);
        return conta;
    }

    /**
     * Consulta o saldo de uma conta pelo número.
     *
     * @param numero número da conta
     * @return o saldo da conta
     * @throws IllegalArgumentException se a conta não for encontrada
     */
    public double consultarSaldo(int numero) {
        return buscarContaObrigatoria(numero).getSaldo();
    }

    /**
     * Realiza operação de crédito (acrescenta valor ao saldo).
     *
     * @param numero número da conta
     * @param valor  valor a ser creditado
     * @throws IllegalArgumentException se a conta não for encontrada ou valor inválido
     */
    public void creditar(int numero, double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de crédito deve ser maior que zero.");
        }
      Conta conta = buscarContaObrigatoria(numero);
      conta.creditar(valor);

      if (conta instanceof ContaBonus cb) {
          int pontosGanhos = (int) (valor / 100);
          cb.adicionarPontos(pontosGanhos);
    }
}

    /**
     * Realiza operação de débito (subtrai valor do saldo).
     * Não permite saldo negativo.
     *
     * @param numero número da conta
     * @param valor  valor a ser debitado
     * @throws IllegalArgumentException se a conta não for encontrada ou valor inválido
     * @throws IllegalStateException se não houver saldo suficiente
     */
    public void debitar(int numero, double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor de débito deve ser maior que zero.");
        }
        buscarContaObrigatoria(numero).debitar(valor);
    }

    /**
     * Realiza transferência entre duas contas.
     * Debita da conta de origem e credita na conta de destino.
     * Não permite saldo negativo na conta de origem.
     *
     * @param numeroOrigem  número da conta de origem
     * @param numeroDestino número da conta de destino
     * @param valor         valor a ser transferido
     * @throws IllegalArgumentException se alguma conta não for encontrada ou valor inválido
     * @throws IllegalStateException se não houver saldo suficiente
     */
    public String transferir(int numeroOrigem, int numeroDestino, double valor) {
        if (numeroOrigem == numeroDestino) {
            throw new IllegalArgumentException("Conta de origem e destino não podem ser iguais.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }

        var origem = buscarContaObrigatoria(numeroOrigem);
        var destino = buscarContaObrigatoria(numeroDestino);

        origem.debitar(valor);
        destino.creditar(valor);

        if (destino instanceof ContaBonus cb) {
            int pontosGanhos = (int) (valor / 150);
            cb.adicionarPontos(pontosGanhos);
        }

        return """
               ========================================
                     COMPROVANTE DE TRANSFERÊNCIA
               ========================================
               Origem:  %d
               Destino: %d
               Valor:   R$ %.2f
               ========================================\
               """.formatted(numeroOrigem, numeroDestino, valor);
    }

    /**
     * Busca uma conta pelo número.
     *
     * @param numero número da conta
     * @return {@link Optional} contendo a conta, ou vazio se não existir
     */
    public Optional<Conta> buscarConta(int numero) {
        return Optional.ofNullable(contas.get(numero));
    }

    /**
     * Retorna uma visão não modificável de todas as contas cadastradas.
     *
     * @return mapa imutável de contas
     */
    public Map<Integer, Conta> listarContas() {
        return Collections.unmodifiableMap(contas);
    }

    private Conta buscarContaObrigatoria(int numero) {
        return buscarConta(numero)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Conta com número %d não encontrada.".formatted(numero)));
    }

    public void renderJurosGlobal(double taxa) {
        boolean houveRendimento = false;
        for (Conta conta : contas.values()) {
            if (conta instanceof ContaPoupanca cp) {
                cp.renderJuros(taxa);
                houveRendimento = true;
            }
        }
        if (!houveRendimento) {
            throw new IllegalStateException("Não existem contas poupança cadastradas para render juros.");
        }
    }

    public Conta consultarConta(int numero) {
        return buscarContaObrigatoria(numero);
    }
}
