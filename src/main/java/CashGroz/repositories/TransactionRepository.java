package CashGroz.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import CashGroz.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findByIdAndUserId(Integer id, Integer userId);
}
