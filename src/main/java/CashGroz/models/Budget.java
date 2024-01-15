package CashGroz.models;

import java.time.LocalDateTime;

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
    private LocalDateTime dateTimeFrom;
    @Column(nullable = false)
    private LocalDateTime dateTimeTo;

    public Budget(Double amount, User user, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Category category) {
        this.amount = amount;
        this.user = user;
        this.dateTimeFrom = dateTimeFrom;
        this.dateTimeTo = dateTimeTo;
        this.category = category;
    }
    public Budget(Double amount, Integer id, User user, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Category category) {
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