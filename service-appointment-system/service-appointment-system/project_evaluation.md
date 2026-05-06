# Project Evaluation: Service Appointment & Support System (SASS)

This document provides a comprehensive evaluation of the existing service-appointment-system project, outlining current workflows, functionality gaps, incorrect implementations, and recommendations for future improvements.

---

## 🚀 1. Existing Workflows

### Customer Workflow
1. **Browsing & Booking**: Customers can browse active services and book an appointment.
2. **Payment Flow**: 
   - Upon booking, customers are redirected to the payment page.
   - **Card Payment**: Instantly marks the payment as `PAID` and appointment as `CONFIRMED`.
   - **Bank Transfer**: Customer uploads a receipt image. The payment status becomes `RECEIPT_UPLOADED` pending Admin confirmation.
3. **Post-Service**: Once the appointment is completed and paid, customers can submit feedback. Customers can also raise support tickets at any time.

### Staff Workflow
1. **Job Execution**: Staff members view jobs assigned to them by the Admin.
2. **Status Updates**: Staff update the job status: `ASSIGNED` -> `IN_PROGRESS` -> `COMPLETED`. Updating to `IN_PROGRESS` and `COMPLETED` automatically logs timestamps.
3. **Leave Management**: Staff can submit leave requests which await Admin approval.

### Admin Workflow
1. **Dashboard & Management**: Centralized dashboard to view KPIs.
2. **Job Assignment**: Admin must manually assign incoming `PENDING` jobs to available staff members.
3. **Payment Verification**: Admin reviews uploaded bank transfer receipts and can `Confirm` (marks as PAID & CONFIRMED) or `Reject`.
4. **General Operations**: Admin manages services (CRUD), users (soft/hard delete), leave requests, and replies to support tickets.

---

## ⚠️ 2. What Isn't Working Correctly (Bugs & Flaws)

1. **Appointment Cancellation Does Not Sync with Jobs**
   - **Location**: `AppointmentService.cancel()`
   - **Issue**: When a customer or admin cancels an appointment, the appointment status is set to `CANCELLED`. However, the auto-created `Job` associated with this appointment is **not** updated and remains in its previous state (e.g., `PENDING` or `ASSIGNED`). This will cause ghost jobs to appear in the staff's or admin's queues.

2. **Hard Deletion of Users**
   - **Location**: `AdminController.deleteUser()` / `UserService.deleteUser()`
   - **Issue**: Attempting to delete a user who has associated records (Appointments, Payments, Jobs, Tickets, Feedback) will likely result in SQL `DataIntegrityViolationException` (Foreign Key constraint failure) unless `CASCADE DELETE` is aggressively configured. **Soft deleting** (toggling a disabled static status) is implemented via `toggleUserStatus`, but the hard delete button shouldn't casually wipe relational data.

3. **Inconsistent Appointment Completion State**
   - **Location**: `JobService.updateStatus()` vs. `AppointmentService`
   - **Issue**: When a staff member marks a Job as `COMPLETED`, the corresponding `Appointment` status remains `CONFIRMED`. There is no automated trigger to mark the Appointment itself as `COMPLETED`, which could affect dashboard metrics and feedback unlocking logic.

---

## 🚧 3. What Isn't Fully Implemented (Mocked / Missing)

1. **Card Payment Gateway Integration**
   - **Current State**: The `PaymentService.processCardPayment()` method is a mock. It instantly approves any card transaction without verifying payment details through an external API (like Stripe or PayPal).
   - **Needed**: Integration with a real payment provider SDK to handle tokenization, webhooks, and secure charging.

2. **Local File Storage for Receipts**
   - **Current State**: Bank transfer receipts are saved to the local file system (`uploads/receipts/`).
   - **Needed**: If the application is deployed to a cloud platform (e.g., Heroku, AWS ECS, Render) that uses ephemeral file systems, uploads will be lost upon server restart. Needs integration with cloud storage (AWS S3, Cloudinary).

3. **Email Notifications**
   - **Current State**: The system relies entirely on manual dashboard checking. No emails are sent for booking confirmations, payment receipts, job assignments, or support ticket responses.
   - **Needed**: Integration with `spring-boot-starter-mail` and a provider like SendGrid/Mailgun to dispatch transactional emails.

4. **Automated Staff Assignment (Scheduling Conflict Prevention)**
   - **Current State**: Admin manually assigns jobs. There is no protection against double-booking a staff member for overlapping times.
   - **Needed**: Logic to prevent assigning a staff member to a job if they have another appointment in the same time window, or if they have an approved `LeaveRequest` on that date.

---

## 🛠️ 4. What Is Needed to Be Implemented Correctly

To make this project production-ready, the following implementations are required:

1. **Fix Job-Appointment Synchronization**:
   - Update `AppointmentService.cancel()` to also fetch the related `Job` and set its status to `CANCELLED`.
   - Update `JobService.updateStatus()` so that when a job becomes `COMPLETED`, it automatically marks the parent `Appointment` as `COMPLETED`.

2. **Refactor User Deletion**:
   - Remove the hard delete function for users with transactional history, or restrict it to admin-only with a warning. Rely strictly on the `toggleUserStatus` (Soft Disable) functionality.

3. **Integrate Real Payments (Stripe/PayPal)**:
   - Add Stripe Java SDK. Replace `processCardPayment` with a Stripe Checkout session or PaymentIntent validation.

4. **Implement AWS S3 for File Uploads**:
   - Replace the `java.nio.file.Files.copy` logic in `PaymentService.uploadReceipt()` with an S3 client put-object call. Update `AdminPaymentController.serveReceipt` to serve a pre-signed URL.

5. **Implement an Email Service Layer**:
   - Create an `EmailService` class using `JavaMailSender` and Thymeleaf templates to send out:
     - Booking Confirmation Invoices.
     - Leave Approval/Rejection notices.
     - Support Ticket updates.

6. **Add Validation for Staff Scheduling**:
   - Before assigning a job in `JobService.assignStaff()`, verify the staff member has no overlapping jobs based on `appointmentDate` and `appointmentTime`.
   - Also check the `LeaveRequestRepository` to ensure the staff member isn't on approved leave that day.
