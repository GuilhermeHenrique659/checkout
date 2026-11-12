# Checkout System

Sistema de checkout desenvolvido em **Java e Spring Boot**, com o objetivo de explorar conceitos de arquitetura de
software, Domain-Driven Design (DDD), processamento de pedidos e comunicação assíncrona baseada em eventos.

O projeto busca aplicar padrões e práticas utilizados em sistemas distribuídos e arquiteturas orientadas a eventos,
mantendo uma separação clara entre domínio, aplicação e infraestrutura.

## Tecnologias

- **Java / Spring Boot** — desenvolvimento da aplicação.
- **Spring Data JPA / Hibernate** — persistência de dados.
- **PostgreSQL** — banco de dados relacional.
- **Flyway** — gerenciamento de migrações.
- **Apache Kafka** — mensageria e comunicação assíncrona.

## Arquitetura

O projeto segue uma abordagem de arquitetura em camadas, com separação de responsabilidades:

- **API:** exposição dos endpoints e tratamento das requisições.
- **Application:** implementação dos casos de uso e orquestração dos fluxos.
- **Domain:** regras de negócio, entidades e conceitos do domínio.
- **Infrastructure:** implementações de persistência, mensageria e integrações externas.

A persistência é isolada do domínio, utilizando modelos específicos para o mapeamento com JPA.

## Conceitos explorados

- **Domain-Driven Design (DDD):** modelagem do domínio através de entidades, agregados e regras de negócio.
- **Event-Driven Architecture:** comunicação entre componentes através de eventos.
- **Publish/Subscribe:** abstração para publicação e consumo de eventos, independente da implementação de mensageria.
- **Transactional Outbox:** estratégia para garantir a consistência entre transações de banco de dados e publicação de
  eventos.
- **Dependency Injection:** inversão de dependências e desacoplamento entre implementações.
- **Repository Pattern:** abstração do acesso e persistência de dados.

## Funcionalidades e fluxos

O sistema tem como foco o processamento do ciclo de vida de pedidos:

1. Criação de pedidos e seus respectivos itens.
2. Persistência dos pedidos utilizando JPA.
3. Processamento de pagamentos através de uma abstração de provedor externo.
4. Publicação de eventos relacionados às operações.
5. Comunicação assíncrona utilizando Kafka.
