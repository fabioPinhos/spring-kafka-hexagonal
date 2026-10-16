# Spring Boot + Apache Kafka com Arquitetura Hexagonal (Ports and Adapters)

Este projeto demonstra a criação e estruturação de uma aplicação **Java 17** e **Spring Boot 3** integrada ao **Apache Kafka** em execução via **Docker Compose**, utilizando os princípios de **Arquitetura Hexagonal** (também conhecida como *Ports and Adapters*) com **Observabilidade completa (Spring Boot Actuator, Prometheus e Grafana)**.

---

## 🏛️ Estrutura e Conceitos da Arquitetura Hexagonal

O objetivo principal da arquitetura hexagonal é **desacoplar as regras de negócio (Domínio)** de detalhes externos e de tecnologia (frameworks, bancos de dados, mensageria, APIs web).

```
                                  +------------------------------------+
                                  |            HEXÁGONO                |
                                  |                                    |
   [ HTTP REST Client ]           |   +----------------------------+   |
            │                     |   |        APLICAÇÃO           |   |
            ▼                     |   |                            |   |
  +───────────────────+           |   |  +----------------------+  |   |
  |  Inbound Adapter  | ───────►  |   |  |        DOMÍNIO       |  |   |
  |  (REST Controller)|  Porta de |   |  |                      |  |   |
  +───────────────────+  Entrada  |   |  | • Entidades          |  |   |
                         (Inbound |   |  | • Regras de Negócio  |  |   |
                          Port)   |   |  +----------------------+  |   |
                                  |   |             │              |   |
                                  |   |             ▼              |   |
  +───────────────────+           |   |          Porta de          |   |          +───────────────────+
  |  Inbound Adapter  | ───────►  |   |           Saída            |   | ───────► | Outbound Adapter  | ──► [ Kafka Topic ]
  |  (Kafka Consumer) |  Porta de |   |       (Outbound Port)      |   |          | (Kafka Producer)  |
  +───────────────────+  Entrada  |   +----------------------------+   |          +───────────────────+
            ▲                     +------------------------------------+                    │
            │                                                                               │
            └───────────────────────────────────────────────────────────────────────────────┘
```

### 1. Camada de Domínio (`domain`)
- **Independente de frameworks**: Não possui anotações de Spring, Kafka ou JPA.
- Contém as entidades (`Order`), enums (`OrderStatus`) e exceções de negócio (`DomainException`).
- Toda regra de negócio e invariante é protegida dentro da entidade.

### 2. Camada de Aplicação (`application`)
- **Portas de Entrada (`port.in`)**:
  - `CreateOrderUseCase`: Define a operação para criar pedidos.
  - `ProcessOrderUseCase`: Define a operação para processar pedidos recebidos.
- **Portas de Saída (`port.out`)**:
  - `OrderProducerPort`: Interface que define o contrato de publicação de eventos para mensageria.
- **Serviços / Casos de Uso (`usecase`)**:
  - `CreateOrderService`: Implementa `CreateOrderUseCase`, orquestra a criação no domínio e dispara o evento na porta de saída.
  - `ProcessOrderService`: Implementa `ProcessOrderUseCase`, executando regras de negócio ao receber o pedido do Kafka.

### 3. Camada de Infraestrutura (`infrastructure`)
- **Adaptadores de Entrada (`adapter.in`)**:
  - `rest/OrderRestController`: API REST que recebe chamadas HTTP e aciona `CreateOrderUseCase`.
  - `kafka/KafkaOrderConsumerAdapter`: `@KafkaListener` que consome do tópico Kafka e chama `ProcessOrderUseCase`.
- **Adaptadores de Saída (`adapter.out`)**:
  - `kafka/KafkaOrderProducerAdapter`: Implementação concreta de `OrderProducerPort` que utiliza `KafkaTemplate` para enviar mensagens JSON para o Kafka.
- **Configuração (`config`)**:
  - `KafkaTopicConfig`: Criação automática dos tópicos no Kafka.
  - `OpenApiConfig`: Documentação Swagger / OpenAPI 3.

---

## 📊 Observabilidade & Monitoramento

A aplicação conta com métricas completas integradas via **Spring Boot Actuator** e exportadas para o **Prometheus**, com dashboards visualizáveis em tempo real no **Grafana**:

- **Spring Boot Actuator & Healthcheck**: [http://localhost:8080/actuator](http://localhost:8080/actuator) e [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Scrape Prometheus endpoint**: [http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)
- **Prometheus UI**: [http://localhost:9090](http://localhost:9090)
- **Grafana Dashboard**: [http://localhost:3000](http://localhost:3000) (Login: `admin` / Senha: `admin`)
  - Dashboard automático provisionado: *"Spring Boot & Kafka - Observability Dashboard"*
  - Métricas inclusas: Uptime, Consumo de CPU (Processo e Sistema), Memória JVM Heap/Non-Heap, Throughput de requisições HTTP, Percentis de Latência (p95, p99), Taxa de Erros HTTP, Taxas de Envio/Consumo de Mensagens Kafka.

---

## 📁 Estrutura de Pastas

```
spring-kafka-hexagonal/
├── .mvn/wrapper/                  # Maven Wrapper
├── docker-compose.yml             # Subida do Kafka KRaft, Kafka UI, Prometheus e Grafana
├── Dockerfile                     # Imagem Docker da aplicação
├── mvnw / mvnw.cmd                # Scripts Maven Wrapper
├── pom.xml                        # Dependências do projeto (Spring Boot, Actuator, Micrometer, Kafka, etc.)
├── prometheus/
│   └── prometheus.yml             # Configuração de scrape do Prometheus
├── grafana/
│   └── provisioning/
│       ├── datasources/
│       │   └── prometheus.yml     # Datasource automático do Prometheus no Grafana
│       └── dashboards/
│           ├── dashboards.yml     # Provedor de dashboards
│           └── spring-kafka-dashboard.json # Dashboard pré-configurado
└── src/
    ├── main/
    │   ├── java/com/example/order/
    │   │   ├── OrderApplication.java
    │   │   ├── domain/
    │   │   │   ├── exception/
    │   │   │   │   └── DomainException.java
    │   │   │   └── model/
    │   │   │       ├── Order.java
    │   │   │       └── OrderStatus.java
    │   │   ├── application/
    │   │   │   ├── port/
    │   │   │   │   ├── in/
    │   │   │   │   │   ├── CreateOrderUseCase.java
    │   │   │   │   │   └── ProcessOrderUseCase.java
    │   │   │   │   └── out/
    │   │   │   │       └── OrderProducerPort.java
    │   │   │   └── usecase/
    │   │   │       ├── CreateOrderService.java
    │   │   │       └── ProcessOrderService.java
    │   │   └── infrastructure/
    │   │       ├── adapter/
    │   │       │   ├── in/
    │   │       │   │   ├── rest/
    │   │       │   │   │   ├── OrderRestController.java
    │   │       │   │   │   ├── GlobalExceptionHandler.java
    │   │       │   │   │   └── dto/
    │   │       │   │   │       ├── CreateOrderRequest.java
    │   │       │   │   │       └── OrderResponse.java
    │   │       │   │   └── kafka/
    │   │       │   │       └── KafkaOrderConsumerAdapter.java
    │   │       │   └── out/
    │   │       │       └── kafka/
    │   │       │           ├── KafkaOrderProducerAdapter.java
    │   │       │           └── dto/
    │   │       │               └── OrderEventPayload.java
    │   │       └── config/
    │   │           ├── KafkaTopicConfig.java
    │   │           └── OpenApiConfig.java
    │   └── resources/
    │       └── application.yml
    └── test/
        └── java/com/example/order/
            ├── domain/model/OrderTest.java
            ├── application/usecase/CreateOrderServiceTest.java
            └── infrastructure/adapter/in/rest/OrderRestControllerTest.java
```

---

## 🚀 Como Executar

### Pré-requisitos
- **Java 17** ou superior instalado.
- **Docker** e **Docker Compose** instalados e em execução.

---

### Passo 1: Subir a Infraestrutura (Kafka, Kafka UI, Prometheus, Grafana) via Docker
Na raiz do projeto (`spring-kafka-hexagonal`):

```bash
docker compose up -d
```

Este comando inicia:
- **Apache Kafka (KRaft mode)** na porta `9092` (sem dependência do Zookeeper).
- **Kafka UI** na porta `8085` 👉 **http://localhost:8085**
- **Prometheus** na porta `9090` 👉 **http://localhost:9090**
- **Grafana** na porta `3000` 👉 **http://localhost:3000** (user: `admin` / pass: `admin`)

Para verificar se os contêineres estão rodando:
```bash
docker ps
```

---

### Passo 2: Executar os Testes Automatizados
Para rodar todos os testes unitários e de integração:

```bash
# No Windows PowerShell:
.\mvnw.cmd test

# No Linux/Mac:
./mvnw test
```

---

### Passo 3: Iniciar a Aplicação Spring Boot

```bash
# No Windows PowerShell:
.\mvnw.cmd spring-boot:run

# No Linux/Mac:
./mvnw spring-boot:run
```

A aplicação subirá na porta `8080`.

---

## 🧪 Como Testar a Aplicação

### 1. Documentação Interativa (Swagger UI)
Acesse no seu navegador:
👉 **http://localhost:8080/swagger-ui.html**

Você verá o endpoint `POST /api/v1/orders` disponível para teste direto pela interface web.

---

### 2. Enviando uma requisição via cURL / PowerShell

```bash
curl -X POST http://localhost:8080/api/v1/orders `
  -H "Content-Type: application/json" `
  -d '{
    "customerId": "CLI-99482",
    "product": "Perfume Malbec Gold 100ml",
    "quantity": 2,
    "totalAmount": 259.90
  }'
```

**Resposta esperada (HTTP 201 Created):**
```json
{
  "id": "e8d641d3-35f9-4673-8eb1-4ce8b9a19c1e",
  "customerId": "CLI-99482",
  "product": "Perfume Malbec Gold 100ml",
  "quantity": 2,
  "totalAmount": 259.90,
  "status": "PENDING",
  "createdAt": "2026-10-15T09:30:15.123456"
}
```

---

### 3. Acompanhando nos Dashboards

1. **No Kafka UI** (http://localhost:8085):
   - Abra o cluster **Local-Kafka** -> **Topics** -> `order-events-topic`.
   - Na aba **Messages**, veja o evento trafegado em tempo real.
2. **No Prometheus** (http://localhost:9090):
   - Consulte métricas como `http_server_requests_seconds_count` ou `process_uptime_seconds`.
3. **No Grafana** (http://localhost:3000):
   - Faça login com `admin` / `admin`.
   - Acesse **Dashboards** -> **Spring Boot & Kafka - Observability Dashboard** para ver gráficos de CPU, JVM, requisições HTTP e taxas de envio/consumo Kafka.

---

## 🛑 Como Parar os Serviços Docker

```bash
docker compose down
```
Se desejar remover também os volumes de dados do Kafka/Grafana:
```bash
docker compose down -v
```
