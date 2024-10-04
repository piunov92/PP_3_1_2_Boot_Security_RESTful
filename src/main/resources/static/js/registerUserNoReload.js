document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registration_form');
    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const formData = new FormData(form);
        const data = new URLSearchParams();

        formData.forEach((value, key) => {
            data.append(key, value);
        });

        try {
            const response = await fetch(form.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: data
            });

            if (response.ok) {
                form.reset();
                window.location.reload();
            } else if (response.status === 409) {
                document.getElementById('errorMessage').textContent = await response.text();
                document.getElementById('errorMessage').style.display = 'block';
                document.getElementById('successMessage').style.display = 'none';
            }
        } catch (error) {
            console.error('Error form submit :(', error);
        }
    });
});