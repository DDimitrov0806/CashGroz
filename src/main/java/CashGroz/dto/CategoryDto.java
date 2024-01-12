package CashGroz.dto;

public class CategoryDto {
    private String name;
    private String icon;

    public CategoryDto() {
    }

    public CategoryDto(String name, String icon) {
        this.name = name;
        this.icon = icon;
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
}
