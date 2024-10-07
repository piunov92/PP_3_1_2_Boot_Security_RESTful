{
    let userID;
    let roles = [];

    async function loadRoles(selectedRoles = []) {
        try {
            const response = await fetch('/api/roles');
            const roles = await response.json();
            const rolesSelect = document.getElementById('edit_form_roles');

            rolesSelect.innerHTML = '';

            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.name;
                option.textContent = role.name;

                if (selectedRoles.some(userRole => userRole.id === role.id)) {
                    option.selected = true;
                }

                rolesSelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error loading roles:', error);
        }
    }

    function editUser(user) {
        userID = user.id;
        document.getElementById('edit_form_id').value = userID;
        document.getElementById('edit_form_password').value = "";
        document.getElementById('edit_form_username').value = user.username;
        document.getElementById('edit_form_email').value = user.email;

        roles = user.roles;
        loadRoles(user.roles);
    }

    document.getElementById("edit_form").addEventListener("submit", async function (event) {
        event.preventDefault();

        const selectedRoles = Array.from(document.getElementById("edit_form_roles").selectedOptions).map(option => option.value);
        const rolesToSubmit = selectedRoles.length > 0 ? selectedRoles : roles;

        const formData = {
            id: document.getElementById("edit_form_id").value,
            username: document.getElementById("edit_form_username").value,
            password: document.getElementById("edit_form_password").value,
            email: document.getElementById("edit_form_email").value,
            roles: rolesToSubmit
        };

        try {
            const response = await fetch(`/api/users/${userID}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            const responseText = await response.text();
            const editResponseMessage = document.getElementById("editResponseMessage");

            if (response.ok) {
                editResponseMessage.textContent = responseText;
                editResponseMessage.style.color = "green";
                await loadUsers();
            } else {
                editResponseMessage.textContent = "Error: " + responseText.replace("{\"error\": ", "").replace("}", "");
                editResponseMessage.style.color = "red";
            }

            editResponseMessage.style.display = "block";
            $('#edit_modal').modal('hide');
            setTimeout(() => {
                editResponseMessage.style.display = "none";
            }, 1000);
        } catch (error) {
            console.error("Ошибка при отправке запроса:", error);
            const editResponseMessage = document.getElementById("editResponseMessage");
            editResponseMessage.textContent = "Error: " + error.message();
            editResponseMessage.style.color = "red";
            editResponseMessage.style.display = "block";
        }
    });
}

