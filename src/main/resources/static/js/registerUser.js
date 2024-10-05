document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("registration_form");

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const formData = {
            username: document.getElementById("_username").value,
            password: document.getElementById("_password").value,
            email: document.getElementById("_email").value,
            roles: []
        };

        const selectedRoles = document.getElementById("new_user_tab_roles");
        for (let i = 0; i < selectedRoles.options.length; i++) {
            if (selectedRoles.options[i].selected) {
                formData.roles.push(selectedRoles.options[i].value);
            }
        }

        try {
            const response = await fetch('/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            const responseText = await response.text();
            const responseMessage = document.getElementById("responseMessage");

            if (response.ok) {
                responseMessage.textContent = responseText;
                responseMessage.style.color = "green";
                form.reset();
            } else {
                responseMessage.textContent = "Error: " + responseText.replace("{\"error\": ", "").replace("}", "");
                responseMessage.style.color = "red";
            }

            responseMessage.style.display = "block";
        } catch (error) {
            console.error("Ошибка при отправке запроса:", error);
            const responseMessage = document.getElementById("responseMessage");
            responseMessage.textContent = "Error: " + error.message();
            responseMessage.style.color = "red";
            responseMessage.style.display = "block";
        }
    });
});