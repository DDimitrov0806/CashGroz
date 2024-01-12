package CashGroz.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public void createCategory(CategoryDto categoryDto) {
        User user = getCurrentUser();
        Category category = new Category(categoryDto.getName(), user, categoryDto.getIcon());
        categoryRepository.save(category);
    }

    public List<Category> getAllByUsername() {
        User user = getCurrentUser();
        
        return categoryRepository.findAllByUserId(user.getId());
    }

    public Category updateCategory(Integer id, CategoryDto categoryDto) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category _category = existingCategory.get();
            _category.setName(categoryDto.getName());
            categoryRepository.save(_category);
            return _category;
        }

        return null;
    }

    public Optional<Category> getCategoryById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);

        return category;
    }

    public Category deleteCategory(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);

        if(category.isPresent()){
            categoryRepository.deleteById(id);
            return category.get();
        }

        return null;
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername("test1");

        return user;
    }
}
