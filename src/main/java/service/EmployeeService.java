
package service;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dto.EmployeePatchRequest;
import dto.EmployeeRequest;
import dto.EmployeeResponse;

import entity.Employee;
import entity.Department;

import exception.ResourceNotFoundException;

import repository.EmployeeRepository;
import repository.DepartmentRepository;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;

    // Constructor-based Dependency Injection
    public EmployeeService(
            EmployeeRepository repository,
            DepartmentRepository departmentRepository) {

        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    // Convert Employee entity into Response DTO
    private EmployeeResponse convertToResponse(Employee e) {

        return new EmployeeResponse(
            e.getId(),
            e.getName(),
            e.getEmail(),
            e.getDepartment().getName(),
            e.getSalary(),
            e.getCreatedAt()
        );
    }

    // Find Department by name
    private Department findDepartmentByName(String name) {

        return departmentRepository.findByNameIgnoreCase(name)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Department not found: " + name
            ));
    }

    // CREATE EMPLOYEE
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                "Email already exists"
            );
        }

        Department department =
            findDepartmentByName(request.getDepartment());

        Employee employee = new Employee(
            request.getName(),
            request.getEmail(),
            department,
            request.getSalary()
        );

        Employee saved = repository.save(employee);

        return convertToResponse(saved);
    }

    // GET ALL EMPLOYEES
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return repository.findAll()
            .stream()
            .map(this::convertToResponse)
            .toList();
    }

    // GET EMPLOYEE BY ID
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with ID: " + id
            ));

        return convertToResponse(employee);
    }

    // PAGINATION
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getEmployeesWithPagination(
            Pageable pageable) {

        return repository.findAll(pageable)
            .map(this::convertToResponse);
    }

    // UPDATE EMPLOYEE
    public EmployeeResponse updateEmployee(
            Long id, EmployeeRequest request) {

        Employee employee = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with ID: " + id
            ));

        if (!employee.getEmail().equals(request.getEmail())
                && repository.existsByEmail(request.getEmail())) {

            throw new IllegalArgumentException(
                "Email already exists"
            );
        }

        Department department =
            findDepartmentByName(request.getDepartment());

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);
        employee.setSalary(request.getSalary());

        Employee updated = repository.save(employee);

        return convertToResponse(updated);
    }

    // DELETE EMPLOYEE
    public void deleteEmployee(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Employee not found with ID: " + id
            );
        }

        repository.deleteById(id);
    }

    // PARTIAL UPDATE EMPLOYEE
    public EmployeeResponse patchEmployee(
            Long id, EmployeePatchRequest request) {

        Employee employee = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with ID: " + id
            ));

        if (request.getName() != null) {

            if (request.getName().isBlank()) {
                throw new IllegalArgumentException(
                    "Name cannot be blank"
                );
            }

            employee.setName(request.getName());
        }

        if (request.getEmail() != null) {

            if (request.getEmail().isBlank()) {
                throw new IllegalArgumentException(
                    "Email cannot be blank"
                );
            }

            if (repository.existsByEmail(request.getEmail())
                    && !employee.getEmail().equals(request.getEmail())) {

                throw new IllegalArgumentException(
                    "Email already exists"
                );
            }

            employee.setEmail(request.getEmail());
        }

        if (request.getDepartment() != null) {

            if (request.getDepartment().isBlank()) {
                throw new IllegalArgumentException(
                    "Department cannot be blank"
                );
            }

            Department department =
                findDepartmentByName(request.getDepartment());

            employee.setDepartment(department);
        }

        if (request.getSalary() != null) {

            if (request.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                    "Salary must be greater than zero"
                );
            }

            employee.setSalary(request.getSalary());
        }

        Employee updated = repository.save(employee);

        return convertToResponse(updated);
    }

    // SEARCH BY DEPARTMENT NAME
    @Transactional(readOnly = true)
    public List<EmployeeResponse> searchByDepartment(
            String department) {

        return repository.findByDepartment_NameIgnoreCase(department)
            .stream()
            .map(this::convertToResponse)
            .toList();
    }

    // SEARCH BY MINIMUM SALARY
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByMinimumSalary(
            BigDecimal salary) {

        if (salary == null ||
                salary.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                "Minimum salary must be greater than zero"	
            );
        }

        return repository.findEmployeesByMinimumSalary(salary)
            .stream()
            .map(this::convertToResponse)
            .toList();
    }
}
