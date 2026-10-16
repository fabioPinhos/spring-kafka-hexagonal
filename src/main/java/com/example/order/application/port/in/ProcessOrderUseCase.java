package com.example.order.application.port.in;

import com.example.order.domain.model.Order;

/**
 * Porta de Entrada (Inbound Port / Use Case):
 * Define o contrato para processar um pedido recebido via mensageria (Kafka).
 */
public interface ProcessOrderUseCase {

    void execute(Order order);
}
