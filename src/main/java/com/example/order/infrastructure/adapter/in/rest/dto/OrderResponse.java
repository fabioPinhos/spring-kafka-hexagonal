package com.example.order.infrastructure.adapter.in.rest.dto;

import com.example.order.domain.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Resposta com dados do pedido criado")
public class OrderResponse {

    @Schema(description = "Identificador único do pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Identificador do cliente", example = "CLI-99482")
    private String customerId;

    @Schema(description = "Descrição do produto", example = "Perfume Malbec Gold 100ml")
    private String product;

    @Schema(description = "Quantidade de itens", example = "2")
    private Integer quantity;

    @Schema(description = "Valor total do pedido", example = "259.90")
    private BigDecimal totalAmount;

    @Schema(description = "Status do pedido", example = "PENDING")
    private String status;

    @Schema(description = "Data e hora de criação", example = "2026-10-15T09:30:00")
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(String id, String customerId, String product, Integer quantity, BigDecimal totalAmount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderResponse fromDomain(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getProduct(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
