let currentUser;

function deleteUser(user) {
    currentUser = user.id;
    document.getElementById('delete_form_id').value = currentUser;
    document.getElementById('delete_form_password').value = "";
    document.getElementById('delete_form_username').value = user.username;
    document.getElementById('delete_form_email').value = user.email;

    const roles = user.roleNames;
    const select = document.getElementById('delete_form_roles');
    select.innerHTML = '';
    roles.forEach(item => {
        const option = document.createElement('option');
        option.value = item;
        option.textContent = item;
        select.appendChild(option);
    });
}

document.getElementById("confirmDeleteBtn").addEventListener("click", async function () {
    try {
        const response = await fetch(`/api/users/${currentUser}`, {
            method: 'DELETE'
        });

            const deleteResponseMessage = document.getElementById("deleteResponseMessage");
        if (response.ok) {
            deleteResponseMessage.textContent = "User deleted successfully!";
            deleteResponseMessage.style.color = "green";
            loadUsers();
        } else {
            deleteResponseMessage.textContent = "Error: " + await response.text();
            deleteResponseMessage.style.color = "red";
        }
        deleteResponseMessage.style.display = "block";
        $('#delete_modal').modal('hide');
        setTimeout(() => {
            deleteResponseMessage.style.display = "none";
        }, 1000);
    } catch (error) {
        console.error('Error deleting user:', error);
    }
});









