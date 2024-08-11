package io.github.brandonsousa.bank.api.resource;

import io.github.brandonsousa.bank.api.mapper.TransactionMapper;
import io.github.brandonsousa.bank.api.request.transaction.DepositStoreRequest;
import io.github.brandonsousa.bank.api.request.transaction.PixStoreRequest;
import io.github.brandonsousa.bank.api.request.transaction.RechargeStoreRequest;
import io.github.brandonsousa.bank.api.request.transaction.WithdrawStoreRequest;
import io.github.brandonsousa.bank.api.response.ErrorResponse;
import io.github.brandonsousa.bank.api.response.transaction.DepositStoreResponse;
import io.github.brandonsousa.bank.api.response.transaction.PixStoreResponse;
import io.github.brandonsousa.bank.api.response.transaction.RechargeStoreResponse;
import io.github.brandonsousa.bank.api.response.transaction.TransactionResponse;
import io.github.brandonsousa.bank.api.response.transaction.WithdrawStoreResponse;
import io.github.brandonsousa.bank.entity.TransacaoEntity;
import io.github.brandonsousa.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Operation(summary = "Transações", description = "Lista todas as transações por conta")
    @GetMapping
    public ResponseEntity<PagedModel<TransactionResponse>> list(
            @Parameter(description = "Número da página") @RequestParam(required = false, defaultValue = "0")
            Integer pagina,
            @Parameter(description = "Quantidade de registros por página")
            @RequestParam(required = false, defaultValue = "10") Integer tamanho,
            @Parameter(description = "Número da conta") @RequestParam Integer conta) {
        Page<TransacaoEntity> transactions = service.findAllByConta(conta, pagina, tamanho);

        PagedModel<TransactionResponse> response = mapper.fromTransacaoPage(transactions, conta);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Recarga de celular", description = "Recarrega um celular", responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation =
                    RechargeStoreResponse.class))),
            @ApiResponse(description = "Resposta para todos os casos de error", content = @Content(schema =
            @Schema(implementation = ErrorResponse.class))),
    })
    @Transactional
    @PostMapping("/recharge")
    public ResponseEntity<RechargeStoreResponse> recharge(@RequestBody @Valid RechargeStoreRequest request) {
        TransacaoEntity entity = service.recharge(request.contaOrigem(), request.numeroTelefone(), request.valor());
        RechargeStoreResponse response = mapper.fromTransacaoEntityRecharge(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
