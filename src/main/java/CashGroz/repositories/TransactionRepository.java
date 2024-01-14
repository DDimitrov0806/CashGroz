package CashGroz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import CashGroz.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    
}
