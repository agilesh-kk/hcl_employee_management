
const token = sessionStorage.getItem("accessToken");

const tableBody = document.getElementById("employeeTableBody");
const message = document.getElementById("employeeMessage");

let currentPage = 0;
const pageSize = 5;
let totalPages = 0;

// Load employees with pagination
async function loadEmployees(page = 0) {

    currentPage = page;

    message.textContent = "Loading employees...";

    try {

        const response = await fetch(
            `/api/employees/page?page=${currentPage}&size=${pageSize}&sortBy=id&direction=asc`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            throw new Error("Unable to load employees. Status: " + response.status);
        }

        const result = await response.json();

        totalPages = result.totalPages;

        displayEmployees(result.content);

        updatePagination();

        message.textContent =
            `Total Employees: ${result.totalElements}`;

    } catch (error) {

        tableBody.innerHTML =
            `<tr><td colspan="6">Unable to load employee records.</td></tr>`;

        message.textContent = error.message;

        console.error(error);
    }
}


// Display employee records
function displayEmployees(employees) {

    tableBody.innerHTML = "";

    if (!employees || employees.length === 0) {

        tableBody.innerHTML =
            `<tr><td colspan="6">No employees found.</td></tr>`;

        return;
    }

    employees.forEach(employee => {

        const row = document.createElement("tr");

        const values = [
            employee.id,
            employee.name,
            employee.email,
            employee.department,
            employee.salary,
            employee.createdAt
        ];

        values.forEach(value => {
            const cell = document.createElement("td");
            cell.textContent = value ?? "";
            row.appendChild(cell);
        });

        tableBody.appendChild(row);
    });
}


// Update pagination buttons
function updatePagination() {

    document.getElementById("pageInfo").textContent =
        `Page ${totalPages === 0 ? 0 : currentPage + 1} of ${totalPages}`;

    document.getElementById("previousBtn").disabled =
        currentPage <= 0;

    document.getElementById("nextBtn").disabled =
        currentPage >= totalPages - 1;
}


// Search by department
document.getElementById("searchDepartmentBtn")
    .addEventListener("click", async function () {

        const department =
            document.getElementById("departmentSearch").value.trim();

        if (!department) {
            message.textContent = "Please enter a department.";
            return;
        }

        await loadFilteredEmployees(
            `/api/employees/search?department=${encodeURIComponent(department)}`
        );
    });


// Filter by minimum salary
document.getElementById("searchSalaryBtn")
    .addEventListener("click", async function () {

        const salary = document.getElementById("minSalary").value;

        if (!salary || Number(salary) < 0) {
            message.textContent = "Please enter a valid minimum salary.";
            return;
        }

        await loadFilteredEmployees(
            `/api/employees/salary?minSalary=${encodeURIComponent(salary)}`
        );
    });


// Load filtered employee records
async function loadFilteredEmployees(url) {

    message.textContent = "Searching...";

    try {

        const response = await fetch(url, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Employee search failed. Status: " + response.status);
        }

        const employees = await response.json();

        displayEmployees(employees);

        totalPages = 0;

        document.getElementById("pageInfo").textContent =
            "Filtered Results";

        document.getElementById("previousBtn").disabled = true;
        document.getElementById("nextBtn").disabled = true;

        message.textContent =
            `Found ${employees.length} employee(s).`;

    } catch (error) {

        message.textContent = error.message;

        console.error(error);
    }
}


// Show all employees
document.getElementById("showAllBtn")
    .addEventListener("click", function () {

        document.getElementById("departmentSearch").value = "";
        document.getElementById("minSalary").value = "";

        loadEmployees(0);
    });


// Previous page
document.getElementById("previousBtn")
    .addEventListener("click", function () {

        if (currentPage > 0) {
            loadEmployees(currentPage - 1);
        }
    });


// Next page
document.getElementById("nextBtn")
    .addEventListener("click", function () {

        if (currentPage < totalPages - 1) {
            loadEmployees(currentPage + 1);
        }
    });


// Initial loading
if (token) {
    loadEmployees();
}
