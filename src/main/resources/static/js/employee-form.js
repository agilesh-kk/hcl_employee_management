
const employeeForm = document.getElementById("employeeForm");
const formMessage = document.getElementById("formMessage");

employeeForm.addEventListener("submit", async function(event) {

    event.preventDefault();

    const token = sessionStorage.getItem("accessToken");

    if (!token) {
        formMessage.textContent = "Please login first.";
        return;
    }

    const employee = {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        department: document.getElementById("department").value.trim(),
        salary: Number(document.getElementById("salary").value)
    };

    try {
        const response = await fetch("/api/employees", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(employee)
        });

        if (response.status === 403) {
            formMessage.textContent =
                "Access Denied! Only ADMIN users can add employees.";
            return;
        }

        const responseText = await response.text();

        let result = {};
        if (responseText) {
            try {
                result = JSON.parse(responseText);
            } catch {
                result = { message: responseText };
            }
        }

        if (!response.ok) {
            throw new Error(
                result.message || "Request failed. Status: " + response.status
            );
        }

        formMessage.textContent = "Employee added successfully!";
        employeeForm.reset();

    } catch (error) {
        formMessage.textContent = error.message;
        console.error(error);
    }
});
