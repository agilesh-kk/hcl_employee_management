
// ===============================
// USER REGISTRATION
// ===============================

const registerForm = document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const username = document.getElementById("username").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;

        const message = document.getElementById("message");

        message.textContent = "Registering...";
        message.style.color = "blue";

        try {

            const response = await fetch("/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    email: email,
                    password: password
                })
            });

            const result = await response.json();

            if (response.ok) {

                message.style.color = "green";
                message.textContent =
                    result.message || "Registration successful!";

                registerForm.reset();

                setTimeout(() => {
                    window.location.href = "index.html";
                }, 1500);

            } else {

                message.style.color = "red";
                message.textContent =
                    result.message || result.error ||
                    "Registration failed.";

            }

        } catch (error) {

            message.style.color = "red";
            message.textContent = "Unable to connect to server.";

            console.error(error);
        }
    });
}


// ===============================
// USER LOGIN
// ===============================

const loginForm = document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value;

        const message = document.getElementById("loginMessage");

        message.textContent = "Logging in...";
        message.style.color = "blue";

        try {

            const response = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            const result = await response.json();

            if (response.ok) {

                // Store JWT tokens
                sessionStorage.setItem("accessToken", result.token);
                sessionStorage.setItem("refreshToken", result.refreshToken);

                message.style.color = "green";
                message.textContent = "Login successful!";

                setTimeout(() => {
                    window.location.href = "dashboard.html";
                }, 1000);

            } else {

                message.style.color = "red";
                message.textContent =
                    result.message || result.error ||
                    "Invalid username or password.";

            }

        } catch (error) {

            message.style.color = "red";
            message.textContent = "Unable to connect to server.";

            console.error(error);
        }
    });
}
