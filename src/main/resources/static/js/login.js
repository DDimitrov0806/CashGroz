document.addEventListener('DOMContentLoaded', (event) => {
    var form = document.querySelector("form");
    console.log(form);
    form.noValidate = true; // disable the browser default validation

    form.onsubmit = function(e) {
        e.preventDefault();
        var username = document.getElementById("username");
        var password = document.getElementById("password");

        var usernameError = document.getElementById("usernameError");
        var passwordError = document.getElementById("passwordError");
        
        var errors = false;

        username.classList.remove('invalid');
        void username.offsetWidth;
        password.classList.remove('invalid');
        void password.offsetWidth;
        usernameError.textContent = '';
        passwordError.textContent = '';

        if(username.value.length < 5) {
            usernameError.textContent = "Username must be at least 5 characters long.";
            usernameError.classList.add("active");
            username.classList.add('invalid');
            errors = true;
        }
        
        if(password.value.length < 8) {
            passwordError.textContent = "Password must be at least 8 characters long.";
            passwordError.classList.add("active");
            password.classList.add('invalid');
            errors = true;
        }
        //Fetch response from backend
        if(!errors) {
            fetch(form.action, {
                method: form.method,
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
                    window.location.href = '/index';
                } else {
                    usernameError.textContent = 'Wrong username or password.';
                    username.classList.add('invalid');
                    passwordError.textContent = 'Wrong username or password.';
                    password.classList.add('invalid');
                }
            })
            .catch(error => console.error('Error:', error));
        }
    }
});