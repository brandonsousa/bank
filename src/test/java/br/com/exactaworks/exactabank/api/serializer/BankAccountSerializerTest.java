package br.com.exactaworks.exactabank.api.serializer;

import br.com.exactaworks.exactabank.api.response.util.BankAccountUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class BankAccountSerializerTest {
    @Nested
    class Serialize {
        @Mock
        private JsonGenerator jsonGenerator;
        @Mock
        private SerializerProvider serializerProvider;

        @InjectMocks
        private BankAccountSerializer serializer;

        @Nested
        class Success {
            @Test
            @DisplayName("When input is null Then do nothing")
            void whenInputIsNullThenDoNothing() throws IOException {
                serializer.serialize(null, jsonGenerator, serializerProvider);

                verifyNoInteractions(jsonGenerator);
            }

            @Test
            @DisplayName("When input is not null Then serialize")
            void whenInputIsNotNullThenSerialize() throws IOException {
                int input = 123456;
                String expected = BankAccountUtil.formatAccountNumber(input);

                serializer.serialize(String.valueOf(input), jsonGenerator, serializerProvider);

                ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
                verify(jsonGenerator).writeString(argumentCaptor.capture());

                assertEquals(expected, argumentCaptor.getValue());
            }
        }
    }
}