# 🕯️ CandleKart – Event-Driven E-Commerce Backend
CandleKart is a backend-focused, production-oriented e-commerce platform built using Spring Boot microservices and Apache Kafka. The project demonstrates real-world backend engineering practices such as event-driven architecture, distributed transactions (Saga pattern), and eventual consistency.

## 🚀 Key Features

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

## 🧩 Architecture Overview

<ul style="list-style-type:disc">
  <li>Synchronous Communication: REST (Feign Client)</li>
  <li>Asynchronous Communication: Kafka events</li>
  <li>Persistence: PostgreSQL (per service)</li>
  <li>Caching & Cart: Redis</li>
  <li>Search: Elasticsearch</li>
  <li>Security: JWT, Spring Security</li>
  <li>Containerization: Docker (Kubernetes-ready)</li>
</ul>

## 🛠️Tech Stack

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

## 📘 API Documentation

CandleKart exposes RESTful APIs for each microservice.
All external requests are routed through the API Gateway, which handles authentication and routing.

### 🔐 Authentication:
All protected APIs require a valid JWT token in the Authorization header:
  ```
    Authorization: Bearer <JWT_TOKEN>
  ```

### 🔐 Auth Service

| Method | Endpoint         | Description                   |
| ------ | ---------------- | ----------------------------- |
| POST   | `/auth/register` | Register a new user           |
| POST   | `/auth/login`    | Authenticate user & issue JWT |
| POST   | `/auth/refresh`  | Refresh access token          |
| POST   | `/auth/validate` | Validate JWT (internal use)   |

### 👤 User Service

| Method | Endpoint                         | Description          | Access        |
| ------ | -------------------------------- | -------------------- | ------------- |
| POST   | `/api/users`                     | Create user profile  | Internal      |
| GET    | `/api/users/{id}`                | Get user by ID       | Authenticated |
| GET    | `/api/users/username/{username}` | Get user by username | Authenticated |
| PUT    | `/api/users/update`              | Update user profile  | Authenticated |
| DELETE | `/api/users/{id}`                | Delete user          | Admin         |
| GET    | `/api/users/all`                 | List all users       | Admin         |

### 📦 Product / Catalog Service

| Method | Endpoint                    | Description                         | Access |
| ------ | --------------------------- | ----------------------------------- | ------ |
| GET    | `/api/products`             | List products (filters, pagination) | Public |
| GET    | `/api/products/{sku}`       | Get product by SKU                  | Public |
| POST   | `/api/admin/products`       | Create product                      | Admin  |
| PUT    | `/api/admin/products/{sku}` | Update product (partial/full)       | Admin  |
| DELETE | `/api/admin/products/{sku}` | Delete product                      | Admin  |

### 📊 Inventory Service

| Method | Endpoint                 | Description             | Access   |
| ------ | ------------------------ | ----------------------- | -------- |
| POST   | `/api/inventory/add`     | Add stock for SKU       | Internal |
| POST   | `/api/inventory/reserve` | Reserve stock for order | Kafka    |
| POST   | `/api/inventory/release` | Release reserved stock  | Kafka    |
| GET    | `/api/inventory/{sku}`   | Get stock details       | Internal |

### 🛒 Cart Service (Redis)

| Method | Endpoint                | Description                  | Access        |
| ------ | ----------------------- | ---------------------------- | ------------- |
| GET    | `/api/cart`             | Get user cart                | Authenticated |
| POST   | `/api/cart/items`       | Add item to cart             | Authenticated |
| PUT    | `/api/cart/items`       | Update item quantity         | Authenticated |
| DELETE | `/api/cart/items/{sku}` | Remove item                  | Authenticated |
| POST   | `/api/cart/checkout`    | Checkout cart & create order | Authenticated |

### 🧾 Order Service

| Method | Endpoint                    | Description       | Access        |
| ------ | --------------------------- | ----------------- | ------------- |
| POST   | `/api/orders`               | Create order      | Authenticated |
| GET    | `/api/orders/{id}`          | Get order details | Authenticated |
| GET    | `/api/orders/user/{userId}` | Get user orders   | Authenticated |
| POST   | `/api/orders/{id}/cancel`   | Cancel order      | Authenticated |

[//]: # (### 💳 Payment Service)

[//]: # ()
[//]: # (| Method | Endpoint                 | Description             | Access           |)

[//]: # (| ------ | ------------------------ | ----------------------- | ---------------- |)

[//]: # (| POST   | `/api/payments/initiate` | Initiate payment        | Internal         |)

[//]: # (| POST   | `/api/payments/webhook`  | Payment gateway webhook | Public &#40;secured&#41; |)

### 🔍 Search Service (Elasticsearch)

| Method | Endpoint               | Description                             |
| ------ | ---------------------- | --------------------------------------- |
| GET    | `/api/search/products` | Search products by keyword              |
| GET    | `/api/search/filter`   | Filter products (category, price, etc.) |

[//]: # (### 📣 Notification Service)

[//]: # ()
[//]: # (| Method | Endpoint                  | Description                   |)

[//]: # (| ------ | ------------------------- | ----------------------------- |)

[//]: # (| POST   | `/api/notifications/send` | Send email/SMS &#40;event-driven&#41; |)



