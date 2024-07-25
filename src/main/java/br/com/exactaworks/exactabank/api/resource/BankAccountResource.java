package br.com.exactaworks.exactabank.api.resource;

import br.com.exactaworks.exactabank.api.mapper.BankAccountMapper;
import br.com.exactaworks.exactabank.api.request.bankAccount.BankAccountStoreRequest;
import br.com.exactaworks.exactabank.api.response.banckAccount.BankAccountStoreResponse;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.service.BankAccountService;
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
}
