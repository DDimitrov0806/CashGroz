let selectedCategory = null;

document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', function() {
        if (selectedCategory) {
            selectedCategory.classList.remove('card-selected');
        }
        
        selectedCategory = this;
        selectedCategory.classList.add('card-selected');
    });
});

document.querySelector('button').addEventListener('click', function() {
    let amount = document.querySelector('input').value;

    fetch('/category', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            category: selectedCategory,
            amount: amount
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});