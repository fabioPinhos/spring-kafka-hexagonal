package com.example.order.infrastructure.adapter.in.rest;

import com.example.order.application.port.in.CreateOrderUseCase;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderStatus;
import com.example.order.infrastructure.adapter.in.rest.dto.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @Test
    @DisplayName("Deve retornar HTTP 201 Created quando os dados do pedido forem válidos")
    void shouldReturn201WhenOrderIsValid() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest("CLI-123", "Notebook Dell", 1, new BigDecimal("3500.00"));
        Order mockOrder = new Order("ord-123", "CLI-123", "Notebook Dell", 1, new BigDecimal("3500.00"), OrderStatus.PENDING, LocalDateTime.now());

        when(createOrderUseCase.execute(any(), any(), any(), any())).thenReturn(mockOrder);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ord-123"))
                .andExpect(jsonPath("$.customerId").value("CLI-123"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Deve retornar HTTP 400 Bad Request quando os campos forem inválidos")
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        CreateOrderRequest invalidRequest = new CreateOrderRequest("", "", 0, new BigDecimal("-10.00"));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.fieldErrors.customerId").exists())
                .andExpect(jsonPath("$.fieldErrors.product").exists())
                .andExpect(jsonPath("$.fieldErrors.quantity").exists())
                .andExpect(jsonPath("$.fieldErrors.totalAmount").exists());
    }
}
