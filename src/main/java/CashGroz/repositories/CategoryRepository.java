package CashGroz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import CashGroz.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
