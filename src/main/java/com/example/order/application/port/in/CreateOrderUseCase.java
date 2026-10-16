package com.example.order.application.port.in;

import com.example.order.domain.model.Order;

import java.math.BigDecimal;

/**
 * Porta de Entrada (Inbound Port / Use Case):
 * Define o contrato para a criação de um novo pedido.
 */
public interface CreateOrderUseCase {

    Order execute(String customerId, String product, Integer quantity, BigDecimal totalAmount);
}
