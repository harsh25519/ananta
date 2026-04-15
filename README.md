# ♾️ Ananta — E-Commerce Backend API

A multivendor marketplace REST API where sellers, customers, and admins each operate in a clearly defined lane. Admins curate the master product catalog, sellers apply to list inventory, customers browse and checkout via Razorpay — built on Spring Boot 4 with JWT auth end to end.

🚀 **Live API:** `https://ananta-ecomm.onrender.com/ananta/v1`
📄 **Swagger Docs:** [View all endpoints](https://ananta-ecomm.onrender.com/ananta/v1/swagger-ui/index.html)

---

## 🛠️ Tech Stack

|                              |                                           |
|------------------------------|-------------------------------------------|
| Language & Framework         | Java 17 · Spring Boot 4.0.3               |
| Build                        | Maven 3.9.x                               |
| Database                     | PostgreSQL (local) · Neon DB (production) |
| Payments                     | Razorpay                                  |
| Image Storage                | Cloudinary                                |
| Hosting                      | Render                                    |
| Security                     | Spring Security and JWT auth              |
| Testing (Unit & Integration) | JUnit5 & Mockito                          |

---

## ⚙️ Local Setup

**Prerequisites:** Java 17, Maven 3.9.x, PostgreSQL

```bash
git clone https://github.com/harsh25519/ananta.git
cd ananta
```

Configure `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ananta_db
spring.datasource.username=your_username
spring.datasource.password=your_password
razorpay.key.id=your_key
razorpay.key.secret=your_secret
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

```bash
mvn clean install
mvn spring-boot:run
# Runs at http://localhost:8080/ananta/v1
```

---

## 🧪 Running Tests

```bash
mvn test
```

Covers unit tests (service layer with Mockito) and integration tests (repository + controller layer with JUnit 5) of some of the features.

---

## 👥 Roles

| Role | Can Do |
|------|--------|
| 🛒 Customer | Browse products, manage cart, place orders, Razorpay checkout, leave reviews |
| 🏪 Seller | Apply for seller status, browse master catalog, apply to list products, manage own inventory |
| 🛡️ Admin | Create/manage manufacturers, categories & tags, approve seller product listings |

---

### 🔄 System Architecture & API Workflows

#### 1. Authentication & User Management
Visitors can register, secure a JWT, manage addresses, and seamlessly upgrade to a Seller account.
![User Auth Flow](https://res.cloudinary.com/dfdlovjnz/image/upload/q_auto/f_auto/v1776275922/auth_user_flow_o5gres.svg)

#### 2. Admin Catalog Management
Admins maintain strict control over the global catalog by managing authorized manufacturers, categories, and tags.
![Admin Catalog Flow](https://res.cloudinary.com/dfdlovjnz/image/upload/q_auto/f_auto/v1776275922/admin_catalog_flow_gnbew8.svg)

#### 3. Seller Product Listing
A secure system where Admins create master products, and Sellers must apply to list inventory against them.
![Product Listing Flow](https://res.cloudinary.com/dfdlovjnz/image/upload/q_auto/f_auto/v1776275922/product_listing_flow_wgpd1o.svg)

#### 4. Customer Shopping & Checkout
The complete end-to-end buyer journey, featuring cart management, order calculation, and secure Razorpay integration.

![Customer Shopping Flow](https://res.cloudinary.com/dfdlovjnz/image/upload/q_auto/f_auto/v1776275922/customer_shopping_flow_qowca5.svg)
---

## 🌍 Production (Render + Neon DB)

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<db>?sslmode=require
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
RAZORPAY_KEY_ID=...
RAZORPAY_KEY_SECRET=...
CLOUDINARY_CLOUD_NAME=...
CLOUDINARY_API_KEY=...
CLOUDINARY_API_SECRET=...
APP_BASE_URL=https://ananta-ecomm.onrender.com
```

---

**Built by [Harsh ](https://www.linkedin.com/in/harshkumar487/) · Feel free to connect or raise an issue.** 