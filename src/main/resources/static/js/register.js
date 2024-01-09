window.onload = function() {
    var form = document.querySelector("form");
    form.noValidate = true;

    form.onsubmit = function(e) {
        e.preventDefault();
        var email = document.getElementById("email");
        var username = document.getElementById("username");
        var password = document.getElementById("password");
        var confirmPassword = document.getElementById("confirmPassword");

        var emailError = document.getElementById("emailError");
        var usernameError = document.getElementById("usernameError");
        var passwordError = document.getElementById("passwordError");
        var confirmPasswordError = document.getElementById("confirmPasswordError");

        var errors = false;

        // reset the input fields styles
        email.classList.remove('invalid');
        void email.offsetWidth;
        username.classList.remove('invalid');
        void username.offsetWidth;
        password.classList.remove('invalid');
        void password.offsetWidth;
        confirmPassword.classList.remove('invalid');
        void confirmPassword.offsetWidth;

        emailError.textContent = '';
        usernameError.textContent = '';
        passwordError.textContent = '';
        confirmPasswordError.textContent = '';

        if(email.value === '') {
            emailError.textContent = "Email is required.";
            emailError.classList.add("active");
            email.classList.add('invalid');
            errors = true;
        } else if(!/\S+@\S+\.\S+/.test(email.value)) {
            emailError.textContent = "Not a valid email.";
            emailError.classList.add("active");
            email.classList.add('invalid');
            errors = true;
        }

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
        } else if(password.value.length < 8) {
            passwordError.textContent = "Password must be at least 8 characters long.";
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
                method: form.method,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email.value,
                    username: username.value,
                    password: password.value
                })
            })
            .then(response => {
                if(response.ok) {
                    window.location.href = '/api/login';
                } else {
                    response.text().then(text => {
                        confirmPasswordError.textContent = 'Something went wrong. Try Again.';
                        confirmPassword.classList.add('invalid');
                    });
                }
            })
            .catch(error => console.error('Error:', error));
        }
    }
}