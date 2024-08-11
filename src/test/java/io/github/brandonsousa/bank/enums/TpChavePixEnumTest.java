package io.github.brandonsousa.bank.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TpChavePixEnumTest {

    @Nested
    @DisplayName("CPF Validation")
    class CpfValidation {
        @Test
        @DisplayName("Valid CPF")
        void validCpf() {
            assertTrue(TpChavePixEnum.CPF.isValidValueToType("83923422083"));
        }

        @Test
        @DisplayName("Invalid CPF")
        void invalidCpf() {
            assertFalse(TpChavePixEnum.CPF.isValidValueToType("123"));
        }
    }

    @Nested
    @DisplayName("CNPJ Validation")
    class CnpjValidation {
        @Test
        @DisplayName("Valid CNPJ")
        void validCnpj() {
            assertTrue(TpChavePixEnum.CNPJ.isValidValueToType("12345678000195"));
        }

        @Test
        @DisplayName("Invalid CNPJ")
        void invalidCnpj() {
            assertFalse(TpChavePixEnum.CNPJ.isValidValueToType("12345678"));
        }
    }

    @Nested
    @DisplayName("Email Validation")
    class EmailValidation {
        @Test
        @DisplayName("Valid Email")
        void validEmail() {
            assertTrue(TpChavePixEnum.EMAIL.isValidValueToType("example@test.com"));
        }

        @Test
        @DisplayName("Invalid Email")
        void invalidEmail() {
            assertFalse(TpChavePixEnum.EMAIL.isValidValueToType("example.com"));
        }
    }

    @Nested
    @DisplayName("Telefone Validation")
    class TelefoneValidation {
        @Test
        @DisplayName("Valid Telefone")
        void validTelefone() {
            assertTrue(TpChavePixEnum.TELEFONE.isValidValueToType("11999999999"));
        }

        @Test
        @DisplayName("Invalid Telefone")
        void invalidTelefone() {
            assertFalse(TpChavePixEnum.TELEFONE.isValidValueToType("12345"));
        }
    }

    @Nested
    @DisplayName("Aleatoria (UUID) Validation")
    class AleatoriaValidation {
        @Test
        @DisplayName("Valid UUID")
        void validUuid() {
            assertTrue(TpChavePixEnum.ALEATORIA.isValidValueToType(
                    UUID.randomUUID().toString()));
        }

        @Test
        @DisplayName("Invalid UUID")
        void invalidUuid() {
            assertFalse(TpChavePixEnum.ALEATORIA.isValidValueToType("not-a-uuid"));
        }
    }
}
