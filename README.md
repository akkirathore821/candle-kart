# CandleKart – Event-Driven E-Commerce Backend
CandleKart is a backend-focused, production-oriented e-commerce platform built using Spring Boot microservices and Apache Kafka. The project demonstrates real-world backend engineering practices such as event-driven architecture, distributed transactions (Saga pattern), and eventual consistency.

## Key Features

<ul style="list-style-type:disc">
  <li>Microservices Architecture with independent services for Auth, User, Product, Inventory, Cart, Order, Payment, Search, and Notification</li>
  <li>JWT-based Authentication & Authorization with API Gateway–level security</li>
  <li>Event-Driven Order Processing using Apache Kafka</li>
  <li>Saga (Choreography) Pattern for handling inventory reservation and payment workflows</li>
  <li>SKU-based Inventory Management to prevent overselling</li>
  <li>Redis-based Cart Service for fast cart operations</li>
  <li>Elasticsearch Search Service for product search and filtering</li>
  <li>Database-per-Service pattern using PostgreSQL</li>
</ul>

## Architecture Overview

<ul style="list-style-type:disc">
  <li>Synchronous Communication: REST (Feign Client)</li>
  <li>Asynchronous Communication: Kafka events</li>
  <li>Persistence: PostgreSQL (per service)</li>
  <li>Caching & Cart: Redis</li>
  <li>Search: Elasticsearch</li>
  <li>Security: JWT, Spring Security</li>
  <li>Containerization: Docker (Kubernetes-ready)</li>
</ul>

## Tech Stack

<ul style="list-style-type:disc">
  <li>Java 17</li>
  <li>Spring Boot</li>
  <li>Spring Security (JWT)</li>
  <li>Apache Kafka</li>
  <li>PostgreSQL</li>
  <li>Redis</li>
  <li>Elasticsearch</li>
  <li>Docker</li>
  <li>REST APIs</li>
</ul>
