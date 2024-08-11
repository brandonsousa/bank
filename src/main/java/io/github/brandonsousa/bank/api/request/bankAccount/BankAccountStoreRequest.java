package io.github.brandonsousa.bank.api.request.bankAccount;

import jakarta.validation.constraints.NotBlank;

public record BankAccountStoreRequest(
        @NotBlank(message = "O campo 'nome' é obrigatório") String name,
        @NotBlank(message = "O campo 'documento' é obrigatório") String document) {
}
