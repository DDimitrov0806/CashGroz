package CashGroz.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CashGroz.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByUserId(Integer userId);
}
