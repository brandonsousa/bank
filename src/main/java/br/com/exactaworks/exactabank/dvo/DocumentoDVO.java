package br.com.exactaworks.exactabank.dvo;

import java.util.Objects;

public record DocumentoDVO(String value) {
    public DocumentoDVO(String value) {
        Objects.requireNonNull(value, "Documento é obrigatório");

        if (value.isBlank()) throw new IllegalArgumentException("Documento é obrigatório");

        final String sanitizedValue = sanitize(value);

        if (!isCPF(sanitizedValue) && !isCnpj(sanitizedValue)) {
            throw new IllegalArgumentException("Documento inválido");
        }

        this.value = sanitizedValue;
    }

    private boolean isCPF(String cpf) {
        if (Objects.isNull(cpf) || !Objects.equals(cpf.length(), 11)) return false;

        for (char c : cpf.toCharArray()) if (!Character.isDigit(c)) return false;

        if (cpf.chars().distinct().count() == 1) return false;

        int sm = 0;
        int weight = 10;
        for (int i = 0; i < 9; i++) {
            sm += (cpf.charAt(i) - '0') * weight;
            weight--;
        }

        int r = 11 - (sm % 11);
        char dig10 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

        sm = 0;
        weight = 11;
        for (int i = 0; i < 10; i++) {
            sm += (cpf.charAt(i) - '0') * weight;
            weight--;
        }

        r = 11 - (sm % 11);
        char dig11 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

        return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
    }

    private boolean isCnpj(String cnpj) {
        return Objects.nonNull(cnpj) && Objects.equals(cnpj.length(), 14);
    }

    private String sanitize(String value) {
        return value.replaceAll("[^0-9]", "");
    }
}
