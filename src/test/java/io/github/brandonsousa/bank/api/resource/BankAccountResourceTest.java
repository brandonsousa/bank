package io.github.brandonsousa.bank.api.resource;


import io.github.brandonsousa.bank.api.mapper.BankAccountMapper;
import io.github.brandonsousa.bank.api.request.bankAccount.BankAccountStoreRequest;
import io.github.brandonsousa.bank.api.response.banckAccount.BankAccountStoreResponse;
import io.github.brandonsousa.bank.entity.ContaEntity;
import io.github.brandonsousa.bank.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Random;

import static org.mockito.Mockito.when;

@WebMvcTest(BankAccountResource.class)
class BankAccountResourceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankAccountService service;

    @MockBean
    private BankAccountMapper mapper;

    @Nested
    @DisplayName("/api/bank-accounts")
    class Store {
        @Nested
        class Success {
            @Test
            @DisplayName("When store bank account")
            void storeBankAccount() throws Exception {
                BankAccountStoreRequest request = new BankAccountStoreRequest("John Doe", "12345678901");
                ContaEntity entity = ContaEntity.builder()
                        .conta(new Random().nextInt())
                        .nome(request.name())
                        .documento(request.document())
                        .saldo(BigDecimal.ZERO).build();
                BankAccountStoreResponse response = BankAccountMapper.INSTANCE.fromContaEntity(entity);

                when(service.store(request.name(), request.document())).thenReturn(entity);
                when(mapper.fromContaEntity(entity)).thenReturn(response);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/bank-accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.nome")
                                .value(response.nome()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.documento")
                                .value(response.documento()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.conta")
                                .value(response.conta()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.saldo")
                                .value(response.saldo()))
                        .andDo(MockMvcResultHandlers.print());
            }
        }

        @Nested
        @DisplayName("Should fail when store bank account with invalid data")
        class Fail {
            @Test
            @DisplayName("Name is empty")
            void storeBankAccountWithEmptyName() throws Exception {
                BankAccountStoreRequest invalidRequest = new BankAccountStoreRequest("", "12345678901");

                mockMvc.perform(MockMvcRequestBuilders.post("/api/bank-accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("O campo 'nome' é obrigatório"))
                        .andDo(MockMvcResultHandlers.print());
            }

            @Test
            @DisplayName("Name is null")
            void storeBankAccountWithNullName() throws Exception {
                BankAccountStoreRequest invalidRequest = new BankAccountStoreRequest(null, "12345678901");

                mockMvc.perform(MockMvcRequestBuilders.post("/api/bank-accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("O campo 'nome' é obrigatório"))
                        .andDo(MockMvcResultHandlers.print());
            }

            @Test
            @DisplayName("Document is empty")
            void storeBankAccountWithEmptyDocument() throws Exception {
                BankAccountStoreRequest invalidRequest = new BankAccountStoreRequest("John Doe", "");

                mockMvc.perform(MockMvcRequestBuilders.post("/api/bank-accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                                .value("O campo 'documento' é obrigatório"))
                        .andDo(MockMvcResultHandlers.print());
            }

            @Test
            @DisplayName("Document is null")
            void storeBankAccountWithNullDocument() throws Exception {
                BankAccountStoreRequest invalidRequest = new BankAccountStoreRequest("John Doe", null);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/bank-accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                                .value("O campo 'documento' é obrigatório"))
                        .andDo(MockMvcResultHandlers.print());
            }
        }
    }
}