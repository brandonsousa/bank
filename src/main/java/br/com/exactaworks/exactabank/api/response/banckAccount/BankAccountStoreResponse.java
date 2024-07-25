package br.com.exactaworks.exactabank.api.response.banckAccount;

import br.com.exactaworks.exactabank.api.response.util.BankAccountUtil;

import java.math.BigDecimal;
import java.util.Objects;

public record BankAccountStoreResponse(
        String nome,
        String documento,
        String conta,
        BigDecimal saldo) {
    public BankAccountStoreResponse(String nome, String documento, String conta, BigDecimal saldo) {
        Objects.requireNonNull(nome, "O campo 'nome' é obrigatório");
        Objects.requireNonNull(documento, "O campo 'documento' é obrigatório");
        Objects.requireNonNull(conta, "O campo 'conta' é obrigatório");
        Objects.requireNonNull(saldo, "O campo 'saldo' é obrigatório");

        this.nome = nome;
        this.documento = BankAccountUtil.formatDocument(documento);
        this.conta = BankAccountUtil.formatAccountNumber(Integer.parseInt(conta));
        this.saldo = saldo;
    }
}
