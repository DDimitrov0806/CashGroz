document.addEventListener('DOMContentLoaded', (event) => {
    var path = window.location.pathname;
    var link = document.querySelector(`nav a[href="${path}"]`);
    if (link) {
        link.classList.add('active');
    }
});