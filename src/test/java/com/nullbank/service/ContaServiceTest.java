package com.nullbank.service;

import com.nullbank.model.Conta;
import com.nullbank.model.ContaBonus;
import com.nullbank.model.ContaPoupanca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários das operações do banco (camada de negócio).
 *
 * <p>
 * Conforme a especificação, os testes exercitam diretamente a {@link ContaService},
 * sem passar pela camada da API REST. A instância é criada com {@code new ContaService()}
 * (sem contexto Spring), pois toda a lógica de negócio é independente do framework.
 * </p>
 */
class ContaServiceTest {

    private ContaService service;

    @BeforeEach
    void setUp() {
        service = new ContaService();
    }

    // ------------------------------------------------------------------
    // Cadastrar Conta - diferentes testes para cada tipo de conta
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Cadastrar Conta")
    class CadastrarConta {

        @Test
        @DisplayName("Conta Simples é cadastrada com o saldo inicial informado")
        void cadastraContaSimples() {
            Conta conta = service.cadastrarConta(1, 100.0);

            assertEquals(Conta.class, conta.getClass());
            assertEquals(1, conta.getNumero());
            assertEquals(100.0, conta.getSaldo());
        }

        @Test
        @DisplayName("Conta Bônus é cadastrada com saldo zero e 10 pontos iniciais")
        void cadastraContaBonus() {
            ContaBonus conta = (ContaBonus) service.cadastrarContaBonus(2);

            assertEquals(2, conta.getNumero());
            assertEquals(0.0, conta.getSaldo());
            assertEquals(10, conta.getPontuacao());
        }

        @Test
        @DisplayName("Conta Poupança é cadastrada com o saldo inicial informado")
        void cadastraContaPoupanca() {
            Conta conta = service.cadastrarContaPoupanca(3, 50.0);

            assertEquals(ContaPoupanca.class, conta.getClass());
            assertEquals(3, conta.getNumero());
            assertEquals(50.0, conta.getSaldo());
        }

        @Test
        @DisplayName("Não permite cadastrar conta com número já existente")
        void naoPermiteNumeroDuplicado() {
            service.cadastrarConta(1, 0.0);

            assertThrows(IllegalArgumentException.class,
                    () -> service.cadastrarContaBonus(1));
        }

        @Test
        @DisplayName("Não permite saldo inicial negativo")
        void naoPermiteSaldoInicialNegativo() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.cadastrarConta(1, -10.0));
        }
    }

    // ------------------------------------------------------------------
    // Consultar Conta - diferentes testes para cada tipo de conta
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Consultar Conta")
    class ConsultarConta {

        @Test
        @DisplayName("Consulta uma Conta Simples pelo número")
        void consultaContaSimples() {
            service.cadastrarConta(1, 100.0);

            Conta conta = service.consultarConta(1);

            assertEquals(Conta.class, conta.getClass());
            assertEquals(1, conta.getNumero());
            assertEquals(100.0, conta.getSaldo());
        }

        @Test
        @DisplayName("Consulta uma Conta Bônus pelo número")
        void consultaContaBonus() {
            service.cadastrarContaBonus(2);

            Conta conta = service.consultarConta(2);

            assertInstanceOf(ContaBonus.class, conta);
            assertEquals(10, ((ContaBonus) conta).getPontuacao());
        }

        @Test
        @DisplayName("Consulta uma Conta Poupança pelo número")
        void consultaContaPoupanca() {
            service.cadastrarContaPoupanca(3, 50.0);

            Conta conta = service.consultarConta(3);

            assertInstanceOf(ContaPoupanca.class, conta);
            assertEquals(50.0, conta.getSaldo());
        }

        @Test
        @DisplayName("Consultar conta inexistente lança exceção")
        void consultaContaInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.consultarConta(999));
        }
    }

    // ------------------------------------------------------------------
    // Consultar Saldo
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Consultar Saldo retorna o saldo atual da conta")
    void consultarSaldo() {
        service.cadastrarConta(1, 250.0);

        assertEquals(250.0, service.consultarSaldo(1));
    }

    // ------------------------------------------------------------------
    // Crédito
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Crédito")
    class Credito {

        @Test
        @DisplayName("Caso normal: crédito acrescenta o valor ao saldo")
        void creditoCasoNormal() {
            service.cadastrarConta(1, 100.0);

            service.creditar(1, 50.0);

            assertEquals(150.0, service.consultarSaldo(1));
        }

        @Test
        @DisplayName("Não permite valor negativo")
        void creditoValorNegativo() {
            service.cadastrarConta(1, 100.0);

            assertThrows(IllegalArgumentException.class,
                    () -> service.creditar(1, -10.0));
        }

        @Test
        @DisplayName("Bonificação: conta Bônus ganha 1 ponto a cada R$100 creditados")
        void creditoBonificacaoContaBonus() {
            service.cadastrarContaBonus(1); // inicia com 10 pontos

            service.creditar(1, 250.0); // 250 / 100 = 2 pontos

            ContaBonus conta = (ContaBonus) service.consultarConta(1);
            assertEquals(250.0, conta.getSaldo());
            assertEquals(12, conta.getPontuacao());
        }
    }

    // ------------------------------------------------------------------
    // Débito
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Débito")
    class Debito {

        @Test
        @DisplayName("Caso normal: débito subtrai o valor do saldo")
        void debitoCasoNormal() {
            service.cadastrarConta(1, 100.0);

            service.debitar(1, 40.0);

            assertEquals(60.0, service.consultarSaldo(1));
        }

        @Test
        @DisplayName("Não permite valor negativo")
        void debitoValorNegativo() {
            service.cadastrarConta(1, 100.0);

            assertThrows(IllegalArgumentException.class,
                    () -> service.debitar(1, -10.0));
        }

        @Test
        @DisplayName("Não permite o saldo ficar negativo (Conta Poupança, limite 0)")
        void debitoNaoPermiteSaldoNegativo() {
            service.cadastrarContaPoupanca(1, 100.0);

            assertThrows(IllegalArgumentException.class,
                    () -> service.debitar(1, 150.0));
            assertEquals(100.0, service.consultarSaldo(1)); // saldo permanece intacto
        }
    }

    // ------------------------------------------------------------------
    // Transferência
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Transferência")
    class Transferencia {

        @Test
        @DisplayName("Não permite valor negativo")
        void transferenciaValorNegativo() {
            service.cadastrarConta(1, 100.0);
            service.cadastrarConta(2, 0.0);

            assertThrows(IllegalArgumentException.class,
                    () -> service.transferir(1, 2, -10.0));
        }

        @Test
        @DisplayName("Não permite a conta de origem ficar com saldo negativo")
        void transferenciaNaoPermiteSaldoNegativo() {
            service.cadastrarContaPoupanca(1, 100.0); // origem, limite 0
            service.cadastrarConta(2, 0.0);            // destino

            assertThrows(IllegalArgumentException.class,
                    () -> service.transferir(1, 2, 150.0));
            assertEquals(100.0, service.consultarSaldo(1)); // origem intacta
            assertEquals(0.0, service.consultarSaldo(2));   // destino não recebeu
        }

        @Test
        @DisplayName("Bonificação: conta Bônus de destino ganha 1 ponto a cada R$150 recebidos")
        void transferenciaBonificacaoContaBonus() {
            service.cadastrarConta(1, 300.0);  // origem
            service.cadastrarContaBonus(2);    // destino, inicia com 10 pontos

            service.transferir(1, 2, 300.0); // 300 / 150 = 2 pontos

            ContaBonus destino = (ContaBonus) service.consultarConta(2);
            assertEquals(0.0, service.consultarSaldo(1));
            assertEquals(300.0, destino.getSaldo());
            assertEquals(12, destino.getPontuacao());
        }
    }

    // ------------------------------------------------------------------
    // Render Juros
    // ------------------------------------------------------------------
    @Nested
    @DisplayName("Render Juros")
    class RenderJuros {

        @Test
        @DisplayName("Aplica rendimento corretamente em todas as contas Poupança")
        void renderJurosEmTodasAsPoupancas() {
            service.cadastrarContaPoupanca(1, 100.0);
            service.cadastrarContaPoupanca(2, 200.0);

            service.renderJurosGlobal(10.0); // 10%

            assertEquals(110.0, service.consultarSaldo(1));
            assertEquals(220.0, service.consultarSaldo(2));
        }

        @Test
        @DisplayName("Não aplica rendimento em contas que não são Poupança")
        void renderJurosNaoAfetaOutrosTipos() {
            service.cadastrarContaPoupanca(1, 100.0);
            service.cadastrarConta(2, 100.0);   // simples
            service.cadastrarContaBonus(3);     // bônus

            service.renderJurosGlobal(10.0);

            assertEquals(110.0, service.consultarSaldo(1)); // poupança rendeu
            assertEquals(100.0, service.consultarSaldo(2)); // simples inalterada
            assertEquals(0.0, service.consultarSaldo(3));   // bônus inalterada
        }

        @Test
        @DisplayName("Lança exceção quando não há contas Poupança cadastradas")
        void renderJurosSemPoupanca() {
            service.cadastrarConta(1, 100.0);

            assertThrows(IllegalStateException.class,
                    () -> service.renderJurosGlobal(10.0));
        }
    }
}
