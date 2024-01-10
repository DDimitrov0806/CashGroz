document.addEventListener('DOMContentLoaded', (event) => {
    var cards = document.querySelectorAll('.card');
    cards.forEach((card) => {
        card.addEventListener('click', (event) => {
            // Get the id of the clicked card
            var categoryId = event.currentTarget.id;
            // Navigate to a new page or open a modal to add an income or expense
            // For now, we'll just log the clicked category
            console.log('Clicked category:', categoryId);
        });
    });
});