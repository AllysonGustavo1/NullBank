package com.nullbank.ui;

import com.nullbank.service.ContaService;

import java.util.Scanner;

/**
 * Camada de interação com o usuário via console.
 *
 * <p>
 * Esta classe <strong>NÃO</strong> contém lógica de negócio — apenas
 * captura entradas e exibe resultados, delegando todas as operações
 * para a camada {@link ContaService}.
 * </p>
 */
public class NullBankConsole {

    private static final String MENU = """
            ========================================
                        NULLBANK - Menu
            ========================================
              1 - Cadastrar Conta
              2 - Consultar Saldo
              3 - Crédito
              4 - Débito
              5 - Transferência
              6 - Render Juros
              0 - Sair
            ========================================\
            """;

    private final ContaService contaService;
    private final Scanner scanner;

    public NullBankConsole(ContaService contaService) {
        this.contaService = contaService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia o loop principal do menu interativo.
     */
    public void iniciar() {
        var opcao = -1;

        while (opcao != 0) {
            System.out.println(MENU);
            opcao = lerInteiro("Opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarConta();
                    case 2 -> consultarSaldo();
                    case 3 -> creditar();
                    case 4 -> debitar();
                    case 5 -> transferir();
                    case 6 -> renderJuros();
                    case 0 -> System.out.println("Encerrando o NullBank. Até logo!");
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }

            if (opcao != 0) {
                aguardarEnter();
                
                if (opcao < 1 || opcao > 5) {
                    System.out.print("\n".repeat(20));
                }
            }
        }

        scanner.close();
    }

    private void cadastrarConta() {
        int tipo = -1;

        while (tipo != 0 && tipo != 1 && tipo != 2 && tipo != 3) {
            System.out.println("\n--- TIPO DE CONTA ---");
            System.out.println("1 - Simples");
            System.out.println("2 - Bônus");
            System.out.println("3 - Poupança");
            System.out.println("0 - Voltar ao menu principal");
            tipo = lerInteiro("Escolha o tipo desejado: ");

            if (tipo == 0) {
                System.out.println("Operação de cadastro cancelada. Voltando ao menu...");
                return; // O return interrompe o método imediatamente e volta para o menu inicial
            } else if (tipo != 1 && tipo != 2) {
                System.out.println("Erro: Opção inválida. Por favor, digite 1, 2 ou 0 para cancelar.");
            }
        }

        var numero = lerInteiro("Informe o número da conta: ");

        if (tipo == 1) {
            contaService.cadastrarConta(numero);
            System.out.println("Conta Simples %d cadastrada com sucesso!".formatted(numero));
        } else if (tipo == 2) {
            contaService.cadastrarContaBonus(numero);
            System.out.println("Conta Bônus %d cadastrada com 10 pontos iniciais!".formatted(numero));
        }
        else if (tipo == 3) {
            contaService.cadastrarContaPoupanca(numero);
            System.out.println("Conta Poupança %d cadastrada com sucesso!".formatted(numero));
        }
    }
    private void consultarSaldo() {
        var numero = lerInteiro("Informe o número da conta: ");
        var saldo = contaService.consultarSaldo(numero);
        System.out.println("Saldo da conta %d: R$ %.2f".formatted(numero, saldo));
    }

    private void creditar() {
        var numero = lerInteiro("Informe o número da conta: ");
        var valor = lerDouble("Informe o valor do crédito: ");
        contaService.creditar(numero, valor);
        System.out.println("Crédito de R$ %.2f realizado com sucesso!".formatted(valor));
    }

    private void debitar() {
        var numero = lerInteiro("Informe o número da conta: ");
        var valor = lerDouble("Informe o valor do débito: ");
        contaService.debitar(numero, valor);
        System.out.println("Débito de R$ %.2f realizado com sucesso!".formatted(valor));
    }

    private void transferir() {
        var origem = lerInteiro("Informe o número da conta de origem: ");
        var destino = lerInteiro("Informe o número da conta de destino: ");
        var valor = lerDouble("Informe o valor da transferência: ");
        String comprovante = contaService.transferir(origem, destino, valor);
        System.out.println("\n" + comprovante);
    }

    private void renderJuros() {
        var taxa = lerDouble("Informe a taxa de juros (%): ");
        contaService.renderJurosGlobal(taxa);
        System.out.println("Juros de %.2f%% aplicados a todas as contas poupança!".formatted(taxa));
    }

    private int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Informe um número inteiro.");
            scanner.next();
            System.out.print(mensagem);
        }
        int valor = scanner.nextInt();  //limpeza de buffer
        scanner.nextLine();
        return valor;
    }

    private double lerDouble(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextDouble()) {
            System.out.println("Entrada inválida. Informe um valor numérico.");
            scanner.next();
            System.out.print(mensagem);
        }
        double valor = scanner.nextDouble();  //limpeza de buffer
        scanner.nextLine();
        return valor;
    }

    private void aguardarEnter() {
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}
