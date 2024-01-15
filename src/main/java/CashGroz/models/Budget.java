package CashGroz.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private LocalDate dateTimeFrom;
    @Column(nullable = false)
    private LocalDate dateTimeTo;

    public Budget(Double amount, User user, LocalDate dateTimeFrom, LocalDate dateTimeTo, Category category) {
        this.amount = amount;
        this.user = user;
        this.dateTimeFrom = dateTimeFrom;
        this.dateTimeTo = dateTimeTo;
        this.category = category;
    }
    public Budget(Double amount, Integer id, User user, LocalDate dateTimeFrom, LocalDate dateTimeTo, Category category) {
        this.amount = amount;
        this.id = id;
        this.user = user;
        this.dateTimeFrom = dateTimeFrom;
        this.dateTimeTo = dateTimeTo;
        this.category = category;
    }
    public Budget() {

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

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
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