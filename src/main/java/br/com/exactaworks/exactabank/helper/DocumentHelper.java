package br.com.exactaworks.exactabank.helper;

import java.util.Objects;

public interface DocumentHelper {
    static boolean isCPF(String cpf) {
        Objects.requireNonNull(cpf, "CPF não pode ser nulo");

        cpf = sanitize(cpf);

        if (!Objects.equals(cpf.length(), 11)) return false;

        for (char c : cpf.toCharArray()) if (!Character.isDigit(c)) return false;

        if (cpf.chars().distinct().count() == 1) return false;

        int sm = 0;
        int weight = 10;
        for (int i = 0; i < 9; i++) {
            sm += (cpf.charAt(i) - '0') * weight;
            weight--;
        }

        int r = 11 - (sm % 11);
        char dig10 = (Objects.equals(r, 10) || Objects.equals(r, 11)) ? '0' : (char) (r + '0');

        sm = 0;
        weight = 11;
        for (int i = 0; i < 10; i++) {
            sm += (cpf.charAt(i) - '0') * weight;
            weight--;
        }

        r = 11 - (sm % 11);
        char dig11 = (Objects.equals(r, 10) || Objects.equals(r, 11)) ? '0' : (char) (r + '0');

        return (Objects.equals(dig10, cpf.charAt(9))) && (Objects.equals(dig11, cpf.charAt(10)));
    }

    static boolean isCnpj(String cnpj) {
        Objects.requireNonNull(cnpj, "CNPJ não pode ser nulo");

        cnpj = sanitize(cnpj);
        return Objects.equals(cnpj.length(), 14);
    }

    static String sanitize(String value) {
        Objects.requireNonNull(value, "Valor não pode ser nulo");
        return value.replaceAll("[^0-9]", "");
    }
}
