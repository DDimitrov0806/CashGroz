window.onload = function() {
    var form = document.querySelector("form");

    form.onsubmit = function(e) {
        var email = document.getElementById("email").value;
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("confirmPassword").value;

        if(!email || !username || !password || !confirmPassword) {
            alert("All fields must be filled out.");
            e.preventDefault();
        } 
        
        if(password !== confirmPassword) {
            alert("Passwords do not match.");
            e.preventDefault();
        }
    }
}