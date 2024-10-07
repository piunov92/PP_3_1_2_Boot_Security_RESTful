async function loadUsers() {
    try {
        const response = await fetch('/api/users');
        const users = await response.json();

        const tableBody = document.querySelector('#usersTable tbody');
        tableBody.innerHTML = '';

        users.forEach(user => {
            const roles = user.roles.map(role => role.name).join(', ');
            const row = `
             <tr>
                <td>${user.id}</td>
                <td>${user.password}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${roles}</td>
                 <td class="text-end">
                    <div class="d-flex justify-content-end gap-3">
                        <button type="button" class="btn btn-primary"
                                style="--bs-btn-padding-y: .25rem; --bs-btn-padding-x: 1.5rem; --bs-btn-font-size: 1.1rem;"
                                data-bs-toggle="modal"
                                data-bs-target="#edit_modal"
                                onclick='editUser(${JSON.stringify(user)})'>Edit
                        </button>
                        <button type="button" class="btn btn-danger"
                                style="--bs-btn-padding-y: .25rem; --bs-btn-font-size: 1.1rem;"
                                data-bs-toggle="modal"
                                data-bs-target="#delete_modal"
                                onclick='deleteUser(${JSON.stringify(user)})'>
                            Delete
                        </button>
                    </div>
                </td>
             </tr>
        `;
            tableBody.insertAdjacentHTML('afterbegin', row);
        });
    } catch (error) {
        console.error('Ошибка при загрузке пользователей:', error);
    }
}