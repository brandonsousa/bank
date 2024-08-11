package io.github.brandonsousa.bank.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DocumentHelperTest {
    @Nested
    class IsCpf {
        @Nested
        @DisplayName("With success")
        class Success {
            static Stream<String> provideValidCpfs() {
                return Stream.of("83923422083", "839.234.220-83");
            }

            @DisplayName("Should return true when CPF is valid")
            @ParameterizedTest(name = "{0}")
            @MethodSource("provideValidCpfs")
            void shouldReturnTrueWhenCpfIsValid(String cpf) {
                boolean result = DocumentHelper.isCPF(cpf);

                assertTrue(result, "Expected valid CPF but was invalid: " + cpf);
            }
        }

        @Nested
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when cpf is null")
            void shouldThrowNullPointerExceptionWhenCpfIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> DocumentHelper.isCPF(null));

                String expectedMessage = "CPF não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class IsCnpj {
        @Nested
        @DisplayName("With success")
        class Success {
            static Stream<String> provideValidCnpjs() {
                return Stream.of("92407934000160", "92.407.934/0001-60");
            }

            @DisplayName("Should return true when CNPJ is valid")
            @ParameterizedTest(name = "{0}")
            @MethodSource("provideValidCnpjs")
            void shouldReturnTrueWhenCnpjIsValid(String cnpj) {
                boolean result = DocumentHelper.isCnpj(cnpj);

                assertTrue(result, "Expected valid CNPJ but was invalid: " + cnpj);
            }
        }

        @Nested
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when cnpj is null")
            void shouldThrowNullPointerExceptionWhenCnpjIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> DocumentHelper.isCnpj(null));

                String expectedMessage = "CNPJ não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class Sanitize {
        @Nested
        class Success {
            static Stream<String> providedCpfs() {
                return Stream.of("83923422083", "839.234.220-83");
            }

            @DisplayName("Should return sanitized cpf value")
            @ParameterizedTest(name = "{0}")
            @MethodSource("providedCpfs")
            void shouldReturnSanitizedCpfValue(String value) {
                String result = DocumentHelper.sanitize(value);

                assertEquals("83923422083", result);
            }

            static Stream<String> provideCnpjs() {
                return Stream.of("92407934000160", "92.407.934/0001-60");
            }

            @DisplayName("Should return sanitized cnpj value")
            @ParameterizedTest(name = "{0}")
            @MethodSource("provideCnpjs")
            void shouldReturnSanitizedCnpjValue(String value) {
                String result = DocumentHelper.sanitize(value);

                assertEquals("92407934000160", result);
            }
        }

        @Nested
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when value is null")
            void shouldThrowNullPointerExceptionWhenValueIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> DocumentHelper.sanitize(null));

                String expectedMessage = "Valor não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}