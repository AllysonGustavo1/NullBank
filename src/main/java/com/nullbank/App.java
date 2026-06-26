package com.nullbank;

import com.nullbank.service.ContaService;
import com.nullbank.ui.NullBankConsole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        var context = SpringApplication.run(App.class, args);

        boolean modoConsole = Boolean.getBoolean("nullbank.console")
                || Arrays.asList(args).contains("--console");

        if (modoConsole) {
            ContaService contaService = context.getBean(ContaService.class);
            NullBankConsole console = new NullBankConsole(contaService);
            console.iniciar();
        }
    }
}