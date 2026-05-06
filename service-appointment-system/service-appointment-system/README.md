# Service Appointment & Support System (SASS)

A full-stack Spring Boot web application with role-based access for Admin, Staff and Customer.

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### ⚠️ IMPORTANT — First Time Setup (Fix DB Errors)

If you see **ENUM errors** or **"No enum constant"** errors, your database has stale data.
Run `FIX_DB.sql` in MySQL Workbench first:

```sql
-- In MySQL Workbench, run:
DROP DATABASE IF EXISTS service_appointment_db;
CREATE DATABASE service_appointment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Then start the app — Hibernate will auto-create all tables correctly.

### 1. Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### 2. Run
```bash
mvn spring-boot:run
```
Opens at: **http://localhost:8080**

---

## 🔑 Login Credentials

| Role     | URL                    | Email                    | Password    |
|----------|------------------------|--------------------------|-------------|
| Admin    | `/admin/login`         | admin@system.com         | admin123    |
| Staff    | `/auth/login`          | staff@system.com         | staff123    |
| Customer | `/auth/login`          | customer@system.com      | customer123 |

---

## 📋 Features

### 🌐 Public Homepage (`/`)
- Jadoo-style landing page with services, how-it-works, testimonials
- Register / Login links

### 👑 Admin (`/admin/login`)
- Dashboard with KPIs (customers, revenue, pending items)
- User management (enable/disable/delete)
- Service CRUD (add/edit/toggle/delete)
- Appointment status management
- Job assignment to staff
- **Payment management** — view bank transfer receipts, confirm/reject
- **Invoice viewer** with print support
- Leave request review (approve/reject)
- Support ticket management & responses

### 👷 Staff (`/auth/login`)
- Dashboard with job summary
- My Jobs — view & update status (Assigned → In Progress → Completed)
- Schedule — timeline view of upcoming jobs
- Apply for leave & track history
- Profile management

### 🙋 Customer (`/auth/login`)
- Browse services & book appointments
- **Auto-redirect to payment page after booking**
- **Card payment** — instant confirmation
- **Bank Transfer** — upload receipt → admin confirms in 1-2 hrs
- Auto-generated Invoice (printable)
- My appointments with payment status & invoice links
- Payment history
- Feedback — unlocked after payment confirmed
- Support tickets
- Profile management

---

## 💳 Payment Flow

```
Book → Payment Checkout → [Card: instant | Bank Transfer: upload receipt]
     ↓                                          ↓
  Invoice Generated                    Admin Reviews Receipt
     ↓                                          ↓
  Appointment CONFIRMED              Admin Confirms → Appointment CONFIRMED
     ↓                                          ↓
  Feedback Unlocked               Invoice + Feedback Unlocked
```

---

## 🗄️ Tech Stack

| Layer      | Technology              |
|------------|-------------------------|
| Backend    | Java 17, Spring Boot 3.2|
| Security   | Spring Security 6       |
| ORM        | Spring Data JPA + Hibernate |
| Database   | MySQL 8                 |
| Frontend   | Thymeleaf + CSS + JS    |
| Build      | Maven                   |

---

## 📁 Project Structure

```
src/main/java/com/example/sas/
├── config/          SecurityConfig, WebConfig, DataInitializer, GlobalControllerAdvice
├── controller/      Admin, AdminPayment, Auth, Customer, Staff, Home
├── service/         All business logic services
├── repository/      JPA repositories
├── entity/          JPA entities (User, Payment, Appointment, Job, ...)
└── dto/             Data transfer objects

src/main/resources/
├── static/css/      styles.css (blue gradient theme)
├── static/js/       main.js
├── templates/
│   ├── home.html           ← Public landing page
│   ├── auth/               ← Login, Register, Admin login
│   ├── admin/              ← 8 admin pages + invoice
│   ├── staff/              ← 5 staff pages
│   ├── customer/           ← 9 customer pages + payment + invoice
│   ├── fragments/          ← 4 sidebar fragments (admin/staff/customer/generic)
│   └── error/              ← 403, 404 pages
└── db/              fix_payments_table.sql
```
