package CashGroz;

import CashGroz.services.BudgetService;
import CashGroz.services.TransactionService;
import CashGroz.models.Budget;
import CashGroz.models.Category;
import CashGroz.models.Transaction;
import CashGroz.models.User;
import CashGroz.dto.BudgetDto;
import CashGroz.repositories.BudgetRepository;
import CashGroz.repositories.CategoryRepository;
import CashGroz.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BudgetServiceTest {

    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionService transactionService;

    // For getAmountSpentByUsernameAndPeriod METHOD

    @Test
    public void testGetAmountSpentByUsernameAndPeriod() {
        String username = "test";
        Budget budget = new Budget();
        budget.setAmount(1000.0);
        budget.setCategory(new Category());

        Transaction transaction = new Transaction();
        transaction.setAmount(500.0);
        transaction.setCategory(budget.getCategory());

        List<Transaction> transactions = Arrays.asList(transaction);

        when(transactionService.getAllByUsernameAndPeriod(username, budget.getDateTimeFrom(), budget.getDateTimeTo()))
                .thenReturn(transactions);

        Double amountSpent = budgetService.getAmountSpentByUsernameAndPeriod(username, budget);

        assertEquals(500.0, amountSpent, 0.01);
    }

    @Test
    public void testGetAmountSpentWithDifferentCategory() {
        String username = "test";
        Budget budget = new Budget();
        budget.setCategory(new Category());

        Transaction transaction = new Transaction();
        transaction.setCategory(new Category());
        transaction.setAmount(500.0);

        List<Transaction> transactions = Arrays.asList(transaction);

        when(transactionService.getAllByUsernameAndPeriod(username, budget.getDateTimeFrom(), budget.getDateTimeTo()))
                .thenReturn(transactions);

        Double amountSpent = budgetService.getAmountSpentByUsernameAndPeriod(username, budget);

        assertEquals(0.0, amountSpent, 0.01);
    }

    @Test
    public void testGetAmountSpentWithNoTransactions() {
        String username = "test";
        Budget budget = new Budget();

        List<Transaction> transactions = Arrays.asList();

        when(transactionService.getAllByUsernameAndPeriod(username, budget.getDateTimeFrom(), budget.getDateTimeTo()))
                .thenReturn(transactions);

        Double amountSpent = budgetService.getAmountSpentByUsernameAndPeriod(username, budget);

        assertEquals(0.0, amountSpent, 0.01);
    }

    // For getAllBudgetsByUsername METHOD

    @Test
    public void testGetAllBudgetsByUsername() {
        String username = "test";
        User user = new User();
        Budget budget = new Budget();
        List<Budget> budgets = Arrays.asList(budget);
        user.setBudgets(budgets);

        when(userRepository.findByUsername(username)).thenReturn(user);

        List<Budget> result = budgetService.getAllBudgetsByUsername(username);

        assertEquals(budgets, result);
    }

    @Test
    public void testGetAllBudgetsByUsernameWithNoBudgets() {

        String username = "test";
        User user = new User();

        when(userRepository.findByUsername(username)).thenReturn(user);

        List<Budget> result = budgetService.getAllBudgetsByUsername(username);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllBudgetsByUsernameWithNonexistentUser() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            budgetService.getAllBudgetsByUsername(username);
        });

        assertEquals("User not found", exception.getMessage());
    }

    // For getBudgetById METHOD

    @Test
    public void testGetBudgetById() {
        Budget budget = new Budget();
        Integer id = 1;
        when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

        Optional<Budget> result = budgetService.getBudgetById(id);

        assertEquals(Optional.of(budget), result);
        verify(budgetRepository, times(1)).findById(id);
    }

    @Test
    public void testGetBudgetByIdWithExistingBudget() {
        Integer id = 1;
        Budget budget = new Budget();

        when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

        Optional<Budget> result = budgetService.getBudgetById(id);

        assertTrue(result.isPresent());
        assertEquals(budget, result.get());
    }

    @Test
    public void testGetBudgetByIdWithNonexistentBudget() {
        Integer id = 1;

        when(budgetRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Budget> result = budgetService.getBudgetById(id);

        assertFalse(result.isPresent());
    }

    // For createBudget METHOD

    @Test
    public void testCreateBudget() {
        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setAmount(2000.0);
        budgetDto.setId(1);
        budgetDto.setCategoryId(1);
        budgetDto.setDateTimeFrom(LocalDate.now());
        budgetDto.setDateTimeTo(LocalDate.now());

        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        Category category = new Category();
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));

        budgetService.createBudget(budgetDto, "username");

        verify(budgetRepository, times(1)).save(any());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(categoryRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testCreateBudgetWithNonexistentCategory() {
        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setCategoryId(1);
        String username = "test";

        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(categoryRepository.findById(budgetDto.getCategoryId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            budgetService.createBudget(budgetDto, username);
        });

        assertNotNull(exception);
    }

    @Test
    public void testCreateBudgetWithNonexistentUser() {
        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setCategoryId(1);
        String username = "test";

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(categoryRepository.findById(budgetDto.getCategoryId())).thenReturn(Optional.of(new Category()));

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            budgetService.createBudget(budgetDto, username);
        });

        assertEquals("User not found", exception.getMessage());
    }

    // For updateBudget METHOD

    @Test
    public void testUpdateBudget() {
        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setAmount(2000.0);
        budgetDto.setId(1);
        budgetDto.setCategoryId(1);
        String username = "test";

        User user = new User();
        Category category = new Category();

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(categoryRepository.findById(budgetDto.getCategoryId())).thenReturn(Optional.of(category));

        budgetService.updateBudget(budgetDto, username);

        verify(budgetRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateBudgetWithNonexistentUser() {
        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setCategoryId(1);
        String username = "test";

        when(userRepository.findByUsername(username)).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            budgetService.updateBudget(budgetDto, username);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testUpdateBudgetWithNonexistentCategory() {
        BudgetDto budgetDto = new BudgetDto();
        budgetDto.setCategoryId(1);
        String username = "test";

        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(categoryRepository.findById(budgetDto.getCategoryId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            budgetService.updateBudget(budgetDto, username);
        });

        assertNotNull(exception);
    }

    // For deleteBudget METHOD

    @Test
    public void testDeleteBudget() {
        Integer budgetId = 1;
        Budget budget = new Budget();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        Budget result = budgetService.deleteBudget(budgetId);

        assertEquals(budget, result);
        verify(budgetRepository, times(1)).deleteById(budgetId);
    }

    @Test
    public void testDeleteBudgetWithExistingBudget() {
        Integer budgetId = 1;
        Budget budget = new Budget();

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        Budget result = budgetService.deleteBudget(budgetId);

        assertEquals(budget, result);
        verify(budgetRepository, times(1)).deleteById(budgetId);
    }

    @Test
    public void testDeleteBudgetWithNonexistentBudget() {
        Integer budgetId = 1;

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        Budget result = budgetService.deleteBudget(budgetId);

        assertNull(result);
    }
}