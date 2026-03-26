# UAMS — University Accommodation Management System
## REST API Documentation
**Version:** 1.0.0 | **Spec:** OAS 3.1  
**Base URL:** `http://localhost:8080`  
**Contact:** UAMS Dev Team

---

## Table of Contents

1. [Authentication](#1-authentication)
2. [Students](#2-students)
3. [Next of Kin](#3-next-of-kin)
4. [Staff](#4-staff)
5. [Advisers](#5-advisers)
6. [Leases](#6-leases)
7. [Invoices](#7-invoices)
8. [Residence Halls](#8-residence-halls)
9. [Rooms](#9-rooms)
10. [Apartments](#10-apartments)
11. [Courses](#11-courses)
12. [Reports](#12-reports)
13. [Schemas](#13-schemas)

---

## 1. Authentication

### POST `/api/auth/login`
Authenticate a user and receive access/refresh tokens.

**Request Body:** `LoginRequest`
```json
{
  "email": "string",
  "password": "string"
}
```

**Response:** `ApiResponseLoginResponse`
```json
{
  "success": true,
  "message": "string",
  "data": {
    "accessToken": "string",
    "refreshToken": "string",
    "tokenType": "Bearer"
  }
}
```

---

### POST `/api/auth/refresh`
Refresh an expired access token.

**Request Body:** `RefreshTokenRequest`
```json
{
  "refreshToken": "string"
}
```

**Response:** `ApiResponseLoginResponse` *(same structure as login)*

---

### POST `/api/auth/logout`
Invalidate the current session/token.

**Response:** `ApiResponseVoid`
```json
{
  "success": true,
  "message": "Logged out successfully",
  "data": null
}
```

---

## 2. Students

### GET `/api/students`
Retrieve all students.

**Response:** `ApiResponseListStudentResponse`
```json
{
  "success": true,
  "message": "string",
  "data": [
    {
      "studentId": 1,
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "bannerNumber": "string",
      "dateOfBirth": "2000-01-01",
      "category": "string",
      "waitingList": false,
      "street": "string",
      "city": "string",
      "postcode": "string",
      "mobilePhone": "string",
      "gender": "string",
      "nationality": "string",
      "specialNeeds": "string",
      "additionalComments": "string",
      "major": "string",
      "minor": "string",
      "isActive": true,
      "adviserId": 1
    }
  ]
}
```

---

### POST `/api/students`
Create a new student.

**Request Body:** `StudentRequest`
```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "password": "string",
  "bannerNumber": "string",
  "dateOfBirth": "2000-01-01",
  "category": "string",
  "waitingList": false,
  "street": "string",
  "city": "string",
  "postcode": "string",
  "mobilePhone": "string",
  "gender": "string",
  "nationality": "string",
  "specialNeeds": "string",
  "additionalComments": "string",
  "major": "string",
  "minor": "string",
  "isActive": true,
  "adviserId": 1
}
```

**Response:** `ApiResponseStudentResponse`

---

### GET `/api/students/{id}`
Get a student by ID.

**Path Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| id | integer (int64) | Yes | Student ID |

**Response:** `ApiResponseStudentResponse`

---

### PUT `/api/students/{id}`
Update an existing student.

**Path Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| id | integer (int64) | Yes | Student ID |

**Request Body:** `StudentRequest` *(same as POST)*

**Response:** `ApiResponseStudentResponse`

---

### DELETE `/api/students/{id}`
Delete a student by ID.

**Path Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| id | integer (int64) | Yes | Student ID |

**Response:** `ApiResponseVoid`

---

### GET `/api/students/waiting-list`
Retrieve all students on the waiting list.

**Response:** `ApiResponseListStudentResponse`

---

### GET `/api/students/search`
Search students by keyword.

**Query Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| query | string | Yes | Search term (name, email, banner number, etc.) |

**Response:** `ApiResponseListStudentResponse`

---

## 3. Next of Kin

### GET `/api/students/{studentId}/next-of-kin`
Get all next-of-kin records for a student.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| studentId | integer (int64) | Yes |

**Response:** `ApiResponseListNextOfKinResponse`
```json
{
  "success": true,
  "message": "string",
  "data": [
    {
      "nokId": 1,
      "studentId": 1,
      "fullName": "string",
      "relationship": "string",
      "phone": "string",
      "email": "string",
      "street": "string",
      "city": "string",
      "postcode": "string"
    }
  ]
}
```

---

### POST `/api/students/{studentId}/next-of-kin`
Add a next-of-kin record for a student.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| studentId | integer (int64) | Yes |

**Request Body:** `NextOfKinRequest`
```json
{
  "fullName": "string",
  "relationship": "string",
  "phone": "string",
  "email": "string",
  "street": "string",
  "city": "string",
  "postcode": "string"
}
```

**Response:** `ApiResponseNextOfKinResponse`

---

### GET `/api/students/{studentId}/next-of-kin/{id}`
Get a specific next-of-kin record.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| studentId | integer (int64) | Yes |
| id | integer (int64) | Yes |

**Response:** `ApiResponseNextOfKinResponse`

---

### PUT `/api/students/{studentId}/next-of-kin/{id}`
Update a next-of-kin record.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| studentId | integer (int64) | Yes |
| id | integer (int64) | Yes |

**Request Body:** `NextOfKinRequest`

**Response:** `ApiResponseNextOfKinResponse`

---

### DELETE `/api/students/{studentId}/next-of-kin/{id}`
Delete a next-of-kin record.

**Response:** `ApiResponseVoid`

---

## 4. Staff

### GET `/api/staff`
Retrieve all residence staff.

**Response:** `ApiResponseListStaffResponse`
```json
{
  "success": true,
  "data": [
    {
      "staffId": 1,
      "hallId": 1,
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "phone": "string",
      "role": "string",
      "dateOfBirth": "1980-01-01",
      "street": "string",
      "city": "string",
      "postcode": "string",
      "gender": "string",
      "location": "string",
      "isActive": true
    }
  ]
}
```

---

### POST `/api/staff`
Create a new staff member.

**Request Body:** `StaffRequest`
```json
{
  "hallId": 1,
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "password": "string",
  "phone": "string",
  "role": "string",
  "dateOfBirth": "1980-01-01",
  "street": "string",
  "city": "string",
  "postcode": "string",
  "gender": "string",
  "location": "string",
  "isActive": true
}
```

**Response:** `ApiResponseStaffResponse`

---

### GET `/api/staff/{id}`
Get a staff member by ID.

**Response:** `ApiResponseStaffResponse`

---

### PUT `/api/staff/{id}`
Update a staff member.

**Request Body:** `StaffRequest`

**Response:** `ApiResponseStaffResponse`

---

### DELETE `/api/staff/{id}`
Delete a staff member.

**Response:** `ApiResponseVoid`

---

### GET `/api/staff/search`
Search staff by keyword.

**Query Parameters:**
| Name | Type | Required |
|------|------|----------|
| query | string | Yes |

**Response:** `ApiResponseListStaffResponse`

---

### GET `/api/staff/role/{role}`
Get all staff members by role.

**Path Parameters:**
| Name | Type | Required | Description |
|------|------|----------|-------------|
| role | string | Yes | e.g. `MANAGER`, `WARDEN` |

**Response:** `ApiResponseListStaffResponse`

---

### GET `/api/staff/hall/{hallId}`
Get all staff assigned to a specific hall.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| hallId | integer (int64) | Yes |

**Response:** `ApiResponseListStaffResponse`

---

## 5. Advisers

### GET `/api/advisers`
Get all advisers.

**Response:** `ApiResponseListAdviserResponse`
```json
{
  "success": true,
  "data": [
    {
      "adviserId": 1,
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "phone": "string",
      "department": "string"
    }
  ]
}
```

---

### POST `/api/advisers`
Create a new adviser.

**Request Body:** `AdviserRequest`
```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "password": "string",
  "phone": "string",
  "department": "string"
}
```

**Response:** `ApiResponseAdviserResponse`

---

### GET `/api/advisers/{id}`
Get an adviser by ID.

**Response:** `ApiResponseAdviserResponse`

---

### PUT `/api/advisers/{id}`
Update an adviser.

**Request Body:** `AdviserRequest`

**Response:** `ApiResponseAdviserResponse`

---

### DELETE `/api/advisers/{id}`
Delete an adviser.

**Response:** `ApiResponseVoid`

---

### GET `/api/advisers/search`
Search advisers by keyword.

**Response:** `ApiResponseListAdviserResponse`

---

### GET `/api/advisers/by-student/{studentId}`
Get the adviser assigned to a specific student.

**Response:** `ApiResponseAdviserResponse`

---

## 6. Leases

### GET `/api/leases`
Get all lease agreements.

**Response:** `ApiResponseListLeaseResponse`
```json
{
  "success": true,
  "data": [
    {
      "leaseId": 1,
      "studentId": 1,
      "leaseNumber": "string",
      "startDate": "2024-09-01",
      "endDate": "2025-06-30",
      "enterDate": "2024-09-01",
      "leaveDate": null,
      "placeNumber": "string",
      "monthlyRent": 500.00,
      "depositAmount": 500.00,
      "semester": "string",
      "status": "ACTIVE",
      "notes": "string"
    }
  ]
}
```

---

### POST `/api/leases`
Create a new lease agreement.

**Request Body:** `LeaseRequest`
```json
{
  "studentId": 1,
  "leaseNumber": "string",
  "startDate": "2024-09-01",
  "endDate": "2025-06-30",
  "enterDate": "2024-09-01",
  "leaveDate": null,
  "placeNumber": "string",
  "monthlyRent": 500.00,
  "depositAmount": 500.00,
  "semester": "string",
  "status": "ACTIVE",
  "notes": "string"
}
```

**Response:** `ApiResponseLeaseResponse`

---

### GET `/api/leases/{id}`
Get a lease by ID.

**Response:** `ApiResponseLeaseResponse`

---

### PUT `/api/leases/{id}`
Update a lease.

**Request Body:** `LeaseRequest`

**Response:** `ApiResponseLeaseResponse`

---

### DELETE `/api/leases/{id}`
Delete a lease.

**Response:** `ApiResponseVoid`

---

### PUT `/api/leases/{id}/terminate`
Terminate an active lease early.

**Response:** `ApiResponseLeaseResponse`

---

### GET `/api/leases/student/{studentId}`
Get all leases for a specific student.

**Response:** `ApiResponseListLeaseResponse`

---

### GET `/api/leases/status/{status}`
Get leases filtered by status.

**Path Parameters:**
| Name | Type | Required | Example |
|------|------|----------|---------|
| status | string | Yes | `ACTIVE`, `TERMINATED`, `EXPIRED` |

**Response:** `ApiResponseListLeaseResponse`

---

### GET `/api/leases/semester/{semester}`
Get leases for a given semester.

**Response:** `ApiResponseListLeaseResponse`

---

### GET `/api/leases/expiring-soon`
Get leases expiring in the near future.

**Response:** `ApiResponseListLeaseResponse`

---

## 7. Invoices

### GET `/api/invoices`
Get all invoices.

**Response:** `ApiResponseListInvoiceResponse`
```json
{
  "success": true,
  "data": [
    {
      "invoiceId": 1,
      "studentId": 1,
      "invoiceNumber": "string",
      "description": "string",
      "amount": 500.00,
      "issueDate": "2024-09-01",
      "dueDate": "2024-09-30",
      "semester": "string",
      "paymentMethod": "string",
      "firstReminderDate": null,
      "secondReminderDate": null,
      "paidDate": null,
      "status": "UNPAID"
    }
  ]
}
```

---

### POST `/api/invoices`
Create a new invoice.

**Request Body:** `InvoiceRequest`
```json
{
  "studentId": 1,
  "invoiceNumber": "string",
  "description": "string",
  "amount": 500.00,
  "issueDate": "2024-09-01",
  "dueDate": "2024-09-30",
  "semester": "string",
  "paymentMethod": "string",
  "status": "UNPAID"
}
```

**Response:** `ApiResponseInvoiceResponse`

---

### GET `/api/invoices/{id}`
Get an invoice by ID.

**Response:** `ApiResponseInvoiceResponse`

---

### PUT `/api/invoices/{id}`
Update an invoice.

**Request Body:** `InvoiceRequest`

**Response:** `ApiResponseInvoiceResponse`

---

### DELETE `/api/invoices/{id}`
Delete an invoice.

**Response:** `ApiResponseVoid`

---

### PUT `/api/invoices/{id}/pay`
Mark an invoice as paid.

**Response:** `ApiResponseInvoiceResponse`

---

### GET `/api/invoices/student/{studentId}`
Get all invoices for a student.

**Response:** `ApiResponseListInvoiceResponse`

---

### GET `/api/invoices/status/{status}`
Get invoices by status.

**Path Parameters:**
| Name | Type | Example |
|------|------|---------|
| status | string | `PAID`, `UNPAID`, `OVERDUE` |

**Response:** `ApiResponseListInvoiceResponse`

---

### GET `/api/invoices/overdue`
Get all overdue invoices.

**Response:** `ApiResponseListInvoiceResponse`

---

## 8. Residence Halls

### GET `/api/halls`
Get all residence halls.

**Response:** `ApiResponseListHallResponse`
```json
{
  "success": true,
  "data": [
    {
      "hallId": 1,
      "hallName": "string",
      "address": "string",
      "telephoneNumber": "string",
      "totalRooms": 100,
      "managerName": "string",
      "managerPhone": "string"
    }
  ]
}
```

---

### POST `/api/halls`
Create a new residence hall.

**Request Body:** `HallRequest`
```json
{
  "hallName": "string",
  "address": "string",
  "telephoneNumber": "string",
  "totalRooms": 100,
  "managerName": "string",
  "managerPhone": "string"
}
```

**Response:** `ApiResponseHallResponse`

---

### GET `/api/halls/{id}`
Get a hall by ID.

**Response:** `ApiResponseHallResponse`

---

### PUT `/api/halls/{id}`
Update a hall.

**Request Body:** `HallRequest`

**Response:** `ApiResponseHallResponse`

---

### DELETE `/api/halls/{id}`
Delete a hall.

**Response:** `ApiResponseVoid`

---

### GET `/api/halls/search`
Search halls by keyword.

**Response:** `ApiResponseListHallResponse`

---

### GET `/api/halls/placements`
Get all hall placements.

**Response:** `ApiResponseListPlacementResponse`
```json
{
  "success": true,
  "data": [
    {
      "placementId": 1,
      "studentId": 1,
      "roomId": 1,
      "startDate": "2024-09-01",
      "endDate": null,
      "isActive": true
    }
  ]
}
```

---

### POST `/api/halls/placements`
Create a new hall placement.

**Request Body:** `PlacementRequest`
```json
{
  "studentId": 1,
  "roomId": 1,
  "startDate": "2024-09-01",
  "endDate": null,
  "isActive": true
}
```

**Response:** `ApiResponsePlacementResponse`

---

### PUT `/api/halls/placements/{id}/end`
End (deactivate) a hall placement.

**Response:** `ApiResponsePlacementResponse`

---

### GET `/api/halls/placements/student/{studentId}`
Get all placements for a student.

**Response:** `ApiResponseListPlacementResponse`

---

## 9. Rooms

### GET `/api/halls/{hallId}/rooms`
Get all rooms in a hall.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| hallId | integer (int64) | Yes |

**Response:** `ApiResponseListRoomResponse`
```json
{
  "success": true,
  "data": [
    {
      "roomId": 1,
      "hallId": 1,
      "roomNumber": "string",
      "placeNumber": "string",
      "roomType": "string",
      "monthlyFee": 400.00,
      "isAvailable": true
    }
  ]
}
```

---

### POST `/api/halls/{hallId}/rooms`
Add a room to a hall.

**Request Body:** `RoomRequest`
```json
{
  "roomNumber": "string",
  "placeNumber": "string",
  "roomType": "string",
  "monthlyFee": 400.00,
  "isAvailable": true
}
```

**Response:** `ApiResponseRoomResponse`

---

### GET `/api/halls/{hallId}/rooms/{roomId}`
Get a specific room.

**Response:** `ApiResponseRoomResponse`

---

### PUT `/api/halls/{hallId}/rooms/{roomId}`
Update a room.

**Request Body:** `RoomRequest`

**Response:** `ApiResponseRoomResponse`

---

### DELETE `/api/halls/{hallId}/rooms/{roomId}`
Delete a room.

**Response:** `ApiResponseVoid`

---

### GET `/api/halls/{hallId}/rooms/available`
Get all available rooms in a hall.

**Response:** `ApiResponseListRoomResponse`

---

## 10. Apartments

### GET `/api/apartments`
Get all student apartments.

**Response:** `ApiResponseListApartmentResponse`
```json
{
  "success": true,
  "data": [
    {
      "apartmentId": 1,
      "apartmentName": "string",
      "flatNumber": "string",
      "address": "string",
      "totalBedrooms": 4,
      "managerName": "string",
      "managerPhone": "string"
    }
  ]
}
```

---

### POST `/api/apartments`
Create a new apartment.

**Request Body:** `ApartmentRequest`
```json
{
  "apartmentName": "string",
  "flatNumber": "string",
  "address": "string",
  "totalBedrooms": 4,
  "managerName": "string",
  "managerPhone": "string"
}
```

**Response:** `ApiResponseApartmentResponse`

---

### GET `/api/apartments/{id}`
Get an apartment by ID.

**Response:** `ApiResponseApartmentResponse`

---

### PUT `/api/apartments/{id}`
Update an apartment.

**Request Body:** `ApartmentRequest`

**Response:** `ApiResponseApartmentResponse`

---

### DELETE `/api/apartments/{id}`
Delete an apartment.

**Response:** `ApiResponseVoid`

---

### GET `/api/apartments/search`
Search apartments by keyword.

**Response:** `ApiResponseListApartmentResponse`

---

### GET `/api/apartments/{id}/bedrooms`
Get all bedrooms in an apartment.

**Response:** `ApiResponseListBedroomResponse`
```json
{
  "success": true,
  "data": [
    {
      "bedroomId": 1,
      "apartmentId": 1,
      "studentId": null,
      "bedroomNumber": "string",
      "placeNumber": "string",
      "monthlyFee": 350.00,
      "startDate": "2024-09-01",
      "endDate": null,
      "isOccupied": false
    }
  ]
}
```

---

### POST `/api/apartments/{id}/bedrooms`
Add a bedroom to an apartment.

**Request Body:** `BedroomRequest`
```json
{
  "studentId": null,
  "bedroomNumber": "string",
  "placeNumber": "string",
  "monthlyFee": 350.00,
  "startDate": "2024-09-01",
  "endDate": null,
  "isOccupied": false
}
```

**Response:** `ApiResponseBedroomResponse`

---

### PUT `/api/apartments/{id}/bedrooms/{bedroomId}`
Update a bedroom.

**Request Body:** `BedroomRequest`

**Response:** `ApiResponseBedroomResponse`

---

### DELETE `/api/apartments/{id}/bedrooms/{bedroomId}`
Delete a bedroom.

**Response:** `ApiResponseVoid`

---

### GET `/api/apartments/{id}/bedrooms/available`
Get available bedrooms in an apartment.

**Response:** `ApiResponseListBedroomResponse`

---

### GET `/api/apartments/{id}/inspections`
Get all inspections for an apartment.

**Response:** `ApiResponseListInspectionResponse`
```json
{
  "success": true,
  "data": [
    {
      "inspectionId": 1,
      "apartmentId": 1,
      "inspectionDate": "2024-10-01",
      "inspectorName": "string",
      "remarks": "string",
      "status": "SATISFACTORY"
    }
  ]
}
```

---

### POST `/api/apartments/{id}/inspections`
Add an inspection to an apartment.

**Request Body:** `InspectionRequest`
```json
{
  "inspectionDate": "2024-10-01",
  "inspectorName": "string",
  "remarks": "string",
  "status": "SATISFACTORY"
}
```

**Response:** `ApiResponseInspectionResponse`

---

### PUT `/api/apartments/{id}/inspections/{inspectionId}`
Update an inspection record.

**Request Body:** `InspectionRequest`

**Response:** `ApiResponseInspectionResponse`

---

### DELETE `/api/apartments/{id}/inspections/{inspectionId}`
Delete an inspection record.

**Response:** `ApiResponseVoid`

---

## 11. Courses

### GET `/api/courses`
Get all courses.

**Response:** `ApiResponseListCourseResponse`
```json
{
  "success": true,
  "data": [
    {
      "courseId": 1,
      "courseName": "string",
      "courseCode": "string",
      "department": "string",
      "instructorName": "string",
      "instructorPhone": "string",
      "instructorEmail": "string",
      "instructorRoomNumber": "string"
    }
  ]
}
```

---

### POST `/api/courses`
Create a new course.

**Request Body:** `CourseRequest`
```json
{
  "courseName": "string",
  "courseCode": "string",
  "department": "string",
  "instructorName": "string",
  "instructorPhone": "string",
  "instructorEmail": "string",
  "instructorRoomNumber": "string"
}
```

**Response:** `ApiResponseCourseResponse`

---

### GET `/api/courses/{id}`
Get a course by ID.

**Response:** `ApiResponseCourseResponse`

---

### PUT `/api/courses/{id}`
Update a course.

**Request Body:** `CourseRequest`

**Response:** `ApiResponseCourseResponse`

---

### DELETE `/api/courses/{id}`
Delete a course.

**Response:** `ApiResponseVoid`

---

### GET `/api/courses/search`
Search courses by keyword.

**Response:** `ApiResponseListCourseResponse`

---

### POST `/api/courses/{courseId}/enrol/{studentId}`
Enrol a student in a course.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| courseId | integer (int64) | Yes |
| studentId | integer (int64) | Yes |

**Response:** `ApiResponseVoid`

---

### DELETE `/api/courses/{courseId}/enrol/{studentId}`
Remove a student from a course.

**Response:** `ApiResponseVoid`

---

### GET `/api/courses/by-student/{studentId}`
Get all courses a student is enrolled in.

**Response:** `ApiResponseListCourseResponse`

---

## 12. Reports

All report endpoints use `GET` and require authentication.

### GET `/api/reports/waiting-list`
Students currently on the accommodation waiting list.

**Response:** `ApiResponseListWaitingListReport`
```json
{
  "data": [
    {
      "studentId": 1,
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "category": "string"
    }
  ]
}
```

---

### GET `/api/reports/unsatisfactory-inspections`
Apartments with unsatisfactory inspection results.

**Response:** `ApiResponseListInspectionReport`
```json
{
  "data": [
    {
      "inspectionId": 1,
      "apartmentId": 1,
      "apartmentName": "string",
      "inspectionDate": "2024-10-01",
      "inspectorName": "string",
      "remarks": "string",
      "status": "UNSATISFACTORY"
    }
  ]
}
```

---

### GET `/api/reports/unpaid-invoices`
All currently unpaid invoices.

**Response:** `ApiResponseListUnpaidInvoiceReport`
```json
{
  "data": [
    {
      "invoiceId": 1,
      "studentId": 1,
      "studentName": "string",
      "invoiceNumber": "string",
      "amount": 500.00,
      "dueDate": "2024-09-30",
      "status": "UNPAID"
    }
  ]
}
```

---

### GET `/api/reports/total-rent-paid/{studentId}`
Total rent paid by a specific student.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| studentId | integer (int64) | Yes |

**Response:** `ApiResponseTotalRentReport`
```json
{
  "data": {
    "studentId": 1,
    "studentName": "string",
    "totalPaid": 3000.00,
    "paidInvoices": [
      {
        "invoiceNumber": "string",
        "amount": 500.00,
        "paidDate": "2024-09-15"
      }
    ]
  }
}
```

---

### GET `/api/reports/summer-leases`
All leases active during the summer period.

**Response:** `ApiResponseListSummerLeaseReport`

---

### GET `/api/reports/students-with-leases`
Students who currently have an active lease.

**Response:** `ApiResponseListStudentLeaseReport`

---

### GET `/api/reports/students-no-next-of-kin`
Students who have no next-of-kin records on file.

**Response:** `ApiResponseListNoNextOfKinReport`

---

### GET `/api/reports/students-in-hall/{hallId}`
All students placed in a specific residence hall.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| hallId | integer (int64) | Yes |

**Response:** `ApiResponseListHallStudentReport`

---

### GET `/api/reports/students-by-category`
Students grouped by their category (e.g. undergraduate, postgraduate).

**Response:** `ApiResponseStudentCategoryReport`

---

### GET `/api/reports/staff-over-sixty`
Staff members over the age of 60.

**Response:** `ApiResponseListStaffAgeReport`

---

### GET `/api/reports/rent-statistics`
Statistical summary of rent across the system (min, max, average).

**Response:** `ApiResponseRentStatisticsReport`
```json
{
  "data": {
    "averageRent": 450.00,
    "minimumRent": 300.00,
    "maximumRent": 750.00,
    "totalRevenue": 225000.00
  }
}
```

---

### GET `/api/reports/places-per-hall`
Number of available/total places in each residence hall.

**Response:** `ApiResponseListHallPlacesReport`

---

### GET `/api/reports/hall-managers`
List of all hall managers and their contact details.

**Response:** `ApiResponseListHallManagerReport`

---

### GET `/api/reports/adviser-for-student/{studentId}`
Get the assigned adviser details for a student.

**Path Parameters:**
| Name | Type | Required |
|------|------|----------|
| studentId | integer (int64) | Yes |

**Response:** `ApiResponseAdviserReport`

---

## 13. Schemas

### Generic API Response Wrapper

All endpoints return responses in the following envelope structure:

```json
{
  "success": true,
  "message": "string",
  "data": { }
}
```

| Field | Type | Description |
|-------|------|-------------|
| success | boolean | Whether the request succeeded |
| message | string | Human-readable message |
| data | object / array / null | The response payload |

---

### Database Schema Summary

| Table | Primary Key | Key Foreign Keys |
|-------|-------------|-----------------|
| `students` | `student_id` | `adviser_id → advisers` |
| `advisers` | `adviser_id` | — |
| `next_of_kin` | `nok_id` | `student_id → students` |
| `residence_halls` | `hall_id` | — |
| `residence_staff` | `staff_id` | `hall_id → residence_halls` |
| `rooms` | `room_id` | `hall_id → residence_halls` |
| `hall_placements` | `placement_id` | `student_id → students`, `room_id → rooms` |
| `student_apartments` | `apartment_id` | — |
| `bedrooms` | `bedroom_id` | `apartment_id → student_apartments`, `student_id → students` |
| `apartment_inspections` | `inspection_id` | `apartment_id → student_apartments` |
| `lease_agreements` | `lease_id` | `student_id → students` |
| `invoices` | `invoice_id` | `student_id → students` |
| `courses` | `course_id` | — |
| `student_courses` | `id` | `student_id → students`, `course_id → courses` |

---

*Generated for UAMS v1.0.0 — University Accommodation Management System*
