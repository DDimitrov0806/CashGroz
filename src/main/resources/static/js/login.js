window.onload = function() {
    var form = document.querySelector("form");

    form.onsubmit = function(e) {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;

        if(username.length < 5) {
            alert("Username must be at least 5 characters long.");
            e.preventDefault();
        }
        
        if(password.length < 8) {
            alert("Password must be at least 8 characters long.");
            e.preventDefault();
        }
    }
}