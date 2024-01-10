document.addEventListener('DOMContentLoaded', (event) => {
    window.onload = function() {
        var form = document.querySelector("form");
        form.noValidate = true;

        form.onsubmit = function(e) {
            e.preventDefault();
            var username = document.getElementById("username");
            var password = document.getElementById("password");
            var confirmPassword = document.getElementById("confirmPassword");

            var usernameError = document.getElementById("usernameError");
            var passwordError = document.getElementById("passwordError");
            var confirmPasswordError = document.getElementById("confirmPasswordError");

            var errors = false;

            // reset the input fields styles
            username.classList.remove('invalid');
            void username.offsetWidth;
            password.classList.remove('invalid');
            void password.offsetWidth;
            confirmPassword.classList.remove('invalid');
            void confirmPassword.offsetWidth;

            usernameError.textContent = '';
            passwordError.textContent = '';
            confirmPasswordError.textContent = '';

            if(username.value === '') {
                usernameError.textContent = "Username is required.";
                usernameError.classList.add("active");
                username.classList.add('invalid');
                errors = true;
            }

            if(password.value === '') {
                passwordError.textContent = "Password is required.";
                passwordError.classList.add("active");
                password.classList.add('invalid');
                errors = true;
            } else if(password.value.length < 5) {
                passwordError.textContent = "Password must be at least 5 characters long.";
                passwordError.classList.add("active");
                password.classList.add('invalid');
                errors = true;
            }

            if(confirmPassword.value === '') {
                confirmPasswordError.textContent = "Confirm Password is required.";
                confirmPasswordError.classList.add("active");
                confirmPassword.classList.add('invalid');
                errors = true;
            } else if(password.value !== confirmPassword.value) {
                confirmPasswordError.textContent = "Passwords do not match.";
                confirmPasswordError.classList.add("active");
                confirmPassword.classList.add('invalid');
                errors = true;
            }
            //Fetch response from backend
            if (!errors) {
                fetch(form.action, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: username.value,
                        password: password.value
                    })
                })
                .then(response => {
                    if(response.ok) {
                        window.location.href = '/login';
                    } else {
                        response.text().then(text => {
                            confirmPasswordError.textContent = 'Something went wrong. Try Again.';
                            confirmPassword.classList.add('invalid');
                        });
                    }
                });
            }
        }
    }
});