package CashGroz.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import CashGroz.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByUserId(Integer userId);

    Optional<Category> findByIdAndUserId(Integer id, Integer userId);
}
