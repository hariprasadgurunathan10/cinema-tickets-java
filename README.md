
# Cinema Tickets — Java

## 1. Project Overview

This project implements the [DWP Cinema Tickets coding assessment](https://github.com/dwp/cinema-tickets) in Java. It implements the supplied `TicketService` interface to validate ticket purchases, calculate payment amounts and seat requirements, and coordinate payment processing and seat reservations.

Development followed a **Test-Driven Development (TDD)** approach, with an emphasis on SOLID principles, clean code and reusability. The project uses **Java 21**, **Maven**, **JUnit 5** and **Mockito**, with coverage measured using **IntelliJ IDEA**.

## 2. Business Requirements

### Ticket Types and Pricing

| Ticket Type | Price | Seats Required |
| --- | ---: | ---: |
| Adult | £25 | 1 |
| Child | £15 | 1 |
| Infant | £0 | 0 |

### Assessment Rules

- Customers can purchase multiple tickets of different types in one transaction.
- A purchase cannot exceed **25 tickets in total**, including infants.
- Infants sit on an adult's lap, are free and do not need a reserved seat.
- Child and infant tickets cannot be purchased without an adult ticket.
- The correct payment amount must be sent to `TicketPaymentService`, and the number of seats to `SeatReservationService`.
- Invalid ticket purchases must be rejected.

### Implemented Validation

The service rejects null, zero or negative account IDs; null or empty request arrays; null request entries or ticket types; non-positive quantities; and individual or combined ticket counts exceeding 25. Repeated ticket types are aggregated when validating and calculating purchase totals.

Invalid purchases throw `InvalidPurchaseException`. **All validation completes before payment or seat reservation.** The additional one-infant-per-adult rule is documented separately in Section 4.

### Purchase Calculations

- **Amount payable:** `(adults × £25) + (children × £15)`
- **Seats reserved:** `adults + children`
- **Total tickets:** `adults + children + infants`

| Adults | Children | Infants | Total Tickets | Payment | Seats |
| ---: | ---: | ---: | ---: | ---: | ---: |
| 2 | 2 | 1 | 5 | £80 | 4 |
| 1 | 1 | 1 | 3 | £40 | 2 |
| 1 | 24 | 0 | 25 | £385 | 25 |

## 3. Design and Implementation

### Component Responsibilities

| Component | Responsibility |
| --- | --- |
| `TicketService` | Defines the unchanged public purchase contract |
| `TicketServiceImpl` | Coordinates validation, aggregation, calculation, payment and seat reservation |
| `TicketValidator` | Validates account IDs, requests and aggregated counts |
| `TicketTypeAggregator` | Combines quantities, including repeated ticket types |
| `TicketPurchaseCalculator` | Calculates payment amounts and required seats |
| `TicketTypeRequest` | Represents an immutable ticket request |
| `TicketTypeCounts` / `PurchaseTotals` | Immutable Java records holding aggregated counts and calculated purchase totals |
| `TicketConstants` | Centralises prices, ticket limits and validation messages for consistency and reuse |
| `InvalidPurchaseException` | Reports invalid requests and business-rule violations |
| `TicketPaymentService` / `SeatReservationService` | Supplied interfaces for third-party payment and seat reservation |

### Purchase Workflow

1. Validate the account ID and individual ticket requests.
2. Aggregate quantities by ticket type and validate the combined purchase.
3. Calculate the payment amount and number of seats.
4. Request payment, then reserve the required seats.

### Engineering Decisions

- **SOLID — Single Responsibility:** Validation, aggregation and calculation have focused classes; `TicketServiceImpl` coordinates the workflow.
- **SOLID — Dependency Inversion:** The supplied payment and reservation interfaces are constructor-injected, allowing interactions to be tested with Mockito mocks. Internal helper components are instantiated within `TicketServiceImpl`.
- **Clean Code and DRY:** Descriptive naming, focused methods, early validation, reusable validation methods for ticket limit, and centralised constants reduce code duplication.
- **Reusability and testability:** Validation, aggregation and calculations can be tested independently of external services.
- **Immutability:** `TicketTypeRequest` retains a final, getter-based representation; `TicketTypeCounts` and `PurchaseTotals` use Java records. No mutable purchase state is shared across calls.
- **Documentation:** JavaDoc explains class and method responsibilities, parameters, return values and relevant business rules. Explanatory test comments describe scenarios, edge cases and expected outcomes.
- **Consistent Error Handling:** Validation failures throw `InvalidPurchaseException` with clear, descriptive error messages.
Messages are centralised in `TicketConstants` for consistency and maintainability.

### Project Structure

```text
cinema-tickets-java/
├── pom.xml
├── src/
│   ├── main/java/
│   │   ├── thirdparty/
│   │   │   ├── paymentgateway/
│   │   │   └── seatbooking/
│   │   └── uk/gov/dwp/uc/pairtest/
│   │       ├── TicketService.java
│   │       ├── TicketServiceImpl.java
│   │       ├── TicketTypeAggregator.java
│   │       ├── TicketPurchaseCalculator.java
│   │       ├── constants/TicketConstants.java
│   │       ├── domain/
│   │       │   ├── TicketTypeRequest.java
│   │       │   ├── TicketTypeCounts.java
│   │       │   └── PurchaseTotals.java
│   │       ├── exception/InvalidPurchaseException.java
│   │       └── validation/TicketValidator.java
│   └── test/java/uk/gov/dwp/uc/pairtest/
│       ├── TicketServiceImplTest.java
│       ├── TicketTypeAggregatorTest.java
│       ├── TicketPurchaseCalculatorTest.java
│       └── validation/TicketValidatorTest.java
└── README.md
```

## 4. Assumptions and Constraints

**Assessment constraints:** The supplied `TicketService` interface and the code in `thirdparty.*` remain unchanged; `TicketTypeRequest` is immutable.

**Assessment assumptions:** All account IDs greater than zero are valid and have sufficient funds. The external payment and seat reservation providers are assumed to be defect-free, with all requested operations succeeding. Balance checking, failure recovery and transaction rollback are therefore outside the scope of this exercise.

**Additional assumption — one infant per adult:** The assessment requires an accompanying adult but does not specify an infant-to-adult ratio. This solution assumes each infant requires a separate adult, so infant tickets cannot exceed adult tickets. For example, one adult with two infants is rejected. No additional child-to-adult numerical ratio is imposed.

## 5. Testing and Coverage

### Testing Strategy

Development followed **RED → GREEN → REFACTOR**: write a failing test, implement the required behaviour and refactor while preserving it. **JUnit 5** provides unit and parameterised tests, while **Mockito** mocks and verifies external service interactions.

| Test Suite | Scenarios Covered |
| --- | --- |
| `TicketValidatorTest` | Account IDs, null/empty requests, invalid types and quantities, integer boundaries, purchase limits, adult accompaniment and defensive edge cases |
| `TicketTypeAggregatorTest` | Adult, child and infant totals, duplicate ticket types and request ordering |
| `TicketPurchaseCalculatorTest` | Ticket prices, seat counts and mixed purchases, including free infants |
| `TicketServiceImplTest` | Purchase coordination, correct payment/reservation arguments, invalid requests and **no external calls on rejection** |

Test methods use descriptive names and explanatory comments to clarify business rules, edge cases and expected outcomes. Direct validation of a zero-count aggregate is also tested; empty purchases remain invalid through the normal request-validation workflow.

### Verified IntelliJ IDEA Coverage

The report captured on **18 November 2026** shows the following results for `uk.gov.dwp.uc.pairtest`, including its subpackages:

| Metric | Application Coverage | Covered / Total |
| --- | ---: | ---: |
| Class | **100%** | 10 / 10 |
| Method | **100%** | 17 / 17 |
| Line | **100%** | 65 / 65 |
| Branch | **100%** | 33 / 33 |

The entire project, including supplied third-party packages, shows **83% class, 89% method, 97% line and 100% branch coverage**. The third-party implementations are mocked rather than executed directly, explaining the difference. Coverage reflects exercised code paths rather than proving the absence of defects.

**HTML report:** To reproduce the IntelliJ IDEA coverage results, run the tests with **Run with Coverage** and export the report to `htmlReport/`. Open `htmlReport/index.html` locally. The generated directory is excluded from Git using `.gitignore`.

## 6. Build and Run

**Prerequisites:** JDK 21 or later and Apache Maven 3.x. IntelliJ IDEA is needed only to reproduce the IDE-specific HTML report.

Clone the public repository and navigate to the `cinema-tickets-java` directory containing `pom.xml`. Then run:

```bash
mvn clean compile
mvn clean test
mvn -Dtest=TicketServiceImplTest test
mvn clean verify
```

These commands compile, test and verify the Maven project. The IntelliJ coverage report is generated separately through the IDE.

The project implements a Java service interface, not a standalone executable, REST API or application server.

## 7. Additional Considerations

- **Validation stages:** Individual request entries are checked before aggregation; the combined quantities are then checked against purchase-level business rules.
- **Repeated requests:** Aggregation ensures repeated ticket types contribute correctly to pricing, seat allocation and the overall ticket limit.
- **Boundary cases:** Invalid quantities, null inputs, the maximum of 25 tickets and the infant-to-adult assumption are covered by validation and tests.
- **External service order:** Payment is requested before seat reservation, consistent with the assessment's successful-operation assumptions.
- **Scope and version control:** No unnecessary database, authentication system or additional framework was introduced. Generated build output and HTML coverage reports are excluded from version control.
