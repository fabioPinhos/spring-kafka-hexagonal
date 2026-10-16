package com.example.order.infrastructure.adapter.in.kafka;

import com.example.order.application.port.in.ProcessOrderUseCase;
import com.example.order.domain.model.Order;
import com.example.order.infrastructure.adapter.kafka.dto.OrderEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Adaptador de Entrada (Inbound Adapter):
 * Ouve mensagens do tópico Kafka e encaminha para a porta de entrada (ProcessOrderUseCase).
 */
@Component
public class KafkaOrderConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(KafkaOrderConsumerAdapter.class);

    private final ProcessOrderUseCase processOrderUseCase;

    public KafkaOrderConsumerAdapter(ProcessOrderUseCase processOrderUseCase) {
        this.processOrderUseCase = processOrderUseCase;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.order-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(@Payload OrderEventPayload payload) {
        log.info("Mensagem recebida do tópico Kafka com sucesso: {}", payload);

        try {
            Order order = payload.toDomain();
            processOrderUseCase.execute(order);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem do Kafka: {}", payload, e);
        }
    }
}
