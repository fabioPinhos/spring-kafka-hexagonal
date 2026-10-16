package com.example.order.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Requisição para criação de um novo pedido")
public class CreateOrderRequest {

    @Schema(description = "Identificador do cliente", example = "CLI-99482")
    @NotBlank(message = "O ID do cliente é obrigatório")
    private String customerId;

    @Schema(description = "Descrição do produto", example = "Perfume Malbec Gold 100ml")
    @NotBlank(message = "O produto é obrigatório")
    private String product;

    @Schema(description = "Quantidade de itens", example = "2")
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima deve ser 1")
    private Integer quantity;

    @Schema(description = "Valor total do pedido", example = "259.90")
    @NotNull(message = "O valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero")
    private BigDecimal totalAmount;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String customerId, String product, Integer quantity, BigDecimal totalAmount) {
        this.customerId = customerId;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
