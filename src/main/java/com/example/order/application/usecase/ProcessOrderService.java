package com.example.order.application.usecase;

import com.example.order.application.port.in.ProcessOrderUseCase;
import com.example.order.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso (Application Service):
 * Executa as regras de processamento quando um pedido é recebido do Kafka.
 */
@Service
public class ProcessOrderService implements ProcessOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessOrderService.class);

    @Override
    public void execute(Order order) {
        log.info("Processando pedido recebido do Kafka: ID {}", order.getId());

        // Atualiza o estado da entidade segundo as regras de domínio
        order.markAsProcessed();

        log.info("Pedido ID {} processado com sucesso. Status atual: {}", order.getId(), order.getStatus());
    }
}
