package br.com.exactaworks.exactabank.api.resource;

import br.com.exactaworks.exactabank.api.mapper.TransactionMapper;
import br.com.exactaworks.exactabank.api.request.transaction.DepositStoreRequest;
import br.com.exactaworks.exactabank.api.request.transaction.PixStoreRequest;
import br.com.exactaworks.exactabank.api.request.transaction.WithdrawStoreRequest;
import br.com.exactaworks.exactabank.api.response.transaction.DepositStoreResponse;
import br.com.exactaworks.exactabank.api.response.transaction.PixStoreResponse;
import br.com.exactaworks.exactabank.api.response.transaction.WithdrawStoreResponse;
import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import br.com.exactaworks.exactabank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "API para manipulação de transações")
public class TransactionResource {
    private final TransactionService service;
    private final TransactionMapper mapper;

    @Operation(summary = "Depositar", description = "Deposita um valor em uma conta bancária")
    @Transactional
    @PostMapping("/deposit")
    public ResponseEntity<DepositStoreResponse> deposit(@RequestBody @Valid DepositStoreRequest request) {
        TransacaoEntity entity = service.deposit(request.idConta(), request.valor());
        DepositStoreResponse response = mapper.fromTransacaoEntity(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Pix", description = "Transfere um valor entre contas bancárias via pix")
    @Transactional
    @PostMapping("/pix")
    public ResponseEntity<PixStoreResponse> pix(@RequestBody @Valid PixStoreRequest request) {
        TransacaoEntity entity = service.pix(request.contaOrigem(), request.tipoChavePix(), request.chavePixDestino(),
                request.valor());
        PixStoreResponse response = mapper.fromTransacaoEntityPix(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Sacar", description = "Saca um valor de uma conta bancária")
    @Transactional
    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawStoreResponse> withdraw(@RequestBody @Valid WithdrawStoreRequest request) {
        TransacaoEntity entity = service.withdraw(request.contaOrigem(), request.valor());
        WithdrawStoreResponse response = mapper.fromTransacaoEntityWithdraw(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
