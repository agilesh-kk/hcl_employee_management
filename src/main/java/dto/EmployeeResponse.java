
package dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class EmployeeResponse {

    private Long id;
    private String name;
    private String email;
    private String department;
    private BigDecimal salary;
    private LocalDateTime createdAt;

    public EmployeeResponse(
            Long id,
            String name,
            String email,
            String department,
            BigDecimal salary,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
