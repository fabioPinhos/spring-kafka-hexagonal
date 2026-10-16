package com.example.order.application.usecase;

import com.example.order.application.port.in.CreateOrderUseCase;
import com.example.order.application.port.out.OrderProducerPort;
import com.example.order.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Caso de Uso (Application Service):
 * Coordena a criação do pedido no domínio e notifica as portas de saída.
 */
@Service
public class CreateOrderService implements CreateOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderService.class);

    private final OrderProducerPort orderProducerPort;

    public CreateOrderService(OrderProducerPort orderProducerPort) {
        this.orderProducerPort = orderProducerPort;
    }

    @Override
    public Order execute(String customerId, String product, Integer quantity, BigDecimal totalAmount) {
        log.info("Iniciando criação do pedido para o cliente: {}", customerId);

        // 1. Cria a entidade de domínio (validações de negócio ocorrem dentro do modelo)
        Order order = Order.createNew(customerId, product, quantity, totalAmount);

        // 2. Notifica a porta de saída (publicador Kafka)
        orderProducerPort.sendOrderCreatedEvent(order);

        log.info("Pedido criado e evento enviado com sucesso: ID {}", order.getId());
        return order;
    }
}
