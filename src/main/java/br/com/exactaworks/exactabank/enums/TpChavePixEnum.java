package br.com.exactaworks.exactabank.enums;

import br.com.exactaworks.exactabank.helper.DocumentHelper;

import java.util.UUID;
import java.util.regex.Pattern;

public enum TpChavePixEnum {
    CPF {
        @Override
        public boolean isValidValueToType(String value) {
            return DocumentHelper.isCPF(value);
        }
    },
    CNPJ {
        @Override
        public boolean isValidValueToType(String value) {
            return DocumentHelper.isCnpj(value);
        }
    },
    EMAIL {
        @Override
        public boolean isValidValueToType(String value) {
            return value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        }
    },
    TELEFONE {
        private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");

        @Override
        public boolean isValidValueToType(String value) {
            String cleaned = value.replaceAll("[^0-9]", "");
            return PHONE_PATTERN.matcher(cleaned).matches() && cleaned.length() == 11;
        }
    },
    ALEATORIA {
        @Override
        public boolean isValidValueToType(String value) {
            try {
                UUID.fromString(value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    };

    public abstract boolean isValidValueToType(String value);
}
