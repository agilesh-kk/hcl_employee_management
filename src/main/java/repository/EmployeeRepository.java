
package repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    // Search employees by department name
    List<Employee> findByDepartment_NameIgnoreCase(String department);

    // Search employees by minimum salary
    @Query("SELECT e FROM Employee e WHERE e.salary >= :salary ORDER BY e.salary DESC")
    List<Employee> findEmployeesByMinimumSalary(
            @Param("salary") BigDecimal salary
    );
}
