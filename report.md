# UAMS (University Accommodation Management System) - Database Report

This comprehensive report serves as the complete guide to the MySQL database architecture tailored for the UAMS project. It is written from start to end to clearly explain the underlying data structures, relationships, and queries to all group members, ensuring everyone has a solid grasp of how the backend manages data, regardless of their level of contribution to the coding phase.

---

## 1. Introduction

The University Accommodation Management System (UAMS) relies on a robust relational database to manage students, accommodations (halls and apartments), leases, staff, and financial records. We chose **MySQL** as our database management system due to its reliability, scalability, and excellent integration with Spring Boot (via Hibernate/JPA). 

The database schema is heavily normalized to reduce data redundancy and improve data integrity, leveraging foreign keys to bind related records together.

---

## 2. Entity Relationship Overview

The core entities revolve around the **Student**. A student can be linked to multiple components of the campus living experience:
- **Accommodation**: Students are either placed in **Residence Halls** (rooms) or **Student Apartments** (bedrooms).
- **Lease & Invoices**: Financial agreements (`lease_agreements`) and billing (`invoices`) are tracked against students.
- **Academics**: Students enroll in **Courses** (`student_courses`) and have an assigned **Adviser**.
- **Management**: **Residence Staff** manage the halls, while **Inspections** track apartment conditions.

---

## 3. Detailed Table Documentation

Below is the explanation of each primary table in the database and what role it plays.

### 3.1. Students & Personal Information
* **`students`**: The central table. It stores personal details (name, email, banner number) and demographic info (gender, nationality, category). It also tracks if they are on a waiting list.
  * *Foreign Key*: `adviser_id` connects to the `advisers` table.
* **`next_of_kin`**: Stores emergency contact info for a student.
  * *Foreign Key*: `student_id` linking back to the student.
* **`advisers`**: Academic staff responsible for guiding students. They have basic profile info and the department they belong to.

### 3.2. Accommodation Spaces
* **`residence_halls`**: Details of large dormitory buildings, including the manager's name and total capacity.
* **`rooms`**: Individual rooms inside a residence hall.
  * *Foreign Key*: `hall_id` linking back to `residence_halls`.
* **`student_apartments`**: Contains details of independent student apartments/flats.
* **`bedrooms`**: Individual bedrooms spanning a given student apartment.
  * *Foreign Keys*: `apartment_id` linking to `student_apartments` and `student_id` indicating the current occupant.

### 3.3. Living Arrangements & Placements
* **`hall_placements`**: Records a student being assigned to a specific room in a hall for a given duration.
  * *Foreign Keys*: `student_id` and `room_id`.
* **`lease_agreements`**: The formal contract linking a student to their accommodation space. Includes dates, rent, deposit, and status.
* **`apartment_inspections`**: Keeps track of condition checks on student apartments.

### 3.4. Staff & Administration
* **`residence_staff`**: Employees managing the halls (e.g., Hall Managers).
  * *Foreign Key*: `hall_id` indicating where they are assigned.

### 3.5. Academics
* **`courses`**: Details about university courses and the instructors teaching them.
* **`student_courses`**: A mapping table (many-to-many relationship) tracking which student is enrolled in which course.

### 3.6. Financials
* **`invoices`**: Billing records issued to students for rent or damages. Tracks the amount, due date, reminders, and payment status (e.g., 'PENDING', 'PAID').

---

## 4. Schema Definition Examples (DDL)

For team members curious about how tables are originally constructed in MySQL, here is a snippet of the Data Definition Language (DDL) from our database migrations.

**Creating the Students Table:**
```sql
CREATE TABLE students (
    student_id BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    banner_number VARCHAR(50) NULL,
    date_of_birth date NOT NULL,
    category VARCHAR(20) NOT NULL,
    is_active BIT(1) NULL,
    adviser_id BIGINT NULL,
    CONSTRAINT pk_students PRIMARY KEY (student_id),
    CONSTRAINT uc_students_email UNIQUE (email),
    CONSTRAINT FK_STUDENTS_ON_ADVISER FOREIGN KEY (adviser_id) REFERENCES advisers (adviser_id)
);
```

---

## 5. The 14 Mandatory Reports (SQL Queries)

These are the **14 specific reports** mandated by the problem statement. For each report, the relevant business logic scenario is explained along with its raw SQL query execution. Note that the Spring Backend translates these dynamically using JPA, but the equivalent standard SQL queries are documented below:

### (a) Manager Details for Each Hall
**Description:** Retrieves the manager's name and telephone number for every residence hall.
```sql
SELECT hall_name, manager_name, manager_phone 
FROM residence_halls;
```

### (b) Students with Active Lease Agreements
**Description:** Fetches all student names and banner numbers, alongside the details of their active lease agreements.
```sql
SELECT s.student_id, s.first_name, s.last_name, s.banner_number, 
       l.lease_number, l.semester, l.start_date, l.end_date, l.monthly_rent 
FROM students s
INNER JOIN lease_agreements l ON s.student_id = l.student_id
WHERE l.status = 'ACTIVE';
```

### (c) Summer Semester Lease Agreements
**Description:** Lists all lease agreements that specifically encompass the summer semester.
```sql
SELECT l.lease_number, s.student_id, s.first_name, s.last_name, 
       l.start_date, l.end_date, l.monthly_rent 
FROM lease_agreements l
INNER JOIN students s ON l.student_id = s.student_id
WHERE l.semester = 'SUMMER';
```

### (d) Total Rent Paid by a Given Student
**Description:** Calculates the sum of all invoices fully paid by a specific student (e.g., `student_id = 1`).
```sql
SELECT student_id, SUM(amount) AS total_rent_paid 
FROM invoices 
WHERE status = 'PAID' AND student_id = 1
GROUP BY student_id;
```

### (e) Unpaid Invoices by a Given Date
**Description:** Lists students who have unpaid (overdue) invoices beyond a specific deadline.
```sql
SELECT s.student_id, s.first_name, s.last_name, 
       i.invoice_number, i.amount, i.due_date, i.status 
FROM invoices i
INNER JOIN students s ON i.student_id = s.student_id
WHERE i.status = 'PENDING' AND i.due_date < '2026-05-01'; /* Replace with deadline date */
```

### (f) Unsatisfactory Apartment Inspections
**Description:** Identifies apartments where recent condition inspections have yielded a 'FAILED' or unsatisfactory result.
```sql
SELECT a.apartment_name, a.address, 
       i.inspector_name, i.inspection_date, i.remarks, i.status 
FROM apartment_inspections i
INNER JOIN student_apartments a ON i.apartment_id = a.apartment_id
WHERE i.status = 'FAILED';
```

### (g) Students in a Specific Hall
**Description:** Retrieves a list of students, with their room number and place number, residing in a specific hall.
```sql
SELECT s.student_id, s.first_name, s.last_name, 
       r.room_number, r.place_number, h.hall_name, p.start_date 
FROM hall_placements p
INNER JOIN students s ON p.student_id = s.student_id
INNER JOIN rooms r ON p.room_id = r.room_id
INNER JOIN residence_halls h ON r.hall_id = h.hall_id
WHERE p.is_active = 1 AND h.hall_id = 1; /* Replace with desired hall_id */
```

### (h) Students Currently on the Waiting List
**Description:** Fetches all students who are flagged as currently being on the accommodation waiting list.
```sql
SELECT student_id, first_name, last_name, email, category 
FROM students 
WHERE waiting_list = 1;
```

### (i) Total Number of Students in Each Category
**Description:** Aggregates and counts the total number of students belonging to each category (e.g., First-Year, Postgraduate, etc.).
```sql
SELECT category, COUNT(student_id) AS total_students 
FROM students 
GROUP BY category;
```

### (j) Students Without Next-of-Kin
**Description:** Finds all students who have not yet provided next-of-kin emergency contact information.
```sql
SELECT student_id, first_name, last_name, email, category 
FROM students 
WHERE student_id NOT IN (
    SELECT student_id FROM next_of_kin
);
```

### (k) Adviser Details for a Specific Student
**Description:** Gets the name and internal phone number of the assigned academic adviser for a given student.
```sql
SELECT a.first_name, a.last_name, a.phone, a.email, a.department 
FROM advisers a
INNER JOIN students s ON a.adviser_id = s.adviser_id
WHERE s.student_id = 1; /* Replace with desired student_id */
```

### (l) Hall Room Rent Statistics
**Description:** Computes the minimum, maximum, and average monthly rent for rooms across the university's residence halls.
```sql
SELECT MIN(monthly_fee) AS min_rent, 
       MAX(monthly_fee) AS max_rent, 
       ROUND(AVG(monthly_fee), 2) AS average_rent 
FROM rooms 
WHERE monthly_fee IS NOT NULL;
```

### (m) Total Places Provided in Each Residence Hall
**Description:** Groups the rooms by hall to display the total capacity and available capacity for every residence hall.
```sql
SELECT h.hall_name, 
       COUNT(r.room_id) AS total_rooms, 
       SUM(CASE WHEN r.is_available = 1 THEN 1 ELSE 0 END) AS available_rooms 
FROM rooms r
INNER JOIN residence_halls h ON r.hall_id = h.hall_id
GROUP BY h.hall_name;
```

### (n) Staff Over 60 Years Old
**Description:** Identifies active residence staff who are over 60 years old, retrieving their internal identification, name, age, and assigned location.
```sql
SELECT staff_id, first_name, last_name, role,
       TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) AS age 
FROM residence_staff 
WHERE is_active = 1 
AND TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) > 60;
```

---

## 6. Conclusion
The UAMS database structure is designed tightly with Foreign Keys (`FK`) and Unique Constraints (`UC`) to guarantee data integrity. By splitting personal data, accommodation definitions, and financial tracking into specific tables, the system maintains high performance and logical consistency.

*For any further questions regarding specific table structures or the backend JPA translations, please reference the `V1__init_UAMS.sql` migration script located in the codebase.*
