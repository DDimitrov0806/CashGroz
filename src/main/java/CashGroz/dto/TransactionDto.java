package CashGroz.dto;

import java.time.LocalDateTime;

import CashGroz.models.Transaction;

public class TransactionDto {
    private Integer id;
    private Double amount;
    private String description;
    private LocalDateTime dateTime;
    private Integer categoryId;

    public TransactionDto(Integer id, Double amount, String description, LocalDateTime dateTime, Integer categoryId) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.dateTime = dateTime;
        this.categoryId = categoryId;
    }

    public TransactionDto(Double amount, String description, LocalDateTime dateTime, Integer categoryId) {
        this.amount = amount;
        this.description = description;
        this.dateTime = dateTime;
        this.categoryId = categoryId;
    }

    public TransactionDto() {
    }

    public TransactionDto(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.dateTime = transaction.getDateTime();
        this.categoryId = transaction.getCategory().getId();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
