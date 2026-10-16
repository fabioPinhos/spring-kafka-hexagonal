package com.example.order.infrastructure.adapter.in.rest;

import com.example.order.application.port.in.CreateOrderUseCase;
import com.example.order.domain.model.Order;
import com.example.order.infrastructure.adapter.in.rest.dto.CreateOrderRequest;
import com.example.order.infrastructure.adapter.in.rest.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador de Entrada Primário (Driving Adapter / REST Controller):
 * Expõe a API REST para clientes externos e invoca a porta de entrada CreateOrderUseCase.
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Endpoints para gerenciamento de pedidos e mensageria Kafka")
public class OrderRestController {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderRestController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Criar um novo pedido",
            description = "Recebe os dados do pedido, valida as regras de domínio e despacha um evento para o Kafka."
    )
    @ApiResponse(responseCode = "201", description = "Pedido criado e evento publicado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou violação de regra de negócio")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = createOrderUseCase.execute(
                request.getCustomerId(),
                request.getProduct(),
                request.getQuantity(),
                request.getTotalAmount()
        );

        OrderResponse response = OrderResponse.fromDomain(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
