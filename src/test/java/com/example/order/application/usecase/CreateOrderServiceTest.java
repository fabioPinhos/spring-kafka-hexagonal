package com.example.order.application.usecase;

import com.example.order.application.port.out.OrderProducerPort;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    private OrderProducerPort orderProducerPort;

    @InjectMocks
    private CreateOrderService createOrderService;

    @Test
    @DisplayName("Deve criar pedido e notificar a porta de saída do Kafka")
    void shouldCreateOrderAndPublishToPort() {
        String customerId = "CLI-12345";
        String product = "Smartphone 5G";
        Integer quantity = 1;
        BigDecimal totalAmount = new BigDecimal("2999.00");

        Order createdOrder = createOrderService.execute(customerId, product, quantity, totalAmount);

        assertNotNull(createdOrder);
        assertEquals(customerId, createdOrder.getCustomerId());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderProducerPort, times(1)).sendOrderCreatedEvent(orderCaptor.capture());

        Order publishedOrder = orderCaptor.getValue();
        assertEquals(createdOrder.getId(), publishedOrder.getId());
        assertEquals(customerId, publishedOrder.getCustomerId());
    }
}
