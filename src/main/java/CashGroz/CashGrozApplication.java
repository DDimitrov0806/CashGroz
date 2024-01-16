package CashGroz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import CashGroz.models.Role;
import CashGroz.repositories.RoleRepository;

@SpringBootApplication
public class CashGrozApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashGrozApplication.class, args);
    }

    @Bean
    public CommandLineRunner addRoles(RoleRepository roleRepo) {
        return (args) -> {
            Role role = new Role();
            role.setName("USER");
            roleRepo.save(role);
        };
    }
}
