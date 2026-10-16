package com.example.order.domain.model;

import com.example.order.domain.exception.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de Domínio representando um Pedido (Order).
 * Mantém-se agnóstica de frameworks e tecnologias de infraestrutura (Spring, Kafka, JPA, etc.).
 */
public class Order {

    private final String id;
    private final String customerId;
    private final String product;
    private final Integer quantity;
    private final BigDecimal totalAmount;
    private OrderStatus status;
    private final LocalDateTime createdAt;

    public Order(String id, String customerId, String product, Integer quantity, BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Order createNew(String customerId, String product, Integer quantity, BigDecimal totalAmount) {
        if (customerId == null || customerId.isBlank()) {
            throw new DomainException("O ID do cliente não pode ser vazio.");
        }
        if (product == null || product.isBlank()) {
            throw new DomainException("A descrição do produto não pode ser vazia.");
        }
        if (quantity == null || quantity <= 0) {
            throw new DomainException("A quantidade deve ser maior que zero.");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("O valor total deve ser maior que zero.");
        }

        return new Order(
                UUID.randomUUID().toString(),
                customerId,
                product,
                quantity,
                totalAmount,
                OrderStatus.PENDING,
                LocalDateTime.now()
        );
    }

    public void markAsProcessed() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new DomainException("Um pedido cancelado não pode ser processado.");
        }
        this.status = OrderStatus.PROCESSED;
    }

    public void cancel() {
        if (this.status == OrderStatus.PROCESSED) {
            throw new DomainException("Não é possível cancelar um pedido que já foi processado.");
        }
        this.status = OrderStatus.CANCELLED;
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

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
