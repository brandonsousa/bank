package br.com.exactaworks.exactabank.api.resource;

import br.com.exactaworks.exactabank.api.mapper.BankAccountMapper;
import br.com.exactaworks.exactabank.api.request.bankAccount.BankAccountStoreRequest;
import br.com.exactaworks.exactabank.api.response.banckAccount.BankAccountListResponse;
import br.com.exactaworks.exactabank.api.response.banckAccount.BankAccountStoreResponse;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor
@Tag(name = "Bank Account", description = "API para manipulação de contas bancárias")
public class BankAccountResource {
    private final BankAccountService service;
    private final BankAccountMapper mapper;

    @Operation(
            summary = "Cria  conta",
            description = "Cria uma conta bancária, única por cpf/cnpj, pode usar o 4devs para pegar um documento " +
                    "válido"
    )
    @Transactional
    @PostMapping
    public ResponseEntity<BankAccountStoreResponse> store(@RequestBody @Valid BankAccountStoreRequest request) {
        ContaEntity bankAccount = service.store(request.name(), request.document());
        BankAccountStoreResponse response = mapper.fromContaEntity(bankAccount);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Lista todas as contas",
            description = "Lista todas as contas bancárias"
    )
    @GetMapping
    public ResponseEntity<PagedModel<BankAccountListResponse>> list(
            @Parameter(description = "Número da página") @RequestParam(required = false, defaultValue = "0")
            Integer pagina,
            @Parameter(description = "Quantidade de registros por página")
            @RequestParam(required = false, defaultValue = "10") Integer tamanho,
            @Parameter(description = "Filtro para busca(nome, documento ou conta)") @RequestParam(required = false)
            String filtro
    ) {
        Page<ContaEntity> bankAccounts = service.findAll(pagina, tamanho, filtro);
        PagedModel<BankAccountListResponse> response = mapper.fromContaPage(bankAccounts);
        return ResponseEntity.ok(response);
    }
}
