package com.nullbank.api;

import com.nullbank.api.dto.ContaRequest;
import com.nullbank.api.dto.ContaResponse;
import com.nullbank.api.dto.RendimentoRequest;
import com.nullbank.api.dto.TransferenciaRequest;
import com.nullbank.api.dto.ValorRequest;
import com.nullbank.model.Conta;
import com.nullbank.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banco/conta")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaResponse> cadastrarConta(@RequestBody ContaRequest request) {
        Conta conta = switch (request.tipo().toLowerCase()) {
            case "simples" -> contaService.cadastrarConta(request.numero(), request.saldoInicial());
            case "bonus", "bônus" -> contaService.cadastrarContaBonus(request.numero());
            case "poupanca", "poupança" -> contaService.cadastrarContaPoupanca(request.numero(), request.saldoInicial());
            default -> throw new IllegalArgumentException("Tipo de conta inválido.");
        };

        return ResponseEntity.ok(ContaResponse.from(conta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> consultarConta(@PathVariable int id) {
        return ResponseEntity.ok(ContaResponse.from(contaService.consultarConta(id)));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<Double> consultarSaldo(@PathVariable int id) {
        return ResponseEntity.ok(contaService.consultarSaldo(id));
    }

    @PutMapping("/{id}/credito")
    public ResponseEntity<ContaResponse> creditar(
            @PathVariable int id,
            @RequestBody ValorRequest request
    ) {
        contaService.creditar(id, request.valor());
        return ResponseEntity.ok(ContaResponse.from(contaService.consultarConta(id)));
    }

    @PutMapping("/{id}/debito")
    public ResponseEntity<ContaResponse> debitar(
            @PathVariable int id,
            @RequestBody ValorRequest request
    ) {
        contaService.debitar(id, request.valor());
        return ResponseEntity.ok(ContaResponse.from(contaService.consultarConta(id)));
    }

    @PutMapping("/transferencia")
    public ResponseEntity<String> transferir(@RequestBody TransferenciaRequest request) {
        String comprovante = contaService.transferir(request.from(), request.to(), request.amount());
        return ResponseEntity.ok(comprovante);
    }

    @PutMapping("/rendimento")
    public ResponseEntity<String> renderJuros(@RequestBody RendimentoRequest request) {
        contaService.renderJurosGlobal(request.taxa());
        return ResponseEntity.ok("Juros aplicados com sucesso.");
    }
}