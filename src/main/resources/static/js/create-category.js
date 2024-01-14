document.querySelectorAll(".icon-card").forEach((item) => {
  item.addEventListener("click", (event) => {
    const iconName = item.getAttribute("data-icon-name");
    document.querySelectorAll(".icon-card").forEach((icon) => {
      icon.classList.remove("selected-card");
    });

    item.classList.add("selected-card");
    document.getElementById("icon").value = iconName;

    const selectedColor = document.getElementById("category-color-input").value;
    document.getElementById("color").value = selectedColor;
  });
});