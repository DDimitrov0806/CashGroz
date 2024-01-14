package CashGroz.dto;

public class CategoryDto {
    private String name;
    private String icon;
    private String color;

    public CategoryDto() {
    }

    public CategoryDto(String name, String icon, String color) {
        this.name = name;
        this.icon = icon;
        this.color = color; // Initializing color
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
