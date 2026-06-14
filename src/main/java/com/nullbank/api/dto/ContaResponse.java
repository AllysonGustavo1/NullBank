package com.nullbank.api.dto;

import com.nullbank.model.Conta;
import com.nullbank.model.ContaBonus;
import com.nullbank.model.ContaPoupanca;

public record ContaResponse(String tipo, int numero, double saldo, Integer bonus) {

    public static ContaResponse from(Conta conta) {
        if (conta instanceof ContaBonus contaBonus) {
            return new ContaResponse(
                    "Bônus",
                    contaBonus.getNumero(),
                    contaBonus.getSaldo(),
                    contaBonus.getPontuacao()
            );
        }

        if (conta instanceof ContaPoupanca) {
            return new ContaResponse(
                    "Poupança",
                    conta.getNumero(),
                    conta.getSaldo(),
                    null
            );
        }

        return new ContaResponse(
                "Simples",
                conta.getNumero(),
                conta.getSaldo(),
                null
        );
    }
}