document.querySelectorAll(".icon-card").forEach((item) => {
  item.addEventListener("click", (event) => {
    const iconName = item.getAttribute("data-icon-name");
    // Remove 'selected-icon' class from all icons
    document.querySelectorAll(".icon-card").forEach((icon) => {
      icon.classList.remove("selected-card");
    });

    // Add 'selected-icon' class to the clicked icon
    item.classList.add("selected-card");
    document.getElementById("icon").value = iconName;
  });
});
