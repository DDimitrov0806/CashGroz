window.onload = function() {
    var form = document.querySelector("form");

    form.onsubmit = function(e) {
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

        if(errors) e.preventDefault();
    }
}