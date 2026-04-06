# UAMS — ORM (Object-Relational Mapping) Documentation

> **Framework:** Spring Data JPA (Hibernate) · **Database:** MySQL 8 · **Java:** 17

---

## Table of Contents

1. [What is ORM?](#1-what-is-orm)
2. [Bipartite Mapping — Entity ↔ Table](#2-bipartite-mapping--entity--table)
3. [Entity-Relationship Diagram](#3-entity-relationship-diagram)
4. [Entity Classes Reference](#4-entity-classes-reference)
5. [Repository Interfaces](#5-repository-interfaces)
6. [Enum Types](#6-enum-types)
7. [JPA Annotations Cheat-Sheet](#7-jpa-annotations-cheat-sheet)

---

## 1. What is ORM?

ORM stands for **Object-Relational Mapping**. It bridges the gap between an **object-oriented** Java application and a **relational** MySQL database by automatically converting:

| Java (Object World)       | MySQL (Relational World) |
|---------------------------|--------------------------|
| Class                     | Table                    |
| Field / Property          | Column                   |
| Object instance           | Row                      |
| `@ManyToOne` reference    | Foreign Key              |
| `@OneToMany` collection   | One-to-Many relationship |
| Enum                      | VARCHAR / ENUM column    |

We use **Spring Data JPA** (powered by **Hibernate**) as our ORM provider. Each entity class under `module/*/entity/` maps directly to a MySQL table, and each `JpaRepository` interface gives us CRUD + custom query methods without writing raw SQL.

---

## 2. Bipartite Mapping — Entity ↔ Table

The bipartite graph below shows how every **Java Entity Class** (left) maps to its corresponding **MySQL Table** (right). This is the core of the ORM — each class is a direct mirror of a single table.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        BIPARTITE MAPPING: CLASS ↔ TABLE                        │
├───────────────────────────────┬─────────────────────────────────────────────────┤
│       Java Entity Class       │            MySQL Table                         │
├───────────────────────────────┼─────────────────────────────────────────────────┤
│  Student                      │ ◄──────────► students                          │
│  Adviser                      │ ◄──────────► advisers                          │
│  Course                       │ ◄──────────► courses                           │
│  StudentCourse                │ ◄──────────► student_courses                   │
│  ResidenceHall                │ ◄──────────► residence_halls                   │
│  Room                         │ ◄──────────► rooms                             │
│  HallPlacement                │ ◄──────────► hall_placements                   │
│  ResidenceStaff               │ ◄──────────► residence_staff                   │
│  LeaseAgreement               │ ◄──────────► lease_agreements                  │
│  Invoice                      │ ◄──────────► invoices                          │
│  NextOfKin                    │ ◄──────────► next_of_kin                       │
│  StudentApartment             │ ◄──────────► student_apartments                │
│  Bedroom                      │ ◄──────────► bedrooms                          │
│  ApartmentInspection          │ ◄──────────► apartment_inspections             │
└───────────────────────────────┴─────────────────────────────────────────────────┘
```

### Bipartite Mapping — Fields ↔ Columns (Per Entity)

#### Student ↔ `students`

```
Student.java                        students (MySQL)
──────────────                      ────────────────
studentId          ◄──────────►     student_id          (BIGINT, PK, AUTO_INCREMENT)
firstName          ◄──────────►     first_name          (VARCHAR(100), NOT NULL)
lastName           ◄──────────►     last_name           (VARCHAR(100), NOT NULL)
email              ◄──────────►     email               (VARCHAR(255), NOT NULL, UNIQUE)
password           ◄──────────►     password            (VARCHAR(255), NOT NULL)
bannerNumber       ◄──────────►     banner_number       (VARCHAR(50), UNIQUE)
dateOfBirth        ◄──────────►     date_of_birth       (DATE, NOT NULL)
category           ◄──────────►     category            (VARCHAR(20), NOT NULL)
waitingList        ◄──────────►     waiting_list        (TINYINT(1), DEFAULT 0)
street             ◄──────────►     street              (VARCHAR(200))
city               ◄──────────►     city                (VARCHAR(100))
postcode           ◄──────────►     postcode            (VARCHAR(20))
mobilePhone        ◄──────────►     mobile_phone        (VARCHAR(20))
gender             ◄──────────►     gender              (VARCHAR(20))
nationality        ◄──────────►     nationality         (VARCHAR(100))
specialNeeds       ◄──────────►     special_needs       (TEXT)
additionalComments ◄──────────►     additional_comments (TEXT)
major              ◄──────────►     major               (VARCHAR(100))
minor              ◄──────────►     minor               (VARCHAR(100))
isActive           ◄──────────►     is_active           (TINYINT(1), DEFAULT 1)
adviser            ◄──────────►     adviser_id          (BIGINT, FK → advisers)
```

#### Adviser ↔ `advisers`

```
Adviser.java                        advisers (MySQL)
────────────                        ─────────────────
adviserId          ◄──────────►     adviser_id          (BIGINT, PK, AUTO_INCREMENT)
firstName          ◄──────────►     first_name          (VARCHAR(100), NOT NULL)
lastName           ◄──────────►     last_name           (VARCHAR(100), NOT NULL)
email              ◄──────────►     email               (VARCHAR(255), NOT NULL, UNIQUE)
password           ◄──────────►     password            (VARCHAR(255))
phone              ◄──────────►     phone               (VARCHAR(20))
department         ◄──────────►     department          (VARCHAR(200))
```

#### Course ↔ `courses`

```
Course.java                         courses (MySQL)
───────────                         ───────────────
courseId            ◄──────────►     course_id           (BIGINT, PK, AUTO_INCREMENT)
courseName         ◄──────────►     course_name         (VARCHAR(200), NOT NULL)
courseCode          ◄──────────►     course_code         (VARCHAR(20), UNIQUE)
department         ◄──────────►     department          (VARCHAR(200))
instructorName     ◄──────────►     instructor_name     (VARCHAR(200))
instructorPhone    ◄──────────►     instructor_phone    (VARCHAR(20))
instructorEmail    ◄──────────►     instructor_email    (VARCHAR(255))
instructorRoomNumber ◄────────►     instructor_room_number (VARCHAR(50))
```

#### StudentCourse ↔ `student_courses` (Junction Table)

```
StudentCourse.java                  student_courses (MySQL)
──────────────────                  ───────────────────────
id                 ◄──────────►     id                  (BIGINT, PK, AUTO_INCREMENT)
student            ◄──────────►     student_id          (BIGINT, FK → students, NOT NULL)
course             ◄──────────►     course_id           (BIGINT, FK → courses, NOT NULL)
enrolledDate       ◄──────────►     enrolled_date       (DATE)
                                    UNIQUE(student_id, course_id)
```

#### ResidenceHall ↔ `residence_halls`

```
ResidenceHall.java                  residence_halls (MySQL)
──────────────────                  ───────────────────────
hallId             ◄──────────►     hall_id             (BIGINT, PK, AUTO_INCREMENT)
hallName           ◄──────────►     hall_name           (VARCHAR(200), NOT NULL)
address            ◄──────────►     address             (VARCHAR(500))
telephoneNumber    ◄──────────►     telephone_number    (VARCHAR(20))
totalRooms         ◄──────────►     total_rooms         (INT)
managerName        ◄──────────►     manager_name        (VARCHAR(200))
managerPhone       ◄──────────►     manager_phone       (VARCHAR(20))
```

#### Room ↔ `rooms`

```
Room.java                           rooms (MySQL)
─────────                           ─────────────
roomId             ◄──────────►     room_id             (BIGINT, PK, AUTO_INCREMENT)
hall               ◄──────────►     hall_id             (BIGINT, FK → residence_halls, NOT NULL)
roomNumber         ◄──────────►     room_number         (VARCHAR(20), NOT NULL)
placeNumber        ◄──────────►     place_number        (VARCHAR(50), UNIQUE)
roomType           ◄──────────►     room_type           (VARCHAR(100))
monthlyFee         ◄──────────►     monthly_fee         (DECIMAL(10,2))
isAvailable        ◄──────────►     is_available        (TINYINT(1), DEFAULT 1)
```

#### HallPlacement ↔ `hall_placements`

```
HallPlacement.java                  hall_placements (MySQL)
──────────────────                  ───────────────────────
placementId        ◄──────────►     placement_id        (BIGINT, PK, AUTO_INCREMENT)
student            ◄──────────►     student_id          (BIGINT, FK → students, NOT NULL)
room               ◄──────────►     room_id             (BIGINT, FK → rooms, NOT NULL)
startDate          ◄──────────►     start_date          (DATE, NOT NULL)
endDate            ◄──────────►     end_date            (DATE)
isActive           ◄──────────►     is_active           (TINYINT(1), DEFAULT 1)
```

#### ResidenceStaff ↔ `residence_staff`

```
ResidenceStaff.java                 residence_staff (MySQL)
───────────────────                 ──────────────────────
staffId            ◄──────────►     staff_id            (BIGINT, PK, AUTO_INCREMENT)
hall               ◄──────────►     hall_id             (BIGINT, FK → residence_halls)
firstName          ◄──────────►     first_name          (VARCHAR(100), NOT NULL)
lastName           ◄──────────►     last_name           (VARCHAR(100), NOT NULL)
email              ◄──────────►     email               (VARCHAR(255), UNIQUE)
password           ◄──────────►     password            (VARCHAR(255))
phone              ◄──────────►     phone               (VARCHAR(20))
role               ◄──────────►     role                (VARCHAR(100))
dateOfBirth        ◄──────────►     date_of_birth       (DATE)
street             ◄──────────►     street              (VARCHAR(200))
city               ◄──────────►     city                (VARCHAR(100))
postcode           ◄──────────►     postcode            (VARCHAR(20))
gender             ◄──────────►     gender              (VARCHAR(20))
location           ◄──────────►     location            (VARCHAR(100))
isActive           ◄──────────►     is_active           (TINYINT(1), DEFAULT 1)
```

#### LeaseAgreement ↔ `lease_agreements`

```
LeaseAgreement.java                 lease_agreements (MySQL)
───────────────────                 ────────────────────────
leaseId            ◄──────────►     lease_id            (BIGINT, PK, AUTO_INCREMENT)
student            ◄──────────►     student_id          (BIGINT, FK → students, NOT NULL)
leaseNumber        ◄──────────►     lease_number        (VARCHAR(50), UNIQUE)
startDate          ◄──────────►     start_date          (DATE, NOT NULL)
endDate            ◄──────────►     end_date            (DATE, NOT NULL)
enterDate          ◄──────────►     enter_date          (DATE)
leaveDate          ◄──────────►     leave_date          (DATE)
placeNumber        ◄──────────►     place_number        (VARCHAR(50))
monthlyRent        ◄──────────►     monthly_rent        (DECIMAL(10,2))
depositAmount      ◄──────────►     deposit_amount      (DECIMAL(10,2))
semester           ◄──────────►     semester            (VARCHAR(10))
status             ◄──────────►     status              (VARCHAR(20))
notes              ◄──────────►     notes               (TEXT)
```

#### Invoice ↔ `invoices`

```
Invoice.java                        invoices (MySQL)
────────────                        ────────────────
invoiceId          ◄──────────►     invoice_id          (BIGINT, PK, AUTO_INCREMENT)
student            ◄──────────►     student_id          (BIGINT, FK → students, NOT NULL)
invoiceNumber      ◄──────────►     invoice_number      (VARCHAR(50), UNIQUE)
description        ◄──────────►     description         (TEXT)
amount             ◄──────────►     amount              (DECIMAL(10,2), NOT NULL)
issueDate          ◄──────────►     issue_date          (DATE, NOT NULL)
dueDate            ◄──────────►     due_date            (DATE)
semester           ◄──────────►     semester            (VARCHAR(20))
paymentMethod      ◄──────────►     payment_method      (VARCHAR(50))
firstReminderDate  ◄──────────►     first_reminder_date (DATE)
secondReminderDate ◄──────────►     second_reminder_date(DATE)
paidDate           ◄──────────►     paid_date           (DATE)
status             ◄──────────►     status              (VARCHAR(20))
```

#### NextOfKin ↔ `next_of_kin`

```
NextOfKin.java                      next_of_kin (MySQL)
──────────────                      ───────────────────
nokId              ◄──────────►     nok_id              (BIGINT, PK, AUTO_INCREMENT)
student            ◄──────────►     student_id          (BIGINT, FK → students, NOT NULL)
fullName           ◄──────────►     full_name           (VARCHAR(200), NOT NULL)
relationship       ◄──────────►     relationship        (VARCHAR(100))
phone              ◄──────────►     phone               (VARCHAR(20), NOT NULL)
email              ◄──────────►     email               (VARCHAR(255))
street             ◄──────────►     street              (VARCHAR(200))
city               ◄──────────►     city                (VARCHAR(100))
postcode           ◄──────────►     postcode            (VARCHAR(20))
```

#### StudentApartment ↔ `student_apartments`

```
StudentApartment.java               student_apartments (MySQL)
─────────────────────               ──────────────────────────
apartmentId        ◄──────────►     apartment_id        (BIGINT, PK, AUTO_INCREMENT)
apartmentName      ◄──────────►     apartment_name      (VARCHAR(200), NOT NULL)
flatNumber         ◄──────────►     flat_number         (VARCHAR(50), UNIQUE)
address            ◄──────────►     address             (VARCHAR(500))
totalBedrooms      ◄──────────►     total_bedrooms      (INT)
managerName        ◄──────────►     manager_name        (VARCHAR(200))
managerPhone       ◄──────────►     manager_phone       (VARCHAR(20))
```

#### Bedroom ↔ `bedrooms`

```
Bedroom.java                        bedrooms (MySQL)
────────────                        ────────────────
bedroomId          ◄──────────►     bedroom_id          (BIGINT, PK, AUTO_INCREMENT)
apartment          ◄──────────►     apartment_id        (BIGINT, FK → student_apartments, NOT NULL)
student            ◄──────────►     student_id          (BIGINT, FK → students)
bedroomNumber      ◄──────────►     bedroom_number      (VARCHAR(20), NOT NULL)
placeNumber        ◄──────────►     place_number        (VARCHAR(50), UNIQUE)
monthlyFee         ◄──────────►     monthly_fee         (DECIMAL(10,2))
startDate          ◄──────────►     start_date          (DATE)
endDate            ◄──────────►     end_date            (DATE)
isOccupied         ◄──────────►     is_occupied         (TINYINT(1), DEFAULT 0)
```

#### ApartmentInspection ↔ `apartment_inspections`

```
ApartmentInspection.java            apartment_inspections (MySQL)
────────────────────────            ─────────────────────────────
inspectionId       ◄──────────►     inspection_id       (BIGINT, PK, AUTO_INCREMENT)
apartment          ◄──────────►     apartment_id        (BIGINT, FK → student_apartments, NOT NULL)
inspectionDate     ◄──────────►     inspection_date     (DATE, NOT NULL)
inspectorName      ◄──────────►     inspector_name      (VARCHAR(200))
remarks            ◄──────────►     remarks             (TEXT)
status             ◄──────────►     status              (VARCHAR(50))
```

---

## 3. Entity-Relationship Diagram

```
                                ┌─────────────┐
                                │   Adviser    │
                                │  (advisers)  │
                                └──────┬───────┘
                                       │  1
                                       │
                                       │  M
                            ┌──────────┴──────────┐
                            │      Student         │
                            │    (students)         │
                            └──┬──┬──┬──┬──┬──┬────┘
                               │  │  │  │  │  │
                 ┌─────────────┘  │  │  │  │  └────────────────┐
                 │                │  │  │  │                    │
                 │ M              │M │M │M │M                  │ M
      ┌──────────┴──┐    ┌───────┴┐ │  │ ┌┴────────┐  ┌───────┴──────┐
      │ StudentCourse│    │NextOf  │ │  │ │Lease     │  │ HallPlacement│
      │(student_     │    │Kin     │ │  │ │Agreement │  │(hall_        │
      │ courses)     │    │(next_  │ │  │ │(lease_   │  │ placements)  │
      └──────┬───────┘    │of_kin) │ │  │ │agreements│  └──────┬───────┘
             │            └────────┘ │  │ └──────────┘         │
             │ M                     │  │                      │ M
      ┌──────┴───────┐               │  │               ┌──────┴──────┐
      │    Course     │               │  │               │    Room      │
      │  (courses)    │               │  │               │  (rooms)     │
      └──────────────┘               │  │               └──────┬──────┘
                                      │  │                      │ M
                               ┌──────┘  │               ┌──────┴──────┐
                               │ M       │ M             │ResidenceHall│
                        ┌──────┴──┐  ┌───┴──────┐        │(residence_  │
                        │Invoice  │  │ Bedroom  │        │ halls)      │
                        │(invoices│  │(bedrooms)│        └─────────────┘
                        └─────────┘  └────┬─────┘               │ 1
                                          │ M                    │
                                   ┌──────┴──────────┐    ┌──────┴──────┐
                                   │StudentApartment │    │Residence    │
                                   │(student_        │    │Staff        │
                                   │ apartments)     │    │(residence_  │
                                   └────────┬────────┘    │ staff)      │
                                            │ 1           └─────────────┘
                                     ┌──────┴───────────┐
                                     │ApartmentInspection│
                                     │(apartment_        │
                                     │ inspections)      │
                                     └──────────────────┘
```

### Relationship Summary Table

| Relationship             | Type        | FK Column       | Owner Side      |
|--------------------------|-------------|-----------------|-----------------|
| Student → Adviser        | ManyToOne   | `adviser_id`    | Student         |
| StudentCourse → Student  | ManyToOne   | `student_id`    | StudentCourse   |
| StudentCourse → Course   | ManyToOne   | `course_id`     | StudentCourse   |
| NextOfKin → Student      | ManyToOne   | `student_id`    | NextOfKin       |
| LeaseAgreement → Student | ManyToOne   | `student_id`    | LeaseAgreement  |
| Invoice → Student        | ManyToOne   | `student_id`    | Invoice         |
| HallPlacement → Student  | ManyToOne   | `student_id`    | HallPlacement   |
| HallPlacement → Room     | ManyToOne   | `room_id`       | HallPlacement   |
| Room → ResidenceHall     | ManyToOne   | `hall_id`       | Room            |
| ResidenceStaff → ResidenceHall | ManyToOne | `hall_id`  | ResidenceStaff  |
| Bedroom → StudentApartment | ManyToOne | `apartment_id` | Bedroom         |
| Bedroom → Student        | ManyToOne   | `student_id`    | Bedroom         |
| ApartmentInspection → StudentApartment | ManyToOne | `apartment_id` | ApartmentInspection |

---

## 4. Entity Classes Reference

### 4.1 `Student` — `module/student/entity/Student.java`

The **central entity** of the system. Represents a university student with personal, contact, address, and academic information.

```java
@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String firstName, lastName, email, password;
    private String bannerNumber;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private StudentCategory category;     // UNDERGRADUATE | POSTGRADUATE | INTERNATIONAL

    private Boolean waitingList;          // default: false
    private String street, city, postcode;
    private String mobilePhone, gender, nationality;
    private String specialNeeds, additionalComments;
    private String major, minor;
    private Boolean isActive;             // default: true

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adviser_id")
    private Adviser adviser;              // FK → advisers table
}
```

**Key design decisions:**
- Password is stored as a **BCrypt hash** — never plain text.
- `category` uses `@Enumerated(EnumType.STRING)` so MySQL stores `"UNDERGRADUATE"` as text, not an ordinal number. This is safer for future enum reordering.
- `FetchType.LAZY` on the adviser relationship prevents loading the adviser every time a student is queried (N+1 performance).

---

### 4.2 `Adviser` — `module/adviser/entity/Adviser.java`

Represents an academic adviser assigned to students.

```java
@Entity
@Table(name = "advisers")
public class Adviser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adviserId;

    private String firstName, lastName, email, password;
    private String phone, department;
}
```

---

### 4.3 `Course` — `module/course/entity/Course.java`

Represents a university course with instructor details.

```java
@Entity
@Table(name = "courses")
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName, courseCode, department;
    private String instructorName, instructorPhone;
    private String instructorEmail, instructorRoomNumber;
}
```

---

### 4.4 `StudentCourse` — `module/course/entity/StudentCourse.java`

**Junction table** implementing the Many-to-Many relationship between `Student` and `Course`. Uses a **surrogate primary key** (`id`) instead of a composite PK for simpler JPA querying.

```java
@Entity
@Table(name = "student_courses",
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class StudentCourse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDate enrolledDate;
}
```

**Design note:** The `@UniqueConstraint` ensures a student can't enrol in the same course twice, even though we use a surrogate PK.

---

### 4.5 `ResidenceHall` — `module/hall/entity/ResidenceHall.java`

Represents a university residence hall building.

```java
@Entity
@Table(name = "residence_halls")
public class ResidenceHall {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    private String hallName, address, telephoneNumber;
    private Integer totalRooms;
    private String managerName, managerPhone;
}
```

---

### 4.6 `Room` — `module/hall/entity/Room.java`

Represents a room within a residence hall.

```java
@Entity
@Table(name = "rooms")
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private ResidenceHall hall;

    private String roomNumber, placeNumber, roomType;
    private BigDecimal monthlyFee;
    private Boolean isAvailable;          // default: true
}
```

---

### 4.7 `HallPlacement` — `module/hall/entity/HallPlacement.java`

Records which student occupies which room and when. Acts as a **temporal junction** between `Student` and `Room`.

```java
@Entity
@Table(name = "hall_placements")
public class HallPlacement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDate startDate, endDate;
    private Boolean isActive;             // default: true
}
```

---

### 4.8 `ResidenceStaff` — `module/staff/entity/ResidenceStaff.java`

Represents staff members who work in residence halls.

```java
@Entity
@Table(name = "residence_staff")
public class ResidenceStaff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private ResidenceHall hall;

    private String firstName, lastName, email, password;
    private String phone, role;
    private LocalDate dateOfBirth;
    private String street, city, postcode;
    private String gender, location;
    private Boolean isActive;             // default: true
}
```

---

### 4.9 `LeaseAgreement` — `module/lease/entity/LeaseAgreement.java`

Represents a lease/rental agreement between a student and the university for accommodation.

```java
@Entity
@Table(name = "lease_agreements")
public class LeaseAgreement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String leaseNumber;
    private LocalDate startDate, endDate, enterDate, leaveDate;
    private String placeNumber;
    private BigDecimal monthlyRent, depositAmount;

    @Enumerated(EnumType.STRING)
    private Semester semester;            // YEAR1_SEM1, PG_SEM1, SUMMER, etc.

    @Enumerated(EnumType.STRING)
    private LeaseStatus status;           // ACTIVE | EXPIRED | TERMINATED

    private String notes;
}
```

---

### 4.10 `Invoice` — `module/invoice/entity/Invoice.java`

Represents a billing invoice sent to a student.

```java
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String invoiceNumber, description;
    private BigDecimal amount;
    private LocalDate issueDate, dueDate;
    private String semester, paymentMethod;
    private LocalDate firstReminderDate, secondReminderDate, paidDate;
    private String status;                // PENDING | PAID | OVERDUE | CANCELLED
}
```

---

### 4.11 `NextOfKin` — `module/nextofkin/entity/NextOfKin.java`

Emergency contact / next-of-kin information for a student.

```java
@Entity
@Table(name = "next_of_kin")
public class NextOfKin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nokId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String fullName, relationship, phone;
    private String email, street, city, postcode;
}
```

---

### 4.12 `StudentApartment` — `module/apartment/entity/StudentApartment.java`

Represents an off-campus student apartment building.

```java
@Entity
@Table(name = "student_apartments")
public class StudentApartment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apartmentId;

    private String apartmentName, flatNumber, address;
    private Integer totalBedrooms;
    private String managerName, managerPhone;
}
```

---

### 4.13 `Bedroom` — `module/apartment/entity/Bedroom.java`

Represents a bedroom within a student apartment, assignable to a student.

```java
@Entity
@Table(name = "bedrooms")
public class Bedroom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bedroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    private StudentApartment apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String bedroomNumber, placeNumber;
    private BigDecimal monthlyFee;
    private LocalDate startDate, endDate;
    private Boolean isOccupied;           // default: false
}
```

---

### 4.14 `ApartmentInspection` — `module/apartment/entity/ApartmentInspection.java`

Records inspection events performed on an apartment.

```java
@Entity
@Table(name = "apartment_inspections")
public class ApartmentInspection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inspectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    private StudentApartment apartment;

    private LocalDate inspectionDate;
    private String inspectorName, remarks;
    private String status;                // PASSED | FAILED | PENDING
}
```

---

## 5. Repository Interfaces

Each entity has a corresponding `JpaRepository` interface that provides CRUD operations and custom queries.

| Repository                   | Entity              | Notable Custom Queries                                      |
|------------------------------|---------------------|-------------------------------------------------------------|
| `StudentRepository`         | Student             | `findByEmail`, `findByCategory`, `findStudentsWithNoNextOfKin`, `searchByName` |
| `AdviserRepository`         | Adviser             | `findAdviserByStudentId`, `findByDepartment`, `search`      |
| `CourseRepository`          | Course              | `search`, `findByDepartment`, `findCoursesByStudentId`      |
| `StudentCourseRepository`   | StudentCourse       | `findByStudent_StudentId`, `existsByStudent_StudentIdAndCourse_CourseId` |
| `ResidenceHallRepository`   | ResidenceHall       | `search`, `findHallsByManagerName`                          |
| `RoomRepository`            | Room                | `findByHall_HallId`, `findAvailableRooms`                   |
| `HallPlacementRepository`   | HallPlacement       | `findAllActivePlacements`, `findActivePlacementsByHall`     |
| `StaffRepository`           | ResidenceStaff      | `findByHall_HallId`, `findByRole`, `findAllActiveWithHall`  |
| `LeaseRepository`           | LeaseAgreement      | `findLeasesExpiringSoon`, `findBySemesterAndYear`, `findAllActiveWithStudent` |
| `InvoiceRepository`         | Invoice             | `findOverdueInvoices`, `findPendingByStudent`, `findByIssueDateBetween` |
| `NextOfKinRepository`       | NextOfKin           | `findByStudent_StudentId`, `existsByStudent_StudentId`      |
| `ApartmentRepository`       | StudentApartment    | `findApartmentsWithAvailableBedrooms`, `findApartmentsByStudentId` |
| `BedroomRepository`         | Bedroom             | `findByApartment_ApartmentId`, `findByStudent_StudentId`    |
| `InspectionRepository`      | ApartmentInspection | `findByStatus`, `findByApartment_ApartmentIdOrderByInspectionDateDesc` |

---

## 6. Enum Types

### `StudentCategory`
```java
public enum StudentCategory {
    UNDERGRADUATE,
    POSTGRADUATE,
    INTERNATIONAL
}
```

### `LeaseStatus`
```java
public enum LeaseStatus {
    ACTIVE,
    EXPIRED,
    TERMINATED
}
```

### `Semester`
```java
public enum Semester {
    YEAR1_SEM1, YEAR1_SEM2,      // Undergraduate years
    YEAR2_SEM1, YEAR2_SEM2,
    YEAR3_SEM1, YEAR3_SEM2,
    YEAR4_SEM1, YEAR4_SEM2,
    PG_SEM1, PG_SEM2,            // Postgraduate semesters
    SUMMER                       // Summer — all students
}
```

---

## 7. JPA Annotations Cheat-Sheet

| Annotation                  | Purpose                                                    |
|-----------------------------|------------------------------------------------------------|
| `@Entity`                   | Marks the class as a JPA entity (mapped to a DB table)     |
| `@Table(name = "...")`      | Specifies the exact MySQL table name                       |
| `@Id`                       | Marks the primary key field                                |
| `@GeneratedValue(IDENTITY)` | DB auto-increments the PK                                  |
| `@Column(name, nullable, unique, length)` | Maps a field to a specific column with constraints |
| `@ManyToOne(fetch = LAZY)`  | FK relationship; loads related entity only when accessed    |
| `@JoinColumn(name = "...")`| Specifies the FK column name in the owning table           |
| `@Enumerated(EnumType.STRING)` | Stores enum values as their name string, not ordinal    |
| `@Builder.Default`          | Lombok: provides a default value when using the builder    |
| `@UniqueConstraint`         | Table-level composite unique constraint                    |

---

*Generated from the UAMS codebase · Spring Boot 4.0.3 · Hibernate ORM*
