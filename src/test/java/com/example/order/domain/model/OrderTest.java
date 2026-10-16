package com.example.order.domain.model;

import com.example.order.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("Deve criar um pedido válido com status PENDING")
    void shouldCreateValidOrder() {
        Order order = Order.createNew("CLI-001", "Notebook Gamer", 1, new BigDecimal("4500.00"));

        assertNotNull(order.getId());
        assertEquals("CLI-001", order.getCustomerId());
        assertEquals("Notebook Gamer", order.getProduct());
        assertEquals(1, order.getQuantity());
        assertEquals(new BigDecimal("4500.00"), order.getTotalAmount());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    @DisplayName("Deve lançar DomainException quando cliente for nulo ou vazio")
    void shouldThrowExceptionWhenCustomerIdIsInvalid() {
        assertThrows(DomainException.class, () ->
                Order.createNew("", "Notebook", 1, new BigDecimal("100.00"))
        );
        assertThrows(DomainException.class, () ->
                Order.createNew(null, "Notebook", 1, new BigDecimal("100.00"))
        );
    }

    @Test
    @DisplayName("Deve lançar DomainException quando produto for inválido")
    void shouldThrowExceptionWhenProductIsInvalid() {
        assertThrows(DomainException.class, () ->
                Order.createNew("CLI-001", "  ", 1, new BigDecimal("100.00"))
        );
    }

    @Test
    @DisplayName("Deve lançar DomainException quando quantidade for menor ou igual a zero")
    void shouldThrowExceptionWhenQuantityIsZeroOrNegative() {
        assertThrows(DomainException.class, () ->
                Order.createNew("CLI-001", "Mouse", 0, new BigDecimal("50.00"))
        );
        assertThrows(DomainException.class, () ->
                Order.createNew("CLI-001", "Mouse", -2, new BigDecimal("50.00"))
        );
    }

    @Test
    @DisplayName("Deve lançar DomainException quando valor for menor ou igual a zero")
    void shouldThrowExceptionWhenTotalAmountIsZeroOrNegative() {
        assertThrows(DomainException.class, () ->
                Order.createNew("CLI-001", "Teclado", 1, BigDecimal.ZERO)
        );
    }

    @Test
    @DisplayName("Deve atualizar status para PROCESSED com sucesso")
    void shouldMarkAsProcessed() {
        Order order = Order.createNew("CLI-001", "Fone de Ouvido", 2, new BigDecimal("200.00"));
        order.markAsProcessed();

        assertEquals(OrderStatus.PROCESSED, order.getStatus());
    }

    @Test
    @DisplayName("Não deve permitir processar pedido cancelado")
    void shouldNotProcessCancelledOrder() {
        Order order = Order.createNew("CLI-001", "Monitor", 1, new BigDecimal("1200.00"));
        order.cancel();

        assertThrows(DomainException.class, order::markAsProcessed);
    }
}
