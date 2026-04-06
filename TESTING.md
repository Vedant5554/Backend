# UAMS — Testing Documentation

> **Testing Stack:** JUnit 5 · Mockito · Spring Boot Test · MockMvc · H2 (in-memory) · Bucket4j

---

## Table of Contents

1. [Overview](#1-overview)
2. [Test Architecture](#2-test-architecture)
3. [Test Configuration](#3-test-configuration)
4. [Test Suite Reference](#4-test-suite-reference)
5. [Running the Tests](#5-running-the-tests)
6. [Testing Patterns & Conventions](#6-testing-patterns--conventions)
7. [Dependencies](#7-dependencies)

---

## 1. Overview

The UAMS backend uses a **three-layer testing strategy**:

```
┌──────────────────────────────────────────────────────────────────────┐
│                        TESTING PYRAMID                               │
│                                                                      │
│                            ╱╲                                        │
│                           ╱  ╲        Integration Tests              │
│                          ╱    ╲       (UamsApplicationTests)         │
│                         ╱──────╲      Spring context boot + H2 DB   │
│                        ╱        ╲                                    │
│                       ╱          ╲    Unit Tests                     │
│                      ╱            ╲   (AuthControllerTest)           │
│                     ╱              ╲  MockMvc + Mockito (no Spring)  │
│                    ╱────────────────╲                                │
│                   ╱                  ╲ Infrastructure Tests          │
│                  ╱                    ╲(RateLimitTest)               │
│                 ╱                      ╲Pure unit test on filter     │
│                ╱────────────────────────╲                            │
└──────────────────────────────────────────────────────────────────────┘
```

| Layer              | Test Class               | What it verifies                                  | Needs Spring? | Needs DB? |
|--------------------|--------------------------|---------------------------------------------------|---------------|-----------|
| Integration        | `UamsApplicationTests`   | Full Spring context loads, beans wire correctly    | ✅ Yes        | ✅ H2     |
| Controller Unit    | `AuthControllerTest`     | HTTP endpoints, request/response, status codes     | ❌ No         | ❌ No     |
| Infrastructure     | `RateLimitTest`          | Rate limiting filter enforces 100 req/min limit    | ❌ No         | ❌ No     |

---

## 2. Test Architecture

### Directory Structure

```
src/test/
├── java/com/example/uams/
│   ├── AuthControllerTest.java      ← Controller unit test (MockMvc + Mockito)
│   ├── RateLimitTest.java           ← Rate-limiting filter unit test
│   └── UamsApplicationTests.java    ← Integration test (full Spring Boot context)
└── resources/
    └── application-test.properties  ← Test-specific database & config overrides
```

### How It Works

```
┌─────────────────┐     ┌──────────────┐     ┌────────────────────┐
│  JUnit 5 Runner │────►│ @SpringBoot  │────►│ H2 In-Memory DB    │
│  (test engine)  │     │ Test Context  │     │ (MySQL compat mode)│
└─────────────────┘     └──────────────┘     └────────────────────┘
        │
        ├── AuthControllerTest   → MockMvc (standalone, no Spring context)
        ├── RateLimitTest        → Direct Java object instantiation
        └── UamsApplicationTests → Full Spring context with @ActiveProfiles("test")
```

**Key insight:** Only `UamsApplicationTests` actually boots Spring. The other tests are **pure unit tests** — fast, isolated, no external dependencies.

---

## 3. Test Configuration

### `application-test.properties`

This file overrides the production MySQL configuration with an **H2 in-memory database** for testing:

```properties
# ── In-Memory Database ────────────────────────────────────────
spring.datasource.url=jdbc:h2:mem:uamsdb;DB_CLOSE_DELAY=-1;MODE=MySQL
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# ── Environment Variables (mocked for test) ───────────────────
DB_URL=jdbc:h2:mem:uamsdb
DB_USERNAME=sa
DB_PASSWORD=
JWT_SECRET=this-is-a-mock-jwt-secret-for-testing-purposes-must-be-long-enough!

# ── Hibernate: auto-create and drop schema ────────────────────
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# ── Flyway: disabled (H2 uses Hibernate's schema generation) ─
spring.flyway.enabled=false
```

### Why H2 instead of MySQL for tests?

| Concern              | Production (MySQL) | Testing (H2)           |
|----------------------|--------------------|------------------------|
| Speed                | ~seconds startup   | ~milliseconds startup  |
| External dependency  | Docker container   | None (in-process JVM)  |
| Schema management    | Flyway migrations  | Hibernate `create-drop`|
| Compatibility        | Full MySQL         | `MODE=MySQL` emulation |
| Cleanup              | Manual             | Automatic (in-memory)  |

---

## 4. Test Suite Reference

### 4.1 `UamsApplicationTests` — Integration Test

**File:** `src/test/java/com/example/uams/UamsApplicationTests.java`

**Purpose:** Verifies that the entire Spring Boot application context loads successfully — all beans, configurations, security filters, JPA entities, and repository proxies wire up without errors.

```java
@SpringBootTest
@ActiveProfiles("test")
class UamsApplicationTests {

    @Test
    void contextLoads() {
        // If this test passes, the entire Spring context bootstraps
        // without errors. This catches:
        //   - Missing bean definitions
        //   - Circular dependencies
        //   - Invalid JPA entity mappings
        //   - Broken configuration properties
    }
}
```

**What it catches:**

| Problem                             | Example                                                  |
|-------------------------------------|----------------------------------------------------------|
| Missing `@Bean` or `@Component`     | Forgot `@Repository` on a repository interface           |
| Bad entity mapping                  | `@Column(name = "...")` references a non-existent column |
| Circular dependency                 | Service A depends on B, B depends on A                   |
| Invalid configuration               | Missing required `@Value` property                      |
| Flyway migration failures           | (Disabled in test — Hibernate handles schema)           |

**Activation:** The `@ActiveProfiles("test")` annotation loads `application-test.properties` which swaps MySQL for H2.

---

### 4.2 `AuthControllerTest` — Controller Unit Test

**File:** `src/test/java/com/example/uams/AuthControllerTest.java`

**Purpose:** Tests the `AuthController` HTTP endpoints in isolation using **MockMvc** and **Mockito** — no Spring context, no database.

```java
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLogoutReturnsOk() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
               .andExpect(status().isOk());
    }
}
```

**What this test verifies:**

| Aspect                 | Verification                                                  |
|------------------------|---------------------------------------------------------------|
| Endpoint reachability  | `POST /api/auth/logout` is properly mapped and reachable      |
| Response status        | Returns HTTP `200 OK`                                         |
| Controller wiring      | `AuthController` constructor injection works via `@InjectMocks`|

**How the Mocking Works:**

```
┌──────────────────────────────────────────────────────────────┐
│                  AuthControllerTest                           │
│                                                              │
│  @Mock AuthenticationManager ──────► Fake (no-op) mock       │
│  @Mock JwtTokenProvider      ──────► Fake (no-op) mock       │
│                                                              │
│  @InjectMocks AuthController ──────► Real controller,        │
│                                      but with mocked deps    │
│                                                              │
│  MockMvc ──► Simulates HTTP requests without a real server   │
└──────────────────────────────────────────────────────────────┘
```

1. **`@Mock`** creates fake implementations of `AuthenticationManager` and `JwtTokenProvider` (they return `null` / do nothing by default).
2. **`@InjectMocks`** creates a real `AuthController` and injects the mocks into its constructor.
3. **`MockMvcBuilders.standaloneSetup()`** wraps the controller in a lightweight HTTP simulation layer — no actual server starts.

---

### 4.3 `RateLimitTest` — Infrastructure Unit Test

**File:** `src/test/java/com/example/uams/RateLimitTest.java`

**Purpose:** Verifies that the `RateLimitFilter` correctly enforces the rate limit of **100 requests per minute** per IP address using Bucket4j.

```java
class RateLimitTest {

    @Test
    void testRateLimitingEnforcement() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();

        // Phase 1: Send 100 requests — all should pass (HTTP 200)
        for (int i = 0; i < 100; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();
            request.setRemoteAddr("127.0.0.1");

            filter.doFilter(request, response, filterChain);
            assert response.getStatus() != 429
                : "Rate limit exceeded too early at request " + i;
        }

        // Phase 2: Request #101 should be rejected (HTTP 429)
        MockHttpServletRequest finalReq = new MockHttpServletRequest();
        MockHttpServletResponse finalResp = new MockHttpServletResponse();
        MockFilterChain finalChain = new MockFilterChain();
        finalReq.setRemoteAddr("127.0.0.1");

        filter.doFilter(finalReq, finalResp, finalChain);
        assert finalResp.getStatus() == 429
            : "Expected 429 TOO_MANY_REQUESTS but got " + finalResp.getStatus();
    }
}
```

**What this test verifies:**

| Phase    | Requests | Expected Status | Behavior                               |
|----------|----------|-----------------|----------------------------------------|
| Phase 1  | 1—100    | ≠ 429           | All 100 tokens consumed successfully   |
| Phase 2  | 101      | = 429           | Bucket empty → `429 Too Many Requests` |

**How the Rate Limiter Works (Bucket4j):**

```
                    ┌──────────────────────────────────┐
                    │      RateLimitFilter              │
                    │                                  │
  Request ──────►   │  IP = request.getRemoteAddr()    │
                    │         │                        │
                    │         ▼                        │
                    │  buckets.computeIfAbsent(ip)     │
                    │         │                        │
                    │         ▼                        │
                    │  ┌─────────────┐                 │
                    │  │   Bucket    │  capacity: 100  │
                    │  │  (per IP)   │  refill: 100/min│
                    │  └──────┬──────┘                 │
                    │         │                        │
                    │    tryConsume(1)                  │
                    │      │      │                    │
                    │     ✅      ❌                   │
                    │   allow   reject                 │
                    │  (pass)  (HTTP 429)              │
                    └──────────────────────────────────┘
```

- Each IP address gets its own **token bucket** with a capacity of 100 tokens.
- Tokens refill at a rate of 100 tokens per minute (greedy refill).
- Each request consumes 1 token. If the bucket is empty, the request is rejected with HTTP 429.

---

## 5. Running the Tests

### Run All Tests

```bash
cd UAMS
./gradlew test
```

### Run a Single Test Class

```bash
# Run only the auth controller test
./gradlew test --tests "com.example.uams.AuthControllerTest"

# Run only the rate limit test
./gradlew test --tests "com.example.uams.RateLimitTest"

# Run only the integration test
./gradlew test --tests "com.example.uams.UamsApplicationTests"
```

### Run Tests with Verbose Output

```bash
./gradlew test --info
```

### View Test Reports

After running tests, Gradle generates an HTML report:

```
UAMS/build/reports/tests/test/index.html
```

Open this file in a browser to see:
- ✅ / ❌ pass/fail status per test
- Execution duration
- Standard output and error logs
- Stack traces for failures

### Quick Compile Check (No Execution)

```bash
./gradlew compileTestJava
```

This verifies that all test files compile without actually running them — useful for CI/CD build validation.

---

## 6. Testing Patterns & Conventions

### Pattern 1: Standalone MockMvc (Controller Tests)

Used in `AuthControllerTest`. No Spring context — the fastest type of test.

```java
// Setup
MockitoAnnotations.openMocks(this);
mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

// Execute & Assert
mockMvc.perform(post("/api/auth/logout"))
       .andExpect(status().isOk());
```

**When to use:** Testing HTTP status codes, request mapping, request validation, and response format of a single controller.

---

### Pattern 2: Mock Servlet (Filter Tests)

Used in `RateLimitTest`. Directly instantiates the filter and feeds it mock HTTP objects.

```java
// Setup
RateLimitFilter filter = new RateLimitFilter();
MockHttpServletRequest request = new MockHttpServletRequest();
MockHttpServletResponse response = new MockHttpServletResponse();
MockFilterChain chain = new MockFilterChain();

// Execute
filter.doFilter(request, response, chain);

// Assert
assert response.getStatus() == 429;
```

**When to use:** Testing `OncePerRequestFilter` or `Filter` implementations in isolation.

---

### Pattern 3: Full Context Integration (`@SpringBootTest`)

Used in `UamsApplicationTests`. Boots the entire application.

```java
@SpringBootTest
@ActiveProfiles("test")
class UamsApplicationTests {
    @Test
    void contextLoads() { }
}
```

**When to use:** Verifying that all beans wire together, JPA entities are valid, and the application starts without errors.

---

### Naming Conventions

| Convention                    | Example                           |
|-------------------------------|-----------------------------------|
| Test class name               | `{ClassName}Test.java`            |
| Test method name              | `test{Behavior}` e.g. `testLogoutReturnsOk` |
| Test profile                  | `@ActiveProfiles("test")`         |
| Test config file              | `application-test.properties`     |

---

## 7. Dependencies

All testing dependencies are declared in `build.gradle`:

```groovy
dependencies {
    // Spring Boot Test (includes JUnit 5, Mockito, MockMvc, AssertJ)
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    // Spring Security Test (SecurityMockMvcRequestPostProcessors, etc.)
    testImplementation 'org.springframework.security:spring-security-test'

    // H2 In-Memory Database (replaces MySQL during tests)
    testImplementation 'com.h2database:h2'
}
```

### What `spring-boot-starter-test` includes:

| Library          | Purpose                                                    |
|------------------|------------------------------------------------------------|
| **JUnit 5**      | Test runner and assertion framework (`@Test`, `assertEquals`) |
| **Mockito**      | Mocking framework (`@Mock`, `@InjectMocks`, `when/thenReturn`) |
| **MockMvc**      | HTTP request simulation for controller testing              |
| **AssertJ**      | Fluent assertion library (`assertThat(x).isEqualTo(y)`)     |
| **Hamcrest**     | Matcher library used by MockMvc expectations                |
| **JSONPath**     | JSON response body assertions                              |
| **Spring Test**  | `@SpringBootTest`, `@ActiveProfiles`, `TestRestTemplate`   |

### Test Execution Engine

```groovy
tasks.named('test') {
    useJUnitPlatform()     // Use JUnit 5 (Jupiter) engine
}
```

---

*Generated from the UAMS codebase · JUnit 5 · Spring Boot 4.0.3*
