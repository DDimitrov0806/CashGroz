package CashGroz.dto;

import java.time.LocalDateTime;

public class BudgetDto {
    private Integer id;
    private Double amount;
    private Integer categoryId;
    private LocalDateTime dateTimeFrom;
    private LocalDateTime dateTimeTo;

    public BudgetDto(Double amount, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Integer categoryId) {
        this.amount = amount;
        this.dateTimeFrom = dateTimeFrom;
        this.dateTimeTo = dateTimeTo;
        this.categoryId = categoryId;
    }
    public BudgetDto(Integer id, Double amount, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Integer categoryId) {
        this.id = id;
        this.amount = amount;
        this.dateTimeFrom = dateTimeFrom;
        this.dateTimeTo = dateTimeTo;
        this.categoryId = categoryId;
    }
    public BudgetDto() {
        
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getDateTimeFrom() {
        return dateTimeFrom;
    }
    public void setDateTimeFrom(LocalDateTime dateTimeFrom) {
        this.dateTimeFrom = dateTimeFrom;
    }

    public LocalDateTime getDateTimeTo() {
        return dateTimeTo;
    }
    public void setDateTimeTo(LocalDateTime dateTimeTo) {
        this.dateTimeTo = dateTimeTo;
    }
}