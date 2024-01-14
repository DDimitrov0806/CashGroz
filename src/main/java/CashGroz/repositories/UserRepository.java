package CashGroz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CashGroz.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
