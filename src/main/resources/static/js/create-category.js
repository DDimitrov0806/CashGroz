document.querySelectorAll(".icon-card").forEach((item) => {
  const selectedIconName = document.getElementById("icon").value;
  
  if (item.getAttribute("data-icon-name") === selectedIconName) {
    item.classList.add("selected-card");
  }

  item.addEventListener("click", (event) => {
    document.querySelectorAll(".icon-card").forEach((icon) => {
      icon.classList.remove("selected-card");
    });

    item.classList.add("selected-card");
    document.getElementById("icon").value = item.getAttribute("data-icon-name");
  });
});