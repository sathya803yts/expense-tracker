# Personal Expense Tracker

A full-stack Java Spring Boot application for tracking personal expenses with JWT authentication, category management, and comprehensive reporting features.

## 🚀 Features

- **User Authentication**: JWT-based authentication with Spring Security
- **Expense Management**: Add, edit, delete, and view expenses with date, amount, category, and description
- **Category Management**: CRUD operations on expense categories (Food, Travel, etc.)
- **Reporting**: Monthly, weekly, yearly, and category-wise expense reports
- **API Documentation**: Swagger UI for interactive API testing

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.1.5, Java 17+
- **Security**: Spring Security + JWT
- **Database**: MySQL with Spring Data JPA
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Other**: Lombok, Bean Validation

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

## 🔧 Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd expense-tracker
```

### 2. Database Setup
Create a MySQL database:
```sql
CREATE DATABASE expense_tracker;
```

### 3. Configure Database
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

### 🔐 Authentication Endpoints

- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

### 💰 Expense Endpoints

- `GET /api/expenses` - Get all expenses
- `GET /api/expenses/{id}` - Get expense by ID
- `GET /api/expenses/date-range` - Get expenses by date range
- `POST /api/expenses` - Create new expense
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

### 🏷️ Category Endpoints

- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### 📊 Report Endpoints

- `GET /api/reports/monthly` - Monthly expense report
- `GET /api/reports/weekly` - Weekly expense report
- `GET /api/reports/yearly` - Yearly expense report
- `GET /api/reports/category-wise` - Category-wise expense breakdown

## 🔑 Authentication

All endpoints except authentication require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📝 Sample API Usage

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### 3. Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "name": "Food",
    "description": "Food and dining expenses",
    "color": "#FF6B6B"
  }'
```

### 4. Add Expense
```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "description": "Lunch at restaurant",
    "amount": 25.50,
    "expenseDate": "2024-01-15",
    "categoryId": 1
  }'
```

## 🏗️ Project Structure

```
src/main/java/com/expensetracker/
├── ExpenseTrackerApplication.java
├── config/
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── CategoryController.java
│   ├── ExpenseController.java
│   └── ReportController.java
├── dto/
│   ├── request/
│   └── response/
├── entity/
│   ├── Category.java
│   ├── Expense.java
│   └── User.java
├── exception/
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── CategoryRepository.java
│   ├── ExpenseRepository.java
│   └── UserRepository.java
├── security/
│   ├── AuthTokenFilter.java
│   ├── JwtUtils.java
│   ├── UserDetailsServiceImpl.java
│   └── UserPrincipal.java
└── service/
    ├── AuthService.java
    ├── CategoryService.java
    ├── ExpenseService.java
    └── ReportService.java
```

## 🚀 Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/expense-tracker-1.0.0.jar
```

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
