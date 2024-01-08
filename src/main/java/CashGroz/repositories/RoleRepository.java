package CashGroz.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import CashGroz.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
