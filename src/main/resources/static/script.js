const apiBaseUrl = "http://localhost:9001/api/employees";

// Load Employees on Page Load
document.addEventListener("DOMContentLoaded", () => {
    loadEmployees();
    document.getElementById("employeeForm").addEventListener("submit", saveEmployee);
});

// Fetch and Display Employees
function loadEmployees() {
    fetch(apiBaseUrl)
        .then(response => response.json())
        .then(data => {
            const employeeTableBody = document.getElementById("employeeTableBody");
            employeeTableBody.innerHTML = "";
            data.forEach(employee => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${employee.id}</td>
                    <td>${employee.name}</td>
                    <td>${employee.email}</td>
                    <td>${employee.department}</td>
                    <td>
                        <button class="btn" onclick="editEmployee(${employee.id})">Edit</button>
                        <button class="btn btn-danger" onclick="deleteEmployee(${employee.id})">Delete</button>
                    </td>
                `;
                employeeTableBody.appendChild(row);
            });
        });
}

// Save or Update Employee
function saveEmployee(event) {
    event.preventDefault();

    const id = document.getElementById("employeeId").value;
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const department = document.getElementById("department").value;

    const employee = { name, email, department };

    const method = id ? "PUT" : "POST";
    const url = id ? `${apiBaseUrl}/${id}` : apiBaseUrl;

    fetch(url, {
        method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(employee)
    })
        .then(() => {
            document.getElementById("employeeForm").reset();
            loadEmployees();
        })
        .catch(error => console.error("Error:", error));
}

// Edit Employee
function editEmployee(id) {
    fetch(`${apiBaseUrl}/${id}`)
        .then(response => response.json())
        .then(employee => {
            document.getElementById("employeeId").value = employee.id;
            document.getElementById("name").value = employee.name;
            document.getElementById("email").value = employee.email;
            document.getElementById("department").value = employee.department;
        });
}

// Delete Employee
function deleteEmployee(id) {
    fetch(`${apiBaseUrl}/${id}`, { method: "DELETE" })
        .then(() => loadEmployees())
        .catch(error => console.error("Error:", error));
}
