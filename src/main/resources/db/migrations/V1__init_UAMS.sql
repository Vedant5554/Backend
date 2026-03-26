CREATE TABLE advisers
(
    adviser_id BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NULL,
    phone      VARCHAR(20) NULL,
    department VARCHAR(200) NULL,
    CONSTRAINT pk_advisers PRIMARY KEY (adviser_id)
);

CREATE TABLE apartment_inspections
(
    inspection_id   BIGINT AUTO_INCREMENT NOT NULL,
    apartment_id    BIGINT NOT NULL,
    inspection_date date   NOT NULL,
    inspector_name  VARCHAR(200) NULL,
    remarks         TEXT NULL,
    status          VARCHAR(50) NULL,
    CONSTRAINT pk_apartment_inspections PRIMARY KEY (inspection_id)
);

CREATE TABLE bedrooms
(
    bedroom_id     BIGINT AUTO_INCREMENT NOT NULL,
    apartment_id   BIGINT      NOT NULL,
    student_id     BIGINT NULL,
    bedroom_number VARCHAR(20) NOT NULL,
    place_number   VARCHAR(50) NULL,
    monthly_fee    DECIMAL(10, 2) NULL,
    start_date     date NULL,
    end_date       date NULL,
    is_occupied    BIT(1) NULL,
    CONSTRAINT pk_bedrooms PRIMARY KEY (bedroom_id)
);

CREATE TABLE courses
(
    course_id              BIGINT AUTO_INCREMENT NOT NULL,
    course_name            VARCHAR(200) NOT NULL,
    course_code            VARCHAR(20) NULL,
    department             VARCHAR(200) NULL,
    instructor_name        VARCHAR(200) NULL,
    instructor_phone       VARCHAR(20) NULL,
    instructor_email       VARCHAR(255) NULL,
    instructor_room_number VARCHAR(50) NULL,
    CONSTRAINT pk_courses PRIMARY KEY (course_id)
);

CREATE TABLE hall_placements
(
    placement_id BIGINT AUTO_INCREMENT NOT NULL,
    student_id   BIGINT NOT NULL,
    room_id      BIGINT NOT NULL,
    start_date   date   NOT NULL,
    end_date     date NULL,
    is_active    BIT(1) NULL,
    CONSTRAINT pk_hall_placements PRIMARY KEY (placement_id)
);

CREATE TABLE invoices
(
    invoice_id           BIGINT AUTO_INCREMENT NOT NULL,
    student_id           BIGINT         NOT NULL,
    invoice_number       VARCHAR(50) NULL,
    `description`        TEXT NULL,
    amount               DECIMAL(10, 2) NOT NULL,
    issue_date           date           NOT NULL,
    due_date             date NULL,
    semester             VARCHAR(20) NULL,
    payment_method       VARCHAR(50) NULL,
    first_reminder_date  date NULL,
    second_reminder_date date NULL,
    paid_date            date NULL,
    status               VARCHAR(20) NULL,
    CONSTRAINT pk_invoices PRIMARY KEY (invoice_id)
);

CREATE TABLE lease_agreements
(
    lease_id       BIGINT AUTO_INCREMENT NOT NULL,
    student_id     BIGINT NOT NULL,
    lease_number   VARCHAR(50) NULL,
    start_date     date   NOT NULL,
    end_date       date   NOT NULL,
    enter_date     date NULL,
    leave_date     date NULL,
    place_number   VARCHAR(50) NULL,
    monthly_rent   DECIMAL(10, 2) NULL,
    deposit_amount DECIMAL(10, 2) NULL,
    semester       VARCHAR(10) NULL,
    status         VARCHAR(20) NULL,
    notes          TEXT NULL,
    CONSTRAINT pk_lease_agreements PRIMARY KEY (lease_id)
);

CREATE TABLE next_of_kin
(
    nok_id       BIGINT AUTO_INCREMENT NOT NULL,
    student_id   BIGINT       NOT NULL,
    full_name    VARCHAR(200) NOT NULL,
    relationship VARCHAR(100) NULL,
    phone        VARCHAR(20)  NOT NULL,
    email        VARCHAR(255) NULL,
    street       VARCHAR(200) NULL,
    city         VARCHAR(100) NULL,
    postcode     VARCHAR(20) NULL,
    CONSTRAINT pk_next_of_kin PRIMARY KEY (nok_id)
);

CREATE TABLE residence_halls
(
    hall_id          BIGINT AUTO_INCREMENT NOT NULL,
    hall_name        VARCHAR(200) NOT NULL,
    address          VARCHAR(500) NULL,
    telephone_number VARCHAR(20) NULL,
    total_rooms      INT NULL,
    manager_name     VARCHAR(200) NULL,
    manager_phone    VARCHAR(20) NULL,
    CONSTRAINT pk_residence_halls PRIMARY KEY (hall_id)
);

CREATE TABLE residence_staff
(
    staff_id      BIGINT AUTO_INCREMENT NOT NULL,
    hall_id       BIGINT NULL,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NULL,
    password      VARCHAR(255) NULL,
    phone         VARCHAR(20) NULL,
    `role`        VARCHAR(100) NULL,
    date_of_birth date NULL,
    street        VARCHAR(200) NULL,
    city          VARCHAR(100) NULL,
    postcode      VARCHAR(20) NULL,
    gender        VARCHAR(20) NULL,
    location      VARCHAR(100) NULL,
    is_active     BIT(1) NULL,
    CONSTRAINT pk_residence_staff PRIMARY KEY (staff_id)
);

CREATE TABLE rooms
(
    room_id      BIGINT AUTO_INCREMENT NOT NULL,
    hall_id      BIGINT      NOT NULL,
    room_number  VARCHAR(20) NOT NULL,
    place_number VARCHAR(50) NULL,
    room_type    VARCHAR(100) NULL,
    monthly_fee  DECIMAL(10, 2) NULL,
    is_available BIT(1) NULL,
    CONSTRAINT pk_rooms PRIMARY KEY (room_id)
);

CREATE TABLE student_apartments
(
    apartment_id   BIGINT AUTO_INCREMENT NOT NULL,
    apartment_name VARCHAR(200) NOT NULL,
    flat_number    VARCHAR(50) NULL,
    address        VARCHAR(500) NULL,
    total_bedrooms INT NULL,
    manager_name   VARCHAR(200) NULL,
    manager_phone  VARCHAR(20) NULL,
    CONSTRAINT pk_student_apartments PRIMARY KEY (apartment_id)
);

CREATE TABLE student_courses
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    student_id    BIGINT NOT NULL,
    course_id     BIGINT NOT NULL,
    enrolled_date date NULL,
    CONSTRAINT pk_student_courses PRIMARY KEY (id)
);

CREATE TABLE students
(
    student_id          BIGINT AUTO_INCREMENT NOT NULL,
    first_name          VARCHAR(100) NOT NULL,
    last_name           VARCHAR(100) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    password            VARCHAR(255) NOT NULL,
    banner_number       VARCHAR(50) NULL,
    date_of_birth       date         NOT NULL,
    category            VARCHAR(20)  NOT NULL,
    waiting_list        BIT(1) NULL,
    street              VARCHAR(200) NULL,
    city                VARCHAR(100) NULL,
    postcode            VARCHAR(20) NULL,
    mobile_phone        VARCHAR(20) NULL,
    gender              VARCHAR(20) NULL,
    nationality         VARCHAR(100) NULL,
    special_needs       TEXT NULL,
    additional_comments TEXT NULL,
    major               VARCHAR(100) NULL,
    minor               VARCHAR(100) NULL,
    is_active           BIT(1) NULL,
    adviser_id          BIGINT NULL,
    CONSTRAINT pk_students PRIMARY KEY (student_id)
);

ALTER TABLE student_courses
    ADD CONSTRAINT uc_8018727f82ce0ee7ef4a61d7e UNIQUE (student_id, course_id);

ALTER TABLE advisers
    ADD CONSTRAINT uc_advisers_email UNIQUE (email);

ALTER TABLE bedrooms
    ADD CONSTRAINT uc_bedrooms_place_number UNIQUE (place_number);

ALTER TABLE courses
    ADD CONSTRAINT uc_courses_course_code UNIQUE (course_code);

ALTER TABLE invoices
    ADD CONSTRAINT uc_invoices_invoice_number UNIQUE (invoice_number);

ALTER TABLE lease_agreements
    ADD CONSTRAINT uc_lease_agreements_lease_number UNIQUE (lease_number);

ALTER TABLE residence_staff
    ADD CONSTRAINT uc_residence_staff_email UNIQUE (email);

ALTER TABLE rooms
    ADD CONSTRAINT uc_rooms_place_number UNIQUE (place_number);

ALTER TABLE student_apartments
    ADD CONSTRAINT uc_student_apartments_flat_number UNIQUE (flat_number);

ALTER TABLE students
    ADD CONSTRAINT uc_students_banner_number UNIQUE (banner_number);

ALTER TABLE students
    ADD CONSTRAINT uc_students_email UNIQUE (email);

ALTER TABLE apartment_inspections
    ADD CONSTRAINT FK_APARTMENT_INSPECTIONS_ON_APARTMENT FOREIGN KEY (apartment_id) REFERENCES student_apartments (apartment_id);

ALTER TABLE bedrooms
    ADD CONSTRAINT FK_BEDROOMS_ON_APARTMENT FOREIGN KEY (apartment_id) REFERENCES student_apartments (apartment_id);

ALTER TABLE bedrooms
    ADD CONSTRAINT FK_BEDROOMS_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE hall_placements
    ADD CONSTRAINT FK_HALL_PLACEMENTS_ON_ROOM FOREIGN KEY (room_id) REFERENCES rooms (room_id);

ALTER TABLE hall_placements
    ADD CONSTRAINT FK_HALL_PLACEMENTS_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE invoices
    ADD CONSTRAINT FK_INVOICES_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE lease_agreements
    ADD CONSTRAINT FK_LEASE_AGREEMENTS_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE next_of_kin
    ADD CONSTRAINT FK_NEXT_OF_KIN_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE residence_staff
    ADD CONSTRAINT FK_RESIDENCE_STAFF_ON_HALL FOREIGN KEY (hall_id) REFERENCES residence_halls (hall_id);

ALTER TABLE rooms
    ADD CONSTRAINT FK_ROOMS_ON_HALL FOREIGN KEY (hall_id) REFERENCES residence_halls (hall_id);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_ADVISER FOREIGN KEY (adviser_id) REFERENCES advisers (adviser_id);

ALTER TABLE student_courses
    ADD CONSTRAINT FK_STUDENT_COURSES_ON_COURSE FOREIGN KEY (course_id) REFERENCES courses (course_id);

ALTER TABLE student_courses
    ADD CONSTRAINT FK_STUDENT_COURSES_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (student_id);