package CashGroz.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import CashGroz.models.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findAllByUserId(Integer userId);
}