package br.com.exactaworks.exactabank.api.response.util;

import java.util.Objects;

public interface BankAccountUtil {
    static String formatAccountNumber(Integer accountNumber) {
        Objects.requireNonNull(accountNumber, "Conta não pode ser nulo");
        return String.format("%010d", accountNumber);
    }

    static String formatDocument(String document) {
        Objects.requireNonNull(document, "Documento não pode ser nulo");

        if (Objects.equals(document.length(), 11))
            return document.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");

        if (Objects.equals(document.length(), 14))
            return document.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");

        throw new IllegalArgumentException("Documento inválido");
    }
}