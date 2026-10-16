package com.example.order.infrastructure.adapter.kafka.dto;

import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que trafega no tópico Kafka como JSON.
 * Isola os detalhes de serialização/versão da entidade do domínio.
 */
public class OrderEventPayload {

    private String id;
    private String customerId;
    private String product;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String status;
    private String createdAt;

    public OrderEventPayload() {
    }

    public OrderEventPayload(String id, String customerId, String product, Integer quantity, BigDecimal totalAmount, String status, String createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderEventPayload fromDomain(Order order) {
        return new OrderEventPayload(
                order.getId(),
                order.getCustomerId(),
                order.getProduct(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt().toString()
        );
    }

    public Order toDomain() {
        return new Order(
                this.id,
                this.customerId,
                this.product,
                this.quantity,
                this.totalAmount,
                OrderStatus.valueOf(this.status),
                LocalDateTime.parse(this.createdAt)
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderEventPayload{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
