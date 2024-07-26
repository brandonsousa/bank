package br.com.exactaworks.exactabank.api.resource;

import br.com.exactaworks.exactabank.api.mapper.PixMapper;
import br.com.exactaworks.exactabank.api.request.pix.PixStoreRequest;
import br.com.exactaworks.exactabank.api.response.pix.PixStoreResponse;
import br.com.exactaworks.exactabank.entity.ChavePixEntity;
import br.com.exactaworks.exactabank.service.PixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pix")
@RequiredArgsConstructor
@Tag(name = "Pix", description = "API para manipulação de chaves pix")
public class PixResource {
    private final PixService service;
    private final PixMapper mapper;

    @Operation(
            summary = "Cria chave pix",
            description = "Cria uma chave pix para uma conta bancária"
    )
    @Transactional
    @PostMapping
    public ResponseEntity<PixStoreResponse> store(@RequestBody @Valid PixStoreRequest request) {
        ChavePixEntity entity = service.store(request.idConta(), request.tpChave(), request.chave());
        PixStoreResponse response = mapper.fromChavePixEntity(entity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
