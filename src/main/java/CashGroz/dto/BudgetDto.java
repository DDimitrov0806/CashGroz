package CashGroz.dto;

import java.time.LocalDate;

import CashGroz.models.Budget;

public class BudgetDto {
    private Integer id;
    private Double amount;
    private Integer categoryId;
    private LocalDate dateTimeFrom;
    private LocalDate dateTimeTo;

    public BudgetDto(Double amount, LocalDate dateTimeFrom, LocalDate dateTimeTo, Integer categoryId) {
        this.amount = amount;
        this.dateTimeFrom = dateTimeFrom;
        this.dateTimeTo = dateTimeTo;
        this.categoryId = categoryId;
    }
    public BudgetDto(Integer id, Double amount, LocalDate dateTimeFrom, LocalDate dateTimeTo, Integer categoryId) {
        this.id = id;
        this.amount = amount;
        this.dateTimeFrom = dateTimeFrom;
        this.dateTimeTo = dateTimeTo;
        this.categoryId = categoryId;
    }
    public BudgetDto() {
        
    }

    public BudgetDto(Budget budget) {
        this.id = budget.getId();
        this.amount = budget.getAmount();
        this.dateTimeFrom = budget.getDateTimeFrom();
        this.dateTimeTo = budget.getDateTimeTo();
        this.categoryId = budget.getCategory().getId();
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

    public LocalDate getDateTimeFrom() {
        return dateTimeFrom;
    }
    public void setDateTimeFrom(LocalDate dateTimeFrom) {
        this.dateTimeFrom = dateTimeFrom;
    }

    public LocalDate getDateTimeTo() {
        return dateTimeTo;
    }
    public void setDateTimeTo(LocalDate dateTimeTo) {
        this.dateTimeTo = dateTimeTo;
    }
}