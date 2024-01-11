function createCategory() {
    const categoryNameInput = document.getElementById("category-name-input");
    const newCategory = { name: categoryNameInput.value };
    fetch('/categories', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newCategory)
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/categories';
        } else {
            console.error("Error creating category:", response.status);
        }
        categoryNameInput.value = "";
    });
}


// function addAmountToCategory() {
//     const categoryId = document.querySelector('.card.selected').id;
//     const amountInput = document.getElementById("amount-input");
//     const amount = amountInput.value;
//     const updatedCategory = { amount: amount };

//     fetch(`/categories/${categoryId}`, {
//         method: 'PUT',
//         headers: { 'Content-Type': 'application/json' },
//         body: JSON.stringify(updatedCategory)
//     })
//     .then(response => {
//         if (response.ok) {
//             const category = categories.find(category => category.id == categoryId);
//             category.amount += amount;
//             displayCategories();
//         } else {
//             console.error("Error adding amount to category:", response.status);
//         }
//         amountInput.value = "";
//     });
// }

document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById("create-category-button").addEventListener("click", createCategory);
});