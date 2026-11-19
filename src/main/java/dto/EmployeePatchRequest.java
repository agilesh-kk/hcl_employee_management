
package dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;

public class EmployeePatchRequest {

    @Pattern(
        regexp = ".*\\S.*",
        message = "Name cannot be blank"
    )
    private String name;

    @Email(message = "Enter a valid email")
    private String email;

    @Pattern(
        regexp = ".*\\S.*",
        message = "Department cannot be blank"
    )
    private String department;

    @Positive(message = "Salary must be positive")
    private BigDecimal salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
