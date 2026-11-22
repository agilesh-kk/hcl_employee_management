
// Check whether the user is logged in
const accessToken = sessionStorage.getItem("accessToken");

if (!accessToken) {
    window.location.href = "index.html";
}

// Logout functionality
const logoutBtn = document.getElementById("logoutBtn");

if (logoutBtn) {

    logoutBtn.addEventListener("click", async function () {

        const refreshToken = sessionStorage.getItem("refreshToken");

        try {

            const response = await fetch("/api/auth/logout", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    refreshToken: refreshToken
                })
            });

            if (!response.ok) {
                console.error("Server logout failed");
            }

        } catch (error) {
            console.error("Logout error:", error);

        } finally {

            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshToken");

            window.location.href = "index.html";
        }
    });
}
