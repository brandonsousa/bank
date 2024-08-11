package io.github.brandonsousa.bank.dvo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DocumentoDVOTest {
    @Nested
    @DisplayName("Constructor tests for DocumentoDVO(String)")
    class DocumentoDVOConstructor {
        @Nested
        @DisplayName("Create with success")
        class Success {
            private static final String validFormattedCpf = "839.234.220-83";
            private static final String validSanitizedCpf = "83923422083";
            private static final String validFormattedCnpj = "92.407.934/0001-60";
            private static final String validSanitizedCnpj = "92407934000160";

            @Test
            @DisplayName("Should create a cpf DocumentoDVO successfully from a masked string")
            void shouldCreateDocumentoDVOWithSuccessFromMaskedString() {
                DocumentoDVO documentoDVO = assertDoesNotThrow(() -> new DocumentoDVO(validFormattedCpf));

                assertEquals(validSanitizedCpf, documentoDVO.value());
            }

            @Test
            @DisplayName("Should create a cnpj DocumentoDVO successfully from a masked string")
            void shouldCreateDocumentoDVOWithSuccessFromMaskedString2() {
                DocumentoDVO documentoDVO = assertDoesNotThrow(() -> new DocumentoDVO(validFormattedCnpj));

                assertEquals(validSanitizedCnpj, documentoDVO.value());
            }

            @Test
            @DisplayName("Should create a cpf DocumentoDVO successfully from an unmasked string")
            void shouldCreateDocumentoDVOWithSuccessFromUnmaskedString() {
                DocumentoDVO documentoDVO = assertDoesNotThrow(() -> new DocumentoDVO(validSanitizedCpf));

                assertEquals(validSanitizedCpf, documentoDVO.value());
            }

            @Test
            @DisplayName("Should create a cnpj DocumentoDVO successfully from an unmasked string")
            void shouldCreateDocumentoDVOWithSuccessFromUnmaskedString2() {
                DocumentoDVO documentoDVO = assertDoesNotThrow(() -> new DocumentoDVO(validSanitizedCnpj));

                assertEquals(validSanitizedCnpj, documentoDVO.value());
            }
        }

        @Nested
        @DisplayName("Fail to create")
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when document is null")
            void shouldThrowNullPointerExceptionWhenDocumentIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> new DocumentoDVO(null));

                String expectedMessage = "Documento é obrigatório";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when document is empty")
            void shouldThrowIllegalArgumentExceptionWhenDocumentIsEmpty() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> new DocumentoDVO(""));

                String expectedMessage = "Documento é obrigatório";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when document is invalid")
            void shouldThrowIllegalArgumentExceptionWhenDocumentIsInvalid() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> new DocumentoDVO("1234567890"));

                String expectedMessage = "Documento inválido";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}