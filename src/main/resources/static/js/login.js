window.onload = function() {
    var form = document.querySelector("form");
    form.noValidate = true; // disable the browser's default validation

    form.onsubmit = function(e) {
        e.preventDefault();
        var username = document.getElementById("username");
        var password = document.getElementById("password");

        var usernameError = document.getElementById("usernameError");
        var passwordError = document.getElementById("passwordError");
        
        var errors = false;

        username.classList.remove('invalid');
        void username.offsetWidth; // trigger a DOM reflow
        password.classList.remove('invalid');
        void password.offsetWidth; // trigger a DOM reflow
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
                body: new FormData(form)
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
}