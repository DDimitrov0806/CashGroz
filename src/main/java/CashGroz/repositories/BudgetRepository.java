package CashGroz.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import CashGroz.models.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findAllByUserId(Integer userId);

    Optional<Budget> findByIdAndUserId(Integer id, Integer userId);
}