package io.github.brandonsousa.bank.dvo;

import io.github.brandonsousa.bank.helper.DocumentHelper;

import java.util.Objects;


public record DocumentoDVO(String value) {
    public DocumentoDVO(String value) {
        Objects.requireNonNull(value, "Documento é obrigatório");

        if (value.isBlank()) throw new IllegalArgumentException("Documento é obrigatório");

        final String sanitizedValue = sanitize(value);

        if (!DocumentHelper.isCPF(sanitizedValue) && !DocumentHelper.isCnpj(sanitizedValue)) {
            throw new IllegalArgumentException("Documento inválido");
        }

        this.value = sanitizedValue;
    }

    private String sanitize(String value) {
        return value.replaceAll("[^0-9]", "");
    }
}
