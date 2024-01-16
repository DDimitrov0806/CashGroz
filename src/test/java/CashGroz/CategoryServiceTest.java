package CashGroz;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import CashGroz.dto.CategoryDto;
import CashGroz.models.Category;
import CashGroz.models.User;
import CashGroz.repositories.CategoryRepository;
import CashGroz.repositories.UserRepository;
import CashGroz.services.CategoryService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    // FOR createCategory METHOD
    @Test
    public void testCreateCategory() {
        String username = "testUser";
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("testCategory");
        categoryDto.setIcon("testIcon");
        categoryDto.setColor("#ffffff");

        User user = new User(1, username, "testPassword");
        when(userRepository.findByUsername(username)).thenReturn(user);

        categoryService.createCategory(categoryDto, username);

        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(1)).save(ArgumentMatchers.any(Category.class));
    }

    @Test
    public void shouldNotCreateCategoryIfUsernameDoesNotExist() {
        String username = "testUser";
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("testCategory");
        categoryDto.setIcon("testIcon");
        categoryDto.setColor("#ffffff");

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            categoryService.createCategory(categoryDto, username);
        });

        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    public void shouldCreateCategoryEvenIfCategoryDtoFieldsAreNullOrEmpty() {
        String username = "testUser";
        User user = new User(1, username, "testPassword");
        CategoryDto categoryDto = new CategoryDto();

        when(userRepository.findByUsername(username)).thenReturn(user);

        categoryService.createCategory(categoryDto, username);

        verify(userRepository, times(1)).findByUsername(username);
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository, times(1)).save(categoryCaptor.capture());
        Category category = categoryCaptor.getValue();
        assertEquals(user, category.getUser());
        assertNull(category.getName());
        assertNull(category.getIcon());
    }

    // FOR getAllByUsername METHOD
    @Test
    public void shouldReturnAllCategoriesForAUser() {
        String username = "testUser";
        User user = new User(1, username, "testPassword");
        Category category1 = new Category("category1", user, "icon1", "#ADD533");
        Category category2 = new Category("category2", user, "icon2", "#B2452D");

        List<Category> categories = Arrays.asList(category1, category2);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(categoryRepository.findAllByUserId(user.getId())).thenReturn(categories);

        List<Category> returnedCategories = categoryService.getAllByUsername(username);

        assertEquals(2, returnedCategories.size());
        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(1)).findAllByUserId(user.getId());
    }

    @Test
    public void shouldReturnEmptyListWhenUsernameIsNull() {
        String username = null;

        List<Category> categories = categoryService.getAllByUsername(username);

        assertTrue(categories.isEmpty());
        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(0)).findAllByUserId(any(Integer.class));
    }

    @Test
    public void shouldReturnEmptyListWhenUsernameIsEmpty() {
        String username = "";

        List<Category> categories = categoryService.getAllByUsername(username);

        assertTrue(categories.isEmpty());
        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(0)).findAllByUserId(any(Integer.class));
    }

    @Test
    public void shouldReturnEmptyListWhenUserNotFound() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        List<Category> categories = categoryService.getAllByUsername(username);

        assertTrue(categories.isEmpty());
        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(0)).findAllByUserId(any(Integer.class));
    }

    // FOR updateCategory METHOD
    @Test
    public void shouldUpdateCategory() {
        String username = "testUser";
        User user = new User(1, username, "testPassword");
        Category category = new Category("category1", user, "icon1", "#B2452D");
        category.setId(1);

        when(userRepository.findByUsername(username)).thenReturn(user);

        categoryService.updateCategory(category, username);

        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(1)).save(category);
        assertEquals(user, category.getUser());
    }

    @Test
    public void shouldNotUpdateCategoryWhenUsernameIsEmpty() {
        Category category = new Category("category1", new User(), "icon1", "#B2452D");

        assertThrows(UsernameNotFoundException.class, () -> {
            categoryService.updateCategory(category, "");
        });

        verify(userRepository, times(1)).findByUsername("");
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        String username = "testUser";
        Category category = new Category("category1", new User(), "icon1", "#B2452D");

        when(userRepository.findByUsername(username)).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            categoryService.updateCategory(category, username);
        });

        verify(userRepository, times(1)).findByUsername(username);
        verify(categoryRepository, times(0)).save(any(Category.class));
        String expectedMessage = "The user with username: " + username + " was not found"; // updated expected message
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // FOR getCategoryById METHOD
    @Test
    public void shouldReturnCategoryById() {
        Integer categoryId = 1;
        Category category = new Category("category1", new User(), "icon1", "#B2452D");
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Optional<Category> returnedCategory = categoryService.getCategoryById(categoryId);

        assertTrue(returnedCategory.isPresent());
        assertEquals(categoryId, returnedCategory.get().getId());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenCategoryDoesNotExist() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Optional<Category> returnedCategory = categoryService.getCategoryById(categoryId);

        assertFalse(returnedCategory.isPresent());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void shouldThrowExceptionWhenCategoryIdIsNull() {
        Integer categoryId = null;

        when(categoryRepository.findById(null)).thenThrow(new IllegalArgumentException("ID must not be null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });

        String expectedMessage = "ID must not be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // FOR DELETE METHOD
    @Test
    public void shouldDeleteCategory() {
        Integer categoryId = 1;
        Category category = new Category("category1", new User(), "icon1", "#B2452D");
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category returnedCategory = categoryService.deleteCategory(categoryId);

        assertNotNull(returnedCategory);
        assertEquals(categoryId, returnedCategory.getId());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void shouldReturnNullWhenCategoryDoesNotExist() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Category returnedCategory = categoryService.deleteCategory(categoryId);

        assertNull(returnedCategory);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(0)).deleteById(anyInt());
    }

    @Test
    public void shouldReturnNullWhenCategoryAlreadyDeleted() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Category returnedCategory = categoryService.deleteCategory(categoryId);

        assertNull(returnedCategory);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(0)).deleteById(anyInt());
    }

    @Test
    public void shouldThrowDataAccessExceptionWhenDeletionFails() {
        Integer categoryId = 1;
        Category category = new Category("category1", new User(), "icon1", "#B2452D");
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doThrow(new DataAccessException("Could not access database") {
        }).when(categoryRepository).deleteById(anyInt());

        Exception exception = assertThrows(DataAccessException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });

        assertEquals("Could not access database", exception.getMessage());
    }
}