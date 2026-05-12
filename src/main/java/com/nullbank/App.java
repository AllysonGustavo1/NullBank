package com.nullbank;

import com.nullbank.service.ContaService;
import com.nullbank.ui.NullBankConsole;

/**
 * Ponto de entrada da aplicação NullBank.
 */
public class App {

    public static void main(String[] args) {
        ContaService contaService = new ContaService();
        NullBankConsole console = new NullBankConsole(contaService);
        console.iniciar();
    }
}
