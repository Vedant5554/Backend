package com.example.uams.seeder;

import com.example.uams.module.adviser.entity.Adviser;
import com.example.uams.module.adviser.repository.AdviserRepository;
import com.example.uams.module.apartment.entity.ApartmentInspection;
import com.example.uams.module.apartment.entity.Bedroom;
import com.example.uams.module.apartment.entity.StudentApartment;
import com.example.uams.module.apartment.repository.ApartmentRepository;
import com.example.uams.module.apartment.repository.BedroomRepository;
import com.example.uams.module.apartment.repository.InspectionRepository;
import com.example.uams.module.course.entity.Course;
import com.example.uams.module.course.entity.StudentCourse;
import com.example.uams.module.course.repository.CourseRepository;
import com.example.uams.module.course.repository.StudentCourseRepository;
import com.example.uams.module.hall.entity.HallPlacement;
import com.example.uams.module.hall.entity.ResidenceHall;
import com.example.uams.module.hall.entity.Room;
import com.example.uams.module.hall.repository.HallPlacementRepository;
import com.example.uams.module.hall.repository.ResidenceHallRepository;
import com.example.uams.module.hall.repository.RoomRepository;
import com.example.uams.module.invoice.entity.Invoice;
import com.example.uams.module.invoice.repository.InvoiceRepository;
import com.example.uams.module.lease.entity.LeaseAgreement;
import com.example.uams.module.lease.entity.LeaseStatus;
import com.example.uams.module.lease.entity.Semester;
import com.example.uams.module.lease.repository.LeaseRepository;
import com.example.uams.module.nextofkin.entity.NextOfKin;
import com.example.uams.module.nextofkin.repository.NextOfKinRepository;
import com.example.uams.module.staff.entity.ResidenceStaff;
import com.example.uams.module.staff.repository.StaffRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.entity.StudentCategory;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Profile("seed")
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final AdviserRepository     adviserRepository;
    private final ResidenceHallRepository hallRepository;
    private final StaffRepository       staffRepository;
    private final StudentRepository     studentRepository;
    private final NextOfKinRepository   nextOfKinRepository;
    private final RoomRepository        roomRepository;
    private final HallPlacementRepository placementRepository;
    private final ApartmentRepository   apartmentRepository;
    private final BedroomRepository     bedroomRepository;
    private final InspectionRepository  inspectionRepository;
    private final CourseRepository      courseRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final LeaseRepository       leaseRepository;
    private final InvoiceRepository     invoiceRepository;
    private final PasswordEncoder       passwordEncoder;

    private final Random random = new Random(42);

    // ── Name pools ────────────────────────────────────────────────────────────
    private static final String[] FIRST_NAMES = {
            "James","John","Robert","Michael","William","David","Richard","Joseph","Thomas","Charles",
            "Alice","Mary","Patricia","Jennifer","Linda","Barbara","Elizabeth","Susan","Jessica","Sarah",
            "Christopher","Daniel","Matthew","Anthony","Donald","Mark","Paul","Steven","Andrew","Kenneth",
            "Emily","Emma","Olivia","Sophia","Ava","Isabella","Mia","Charlotte","Amelia","Harper",
            "Kevin","Brian","George","Timothy","Ronald","Edward","Jason","Jeffrey","Ryan","Jacob",
            "Aisha","Fatima","Priya","Mei","Yuki","Amara","Zara","Layla","Nadia","Sana",
            "Mohammed","Ahmed","Ali","Omar","Hassan","Yusuf","Ibrahim","Adam","Tariq","Bilal",
            "Liam","Noah","Ethan","Lucas","Mason","Logan","Oliver","Elijah","Aiden","Carter",
            "Grace","Lily","Chloe","Zoey","Stella","Hazel","Violet","Aurora","Savannah","Bella",
            "Diego","Carlos","Miguel","Luis","Juan","Pedro","Andres","Felipe","Rafael","Sebastian"
    };

    private static final String[] LAST_NAMES = {
            "Smith","Johnson","Williams","Brown","Jones","Garcia","Miller","Davis","Wilson","Taylor",
            "Anderson","Thomas","Jackson","White","Harris","Martin","Thompson","Moore","Young","Allen",
            "King","Wright","Scott","Torres","Nguyen","Hill","Flores","Green","Adams","Nelson",
            "Baker","Hall","Rivera","Campbell","Mitchell","Carter","Roberts","Gomez","Phillips","Evans",
            "Turner","Diaz","Parker","Cruz","Edwards","Collins","Reyes","Stewart","Morris","Sanchez",
            "Patel","Sharma","Singh","Kumar","Gupta","Shah","Mehta","Chopra","Joshi","Nair",
            "Chen","Wang","Li","Zhang","Liu","Yang","Huang","Wu","Zhou","Sun",
            "Kim","Lee","Park","Choi","Jung","Yoon","Lim","Han","Oh","Kwon",
            "Murphy","OBrien","Walsh","Ryan","OConnor","McCarthy","Fitzgerald","Kelly","Burke","Lynch",
            "Johansson","Andersson","Karlsson","Nilsson","Eriksson","Larsson","Olsson","Persson","Svensson","Gustafsson"
    };

    private static final String[] DEPARTMENTS = {
            "Computer Science","Mathematics","Physics","Chemistry","Biology",
            "Engineering","Economics","Psychology","Sociology","History",
            "Literature","Philosophy","Law","Medicine","Architecture",
            "Business Administration","Political Science","Environmental Science",
            "Statistics","Linguistics"
    };

    private static final String[] CITIES = {
            "London","Manchester","Birmingham","Leeds","Glasgow",
            "Edinburgh","Bristol","Liverpool","Sheffield","Newcastle"
    };

    private static final String[] STREETS = {
            "Oak Street","Maple Avenue","University Road","College Lane","Park View",
            "High Street","Queen Street","King Road","Victoria Avenue","Station Road"
    };

    private static final String[] POSTCODES = {
            "SW1A 1AA","M1 1AE","B1 1BB","LS1 1BA","G1 1AF",
            "EH1 1YZ","BS1 1AA","L1 1JD","S1 2HH","NE1 1ST"
    };

    private static final String[] NATIONALITIES = {
            "British","Irish","Indian","Chinese","Nigerian","Pakistani",
            "American","German","French","Italian","Spanish","Brazilian",
            "Australian","Canadian","South African"
    };

    private static final String[] MAJORS = {
            "Computer Science","Software Engineering","Data Science","Artificial Intelligence",
            "Cybersecurity","Mathematics","Physics","Chemistry","Biology","Economics",
            "Psychology","Business","Law","Medicine","Architecture"
    };

    private static final String[] STAFF_ROLES = {
            "Hall Manager","Assistant Manager","Maintenance Staff",
            "Reception Staff","Security Staff","Cleaning Staff"
    };

    private static final String[] LOCATIONS = {
            "Residence Office","Main Hall","Block A","Block B",
            "Block C","Security Gate","Reception Desk"
    };

    private static final String[] GENDERS       = {"Male","Female","Other"};
    private static final String[] RELATIONSHIPS = {"Mother","Father","Sibling","Spouse","Guardian","Uncle","Aunt"};
    private static final String[] ROOM_TYPES    = {"Single","Double","En-Suite"};
    private static final String[] PAY_METHODS   = {"CASH","BANK_TRANSFER","CARD","CHEQUE"};
    private static final BigDecimal[] FEES       = {
            new BigDecimal("350"), new BigDecimal("400"), new BigDecimal("450"),
            new BigDecimal("500"), new BigDecimal("550"), new BigDecimal("600")
    };

    // ── Entry point ───────────────────────────────────────────────────────────

    @Override
    public void run(String... args) {
        log.info("═══════════════════════════════════════════");
        log.info("  UAMS Database Seeder starting...");
        log.info("═══════════════════════════════════════════");

        if (adviserRepository.count() > 0) {
            log.info("Data already exists — skipping seed. Delete all rows first to re-seed.");
            return;
        }

        List<Adviser>       advisers  = seedAdvisers();
        List<ResidenceHall> halls     = seedHalls();
        seedStaff(halls);
        List<Student>       students  = seedStudents(advisers);
        seedNextOfKin(students);
        List<Room>          rooms     = seedRooms(halls);
        seedHallPlacements(students, rooms);
        List<StudentApartment> apts   = seedApartments();
        seedBedrooms(apts, students);
        seedInspections(apts);
        List<Course>        courses   = seedCourses();
        seedStudentCourses(students, courses);
        List<LeaseAgreement> leases   = seedLeases(students);
        seedInvoices(leases);

        log.info("═══════════════════════════════════════════");
        log.info("  Seeding complete!");
        log.info("  Advisers : 100  → adviser1@uni.edu  / adviser1@Uni");
        log.info("  Students : 1000 → student1@uni.edu  / student1@Uni");
        log.info("  Staff    : 50   → staff1@uni.edu    / staff1@Uni");
        log.info("═══════════════════════════════════════════");
    }

    // ── 1. Advisers ───────────────────────────────────────────────────────────

    private List<Adviser> seedAdvisers() {
        log.info("Seeding 100 advisers...");
        List<Adviser> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(adviserRepository.save(Adviser.builder()
                    .firstName(pick(FIRST_NAMES))
                    .lastName(pick(LAST_NAMES))
                    .email("adviser" + i + "@uni.edu")
                    .password(passwordEncoder.encode("adviser" + i + "@Uni"))
                    .phone(phone())
                    .department(pick(DEPARTMENTS))   // ← different dept each time
                    .build()));
        }
        return list;
    }

    // ── 2. Residence Halls ────────────────────────────────────────────────────

    private List<ResidenceHall> seedHalls() {
        log.info("Seeding 10 halls...");
        String[] names = {
                "Maple Hall","Oak Hall","Cedar Hall","Birch Hall","Elm Hall",
                "Pine Hall","Willow Hall","Ash Hall","Beech Hall","Walnut Hall"
        };
        List<ResidenceHall> list = new ArrayList<>();
        for (String name : names) {
            ResidenceHall hall = new ResidenceHall();
            hall.setHallName(name);
            hall.setAddress(random.nextInt(200) + 1 + " " + pick(STREETS) + ", " + pick(CITIES));
            hall.setTelephoneNumber("+44 20 " + (1000 + random.nextInt(8999)) + " " + (1000 + random.nextInt(8999)));
            hall.setManagerName(pick(FIRST_NAMES) + " " + pick(LAST_NAMES));
            hall.setManagerPhone(phone());
            hall.setTotalRooms(20);
            list.add(hallRepository.save(hall));
        }
        return list;
    }

    // ── 3. Staff ──────────────────────────────────────────────────────────────

    private void seedStaff(List<ResidenceHall> halls) {
        log.info("Seeding 50 staff...");
        for (int i = 1; i <= 50; i++) {
            ResidenceStaff s = new ResidenceStaff();
            s.setFirstName(pick(FIRST_NAMES));
            s.setLastName(pick(LAST_NAMES));
            s.setEmail("staff" + i + "@uni.edu");
            s.setPassword(passwordEncoder.encode("staff" + i + "@Uni"));
            s.setPhone(phone());
            s.setRole(pick(STAFF_ROLES));
            s.setDateOfBirth(randomDate(1955, 1990));
            s.setGender(pick(GENDERS));
            s.setStreet(pick(STREETS));
            s.setCity(pick(CITIES));
            s.setPostcode(pick(POSTCODES));
            s.setLocation(pick(LOCATIONS));
            s.setHall(halls.get(random.nextInt(halls.size())));
            s.setIsActive(true);
            staffRepository.save(s);
        }
    }

    // ── 4. Students ───────────────────────────────────────────────────────────

    private List<Student> seedStudents(List<Adviser> advisers) {
        log.info("Seeding 1000 students...");
        StudentCategory[] cats = StudentCategory.values();
        List<Student> list = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            Student s = new Student();
            s.setFirstName(pick(FIRST_NAMES));
            s.setLastName(pick(LAST_NAMES));
            s.setEmail("student" + i + "@uni.edu");
            s.setPassword(passwordEncoder.encode("student" + i + "@Uni"));
            s.setDateOfBirth(randomDate(1995, 2003));
            s.setStreet(pick(STREETS));
            s.setCity(pick(CITIES));
            s.setPostcode(pick(POSTCODES));
            s.setMobilePhone(phone());
            s.setGender(pick(GENDERS));
            s.setNationality(pick(NATIONALITIES));
            s.setCategory(cats[random.nextInt(cats.length)]);
            s.setMajor(pick(MAJORS));
            s.setMinor(pick(MAJORS));
            s.setWaitingList(i > 900);   // last 100 on waiting list
            s.setIsActive(true);
            s.setAdviser(advisers.get(random.nextInt(advisers.size())));
            s.setBannerNumber("B" + String.format("%07d", i));
            list.add(studentRepository.save(s));
        }
        return list;
    }

    // ── 5. Next of Kin ────────────────────────────────────────────────────────

    private void seedNextOfKin(List<Student> students) {
        log.info("Seeding next of kin for 800 students (200 left without for Report j)...");
        List<Student> shuffled = new ArrayList<>(students);
        Collections.shuffle(shuffled, random);
        List<Student> withNok = shuffled.subList(0, 800);

        for (Student student : withNok) {
            NextOfKin nok = new NextOfKin();
            nok.setStudent(student);
            nok.setFullName(pick(FIRST_NAMES) + " " + pick(LAST_NAMES));
            nok.setRelationship(pick(RELATIONSHIPS));
            nok.setStreet(student.getStreet());
            nok.setCity(student.getCity());
            nok.setPostcode(student.getPostcode());
            nok.setPhone(phone());
            nok.setEmail("nok." + student.getStudentId() + "@email.com");
            nextOfKinRepository.save(nok);
        }
    }

    // ── 6. Rooms ──────────────────────────────────────────────────────────────

    private List<Room> seedRooms(List<ResidenceHall> halls) {
        log.info("Seeding 200 rooms (100 occupied, 100 available)...");
        List<Room> list = new ArrayList<>();
        for (ResidenceHall hall : halls) {
            for (int r = 1; r <= 20; r++) {
                Room room = new Room();
                room.setHall(hall);
                room.setRoomNumber(hall.getHallId() + String.format("%02d", r));
                room.setPlaceNumber("H" + hall.getHallId() + "-P" + String.format("%02d", r));
                room.setMonthlyFee(pick(FEES));
                room.setIsAvailable(r > 10);   // rooms 1-10 occupied, 11-20 available
                room.setRoomType(pick(ROOM_TYPES));
                list.add(roomRepository.save(room));
            }
        }
        return list;
    }

    // ── 7. Hall Placements ────────────────────────────────────────────────────

    private void seedHallPlacements(List<Student> students, List<Room> rooms) {
        log.info("Seeding 100 hall placements...");
        // Only rooms that are NOT available (occupied) = first 10 per hall = rooms index 0-9, 20-29, etc.
        List<Room> occupiedRooms = rooms.stream()
                .filter(r -> !r.getIsAvailable())
                .toList();

        for (int i = 0; i < occupiedRooms.size(); i++) {
            LocalDate start = randomPastDate(2, 8);
            HallPlacement p = new HallPlacement();
            p.setStudent(students.get(i));
            p.setRoom(occupiedRooms.get(i));
            p.setStartDate(start);
            p.setEndDate(start.plusDays(180 + random.nextInt(185)));
            p.setIsActive(true);
            placementRepository.save(p);
        }
    }

    // ── 8. Apartments ─────────────────────────────────────────────────────────

    private List<StudentApartment> seedApartments() {
        log.info("Seeding 20 apartments...");
        List<StudentApartment> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            StudentApartment apt = new StudentApartment();
            apt.setFlatNumber("APT-" + String.format("%03d", i));
            apt.setApartmentName("Block " + (char)('A' + i - 1) + " Apartments");
            apt.setAddress((random.nextInt(100) + 1) + " " + pick(STREETS) + ", " + pick(CITIES));
            apt.setTotalBedrooms(3 + random.nextInt(4));
            apt.setManagerName(pick(FIRST_NAMES) + " " + pick(LAST_NAMES));
            apt.setManagerPhone(phone());
            list.add(apartmentRepository.save(apt));
        }
        return list;
    }

    // ── 9. Bedrooms ───────────────────────────────────────────────────────────

    private void seedBedrooms(List<StudentApartment> apts, List<Student> students) {
        log.info("Seeding bedrooms...");
        int studentIdx = 200; // students 201-240 get bedrooms
        for (StudentApartment apt : apts) {
            for (int b = 1; b <= 4; b++) {
                Bedroom bedroom = new Bedroom();
                bedroom.setApartment(apt);
                bedroom.setBedroomNumber("B" + b);
                bedroom.setPlaceNumber("APT" + String.format("%02d", apt.getApartmentId()) + "-B" + b);
                bedroom.setMonthlyFee(pick(new BigDecimal[]{
                        new BigDecimal("300"), new BigDecimal("350"),
                        new BigDecimal("400"), new BigDecimal("450")
                }));
                if (b <= 2 && studentIdx < 240) {
                    LocalDate start = randomPastDate(2, 8);
                    bedroom.setIsOccupied(true);
                    bedroom.setStudent(students.get(studentIdx++));
                    bedroom.setStartDate(start);
                    bedroom.setEndDate(start.plusDays(180 + random.nextInt(185)));
                } else {
                    bedroom.setIsOccupied(false);
                }
                bedroomRepository.save(bedroom);
            }
        }
    }

    // ── 10. Inspections ───────────────────────────────────────────────────────

    private void seedInspections(List<StudentApartment> apts) {
        log.info("Seeding apartment inspections...");
        for (StudentApartment apt : apts) {
            int count = 1 + random.nextInt(3);
            for (int i = 0; i < count; i++) {
                boolean sat = random.nextInt(4) != 0; // 75% pass
                ApartmentInspection ins = new ApartmentInspection();
                ins.setApartment(apt);
                ins.setInspectorName(pick(FIRST_NAMES) + " " + pick(LAST_NAMES));
                ins.setInspectionDate(randomPastDate(1, 12));
                ins.setStatus(sat ? "PASSED" : "FAILED");
                ins.setRemarks(sat
                        ? "All areas satisfactory. No issues found."
                        : "Maintenance issues found. Follow-up required.");
                inspectionRepository.save(ins);
            }
        }
    }

    // ── 11. Courses ───────────────────────────────────────────────────────────

    private List<Course> seedCourses() {
        log.info("Seeding 30 courses...");
        String[][] data = {
                {"CS101","Introduction to Programming","Computer Science"},
                {"CS201","Data Structures","Computer Science"},
                {"CS301","Algorithms","Computer Science"},
                {"CS401","Database Management","Computer Science"},
                {"CS501","Software Engineering","Computer Science"},
                {"MA101","Calculus I","Mathematics"},
                {"MA201","Linear Algebra","Mathematics"},
                {"MA301","Statistics","Mathematics"},
                {"PH101","Physics I","Physics"},
                {"PH201","Quantum Mechanics","Physics"},
                {"CH101","General Chemistry","Chemistry"},
                {"BI101","Introduction to Biology","Biology"},
                {"EC101","Microeconomics","Economics"},
                {"EC201","Macroeconomics","Economics"},
                {"PS101","Introduction to Psychology","Psychology"},
                {"SO101","Introduction to Sociology","Sociology"},
                {"HI101","World History","History"},
                {"EN101","Academic Writing","Literature"},
                {"PL101","Ethics","Philosophy"},
                {"LW101","Introduction to Law","Law"},
                {"AR101","Architectural Design","Architecture"},
                {"BA101","Business Management","Business Administration"},
                {"CS601","Machine Learning","Computer Science"},
                {"CS701","Cybersecurity","Computer Science"},
                {"MA401","Numerical Methods","Mathematics"},
                {"PH301","Electromagnetism","Physics"},
                {"DS201","Data Science","Computer Science"},
                {"BA201","Financial Accounting","Business Administration"},
                {"PS201","Developmental Psychology","Psychology"},
                {"LG101","Linguistics Fundamentals","Linguistics"},
        };
        List<Course> list = new ArrayList<>();
        for (String[] d : data) {
            Course c = new Course();
            c.setCourseCode(d[0]);
            c.setCourseName(d[1]);
            c.setDepartment(d[2]);
            c.setInstructorName("Dr. " + pick(FIRST_NAMES) + " " + pick(LAST_NAMES));
            c.setInstructorPhone("+44 20 " + (1000+random.nextInt(8999)) + " " + (1000+random.nextInt(8999)));
            c.setInstructorEmail(d[0].toLowerCase() + "@uni.edu");
            c.setInstructorRoomNumber("Room " + (100 + random.nextInt(400)));
            list.add(courseRepository.save(c));
        }
        return list;
    }

    // ── 12. Student Courses ───────────────────────────────────────────────────

    private void seedStudentCourses(List<Student> students, List<Course> courses) {
        log.info("Seeding student course enrolments...");
        for (Student student : students) {
            int count = 1 + random.nextInt(4);
            List<Course> shuffled = new ArrayList<>(courses);
            Collections.shuffle(shuffled, random);
            for (int i = 0; i < count; i++) {
                StudentCourse sc = new StudentCourse();
                sc.setStudent(student);
                sc.setCourse(shuffled.get(i));
                sc.setEnrolledDate(randomPastDate(1, 12));
                studentCourseRepository.save(sc);
            }
        }
    }

    // ── 13. Leases ────────────────────────────────────────────────────────────

    private List<LeaseAgreement> seedLeases(List<Student> students) {
        log.info("Seeding 800 leases...");
        Semester[] sems = Semester.values();
        List<Student> shuffled = new ArrayList<>(students.subList(0, 900));
        Collections.shuffle(shuffled, random);
        List<Student> leaseStudents = shuffled.subList(0, 800);

        List<LeaseAgreement> list = new ArrayList<>();
        int idx = 1;
        for (Student student : leaseStudents) {
            LocalDate enter = randomPastDate(1, 8);
            LocalDate leave = enter.plusDays(90 + random.nextInt(181));
            LeaseAgreement l = new LeaseAgreement();
            l.setLeaseNumber("LS-" + String.format("%05d", idx));
            l.setStudent(student);
            l.setPlaceNumber("H" + (1 + random.nextInt(10)) + "-P" + String.format("%02d", 1 + random.nextInt(10)));
            l.setSemester(sems[random.nextInt(sems.length)]);
            l.setStatus(leave.isAfter(LocalDate.now()) ? LeaseStatus.ACTIVE : LeaseStatus.EXPIRED);
            l.setMonthlyRent(pick(FEES));
            l.setDepositAmount(l.getMonthlyRent().multiply(new BigDecimal("2")));
            l.setEnterDate(enter);
            l.setLeaveDate(leave);
            l.setStartDate(enter);
            l.setEndDate(leave);
            list.add(leaseRepository.save(l));
            idx++;
        }
        return list;
    }

    // ── 14. Invoices ──────────────────────────────────────────────────────────

    private void seedInvoices(List<LeaseAgreement> leases) {
        log.info("Seeding 800 invoices...");
        Semester[] sems   = Semester.values();
        String[]   statuses = {"PAID","PAID","PAID","PAID","PAID","PAID","PENDING","PENDING","PENDING","OVERDUE"};
        int idx = 1;
        for (LeaseAgreement lease : leases) {
            LocalDate issue = randomPastDate(3, 6);
            LocalDate due   = issue.plusDays(30);
            String    st    = statuses[random.nextInt(statuses.length)];

            Invoice inv = new Invoice();
            inv.setInvoiceNumber("INV-" + String.format("%05d", idx));
            inv.setStudent(lease.getStudent());
            inv.setSemester(new String[]{"FIRST","SECOND","SUMMER"}[random.nextInt(3)]);
            inv.setAmount(pick(FEES));
            inv.setIssueDate(issue);
            inv.setDueDate(due);
            inv.setStatus(st);
            inv.setDescription("Semester accommodation fee");


            if (st.equals("PAID")) {
                inv.setPaidDate(randomPastDate(1, 3));
                inv.setPaymentMethod(pick(PAY_METHODS));
            }
            if (st.equals("PENDING") || st.equals("OVERDUE")) {
                inv.setFirstReminderDate(due.plusDays(7));
            }
            if (st.equals("OVERDUE")) {
                inv.setSecondReminderDate(due.plusDays(14));
            }

            invoiceRepository.save(inv);
            idx++;
        }
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    private <T> T pick(T[] arr) {
        return arr[random.nextInt(arr.length)];
    }

    private String phone() {
        return "+44 7" + (100 + random.nextInt(900)) + " " + (100000 + random.nextInt(900000));
    }

    private LocalDate randomDate(int startYear, int endYear) {
        LocalDate start = LocalDate.of(startYear, 1, 1);
        LocalDate end   = LocalDate.of(endYear, 12, 31);
        return start.plusDays(random.nextInt((int)(end.toEpochDay() - start.toEpochDay())));
    }

    private LocalDate randomPastDate(int minMonths, int maxMonths) {
        return LocalDate.now().minusDays(minMonths * 30L + random.nextInt((maxMonths - minMonths) * 30));
    }
}