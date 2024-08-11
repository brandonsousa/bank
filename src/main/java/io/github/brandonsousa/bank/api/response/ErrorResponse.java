package io.github.brandonsousa.bank.api.response;

import java.util.List;
import java.util.UUID;

public record ErrorResponse(
        UUID traceId,
        List<String> errors
) {

    public ErrorResponse(String error) {
        this(UUID.randomUUID(), List.of(error));
    }

    public ErrorResponse(List<String> errors) {
        this(UUID.randomUUID(), errors);
    }
}
