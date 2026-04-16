## 📂 Repository Structure

This project uses a **Package-by-Feature (Domain-Driven)** architecture. Instead of grouping files by type (controllers, services, etc.), they are grouped by the business feature they represent. This makes the codebase highly modular and scalable.

```text
ananta/
├── src/
│   ├── main/
│   │   ├── java/dev/hkb/ananta/
│   │   │   ├── address/          # Feature: Address Management
│   │   │   │   ├── dto/
│   │   │   │   ├── Address.java                  (Entity)
│   │   │   │   ├── AddressController.java
│   │   │   │   ├── AddressMapper.java
│   │   │   │   ├── AddressRepository.java
│   │   │   │   ├── AddressService.java           (Interface)
│   │   │   │   └── AddressServiceImpl.java       (Implementation)
│   │   │   ├── cart/             # Feature: Shopping Cart
│   │   │   ├── category/         # Feature: Product Categories
│   │   │   ├── config/           # Global Configurations (e.g., SwaggerConfig)
│   │   │   ├── constants/        # Enums (Roles, Status, Payment Methods, etc.)
│   │   │   ├── exceptionHandler/ # Custom Exceptions & Global RestExceptionHandler
│   │   │   ├── healthController/ # Deployment Health Checks
│   │   │   ├── image/            # Feature: Cloudinary Image Storage
│   │   │   ├── manufacturer/     # Feature: Manufacturer Management
│   │   │   ├── order/            # Feature: Orders & Cart-to-Order Mapping
│   │   │   ├── payment/          # Feature: Razorpay Integration
│   │   │   ├── product/          # Feature: Master Product Catalog
│   │   │   ├── review/           # Feature: Product Reviews
│   │   │   ├── security/         # Security & Authentication
│   │   │   │   ├── auth/         # Auth Endpoints
│   │   │   │   ├── config/       # SecurityConfig
│   │   │   │   ├── jwt/          # JWT Filters and Utils
│   │   │   │   └── utils/        # UserDetailsServiceImpl & EmailService
│   │   │   ├── seller/           # Feature: Seller Profiles
│   │   │   ├── sellerProduct/    # Feature: Seller Inventory/Listings
│   │   │   ├── tag/              # Feature: Product Tags
│   │   │   ├── user/             # Feature: User Profiles & Management
│   │   │   └── AnantaApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-dev.properties.example
│   │       └── application-prod.properties
│   └── test/
│       ├── java/dev/hkb/ananta/
│       │   ├── manufacturer/     # Unit & Integration Tests
│       │   ├── order/
│       │   ├── payment/
│       │   ├── product/
│       │   ├── user/
│       │   └── AnantaApplicationTests.java
│       └── resources/
│           ├── application.properties
│           ├── application-prodtest.properties
│           ├── application-test.properties
│           └── application-test.properties.example
├── .gitattributes
├── .gitignore
├── Dockerfile
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md