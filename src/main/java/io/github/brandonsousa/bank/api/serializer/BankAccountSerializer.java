package io.github.brandonsousa.bank.api.serializer;

import io.github.brandonsousa.bank.api.response.util.BankAccountUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Objects;

public class BankAccountSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (Objects.isNull(s)) return;

        jsonGenerator.writeString(BankAccountUtil.formatAccountNumber(Integer.parseInt(s)));
    }
}
