
package controller;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dto.EmployeePatchRequest;
import dto.EmployeeRequest;
import dto.EmployeeResponse;

import jakarta.validation.Valid;

import service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    // Constructor-based Dependency Injection
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // CREATE EMPLOYEE
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = service.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL EMPLOYEES - JSON AND XML
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(service.getAllEmployees());
    }

    // GET EMPLOYEE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        validateId(id);

        return ResponseEntity.ok(service.getEmployeeById(id));
    }

    // UPDATE EMPLOYEE
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        validateId(id);

        return ResponseEntity.ok(service.updateEmployee(id, request));
    }

    // DELETE EMPLOYEE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        validateId(id);

        service.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // PARTIAL UPDATE EMPLOYEE
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> patchEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeePatchRequest request) {

        validateId(id);

        return ResponseEntity.ok(service.patchEmployee(id, request));
    }

    // SEARCH EMPLOYEES BY DEPARTMENT
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam String department) {

        if (department.isBlank()) {
            throw new IllegalArgumentException(
                    "Department cannot be blank"
            );
        }

        return ResponseEntity.ok(
                service.searchByDepartment(department)
        );
    }

    // SEARCH EMPLOYEES BY MINIMUM SALARY
    @GetMapping("/salary")
    public ResponseEntity<List<EmployeeResponse>> getBySalary(
            @RequestParam BigDecimal minSalary) {

        return ResponseEntity.ok(
                service.getEmployeesByMinimumSalary(minSalary)
        );
    }

    // PAGINATION AND SORTING
    @GetMapping("/page")
    public ResponseEntity<Page<EmployeeResponse>> getEmployeesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Invalid page or size"
            );
        }

        if (sortBy.isBlank() || direction.isBlank()) {
            throw new IllegalArgumentException(
                    "Sorting field and direction cannot be blank"
            );
        }

        List<String> allowedFields = List.of(
                "id", "name", "email", "department.name", "salary"
        );

        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sorting field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Direction must be asc or desc"
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                service.getEmployeesWithPagination(pageable)
        );
    }

    // COMMON EMPLOYEE ID VALIDATION
    private void validateId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Employee ID must be positive"
            );
        }
    }
}
