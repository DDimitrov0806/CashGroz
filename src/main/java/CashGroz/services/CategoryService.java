package CashGroz.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import CashGroz.dto.CategoryDto;
import CashGroz.models.Category;
import CashGroz.models.User;
import CashGroz.repositories.CategoryRepository;
import CashGroz.repositories.UserRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public void createCategory(CategoryDto categoryDto, String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Category category = new Category(categoryDto.getName(), user, categoryDto.getIcon());
            categoryRepository.save(category);
        }
    }

    public List<Category> getAllByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if(user == null) {
            return new ArrayList<>();
        }

        return categoryRepository.findAllByUserId(user.getId());
    }

    public void updateCategory(@NonNull Category category, String username) {
        if (username != null) {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                category.setUser(user);
                categoryRepository.save(category);
            }
        }
    }

    public Optional<Category> getCategoryById(@NonNull Integer id) {
        Optional<Category> category = categoryRepository.findById(id);

        return category;
    }

    public Category deleteCategory(@NonNull Integer id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            categoryRepository.deleteById(id);
            return category.get();
        }

        return null;
    }
}
