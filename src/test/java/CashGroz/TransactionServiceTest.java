package CashGroz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import CashGroz.dto.TransactionDto;
import CashGroz.models.Budget;
import CashGroz.models.Category;
import CashGroz.models.Transaction;
import CashGroz.models.User;
import CashGroz.repositories.CategoryRepository;
import CashGroz.repositories.TransactionRepository;
import CashGroz.repositories.UserRepository;
import CashGroz.services.TransactionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //FOR getAllByUsernameAndPeriod METHOD
    @Test
    public void testGetTransactionsByUserAndDateRange() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");

        Transaction transaction1 = new Transaction(500.00, "description", LocalDateTime.now(), new Category(), user);
        Transaction transaction2 = new Transaction(700.00, "description", LocalDateTime.now(), new Category(), user);

        user.setTransactions(List.of(transaction1, transaction2));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        List<Transaction> result = transactionService.getAllByUsernameAndPeriod("testUser", LocalDate.now().minusDays(5), LocalDate.now().plusDays(1));
        // Assert
        assertEquals(2, result.size());
        assertSame(result.get(0), transaction1);
        assertSame(result.get(1), transaction2);
    }
    //FOR getTransactionById METHOD

    @Test
    public void testGetTransactionById() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1);
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        // Act
        Optional<Transaction> result = transactionService.getTransactionById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testGetAmountSpentByUsernameAndPeriod_NoTransactions() {
        // Arrange
        String username = "testUser";
        Budget budget = new Budget();
        budget.setDateTimeFrom(LocalDate.now().minusDays(5));
        budget.setDateTimeTo(LocalDate.now());

        when(transactionService.getAllByUsernameAndPeriod(username, budget.getDateTimeFrom(), budget.getDateTimeTo())).thenReturn(List.of());

        // Act
        Double result = transactionService.getAmountSpentByUsernameAndPeriod(username, budget);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void testGetAmountSpentByUsernameAndPeriod_SingleTransaction() {
        // Arrange
        String username = "testUser";
        Category category = new Category();
        User user = new User();
        user.setUsername(username);
        Transaction transaction = new Transaction(500.00, "description", LocalDateTime.now(), category, user);
        Budget budget = new Budget();
        budget.setDateTimeFrom(LocalDate.now().minusDays(5));
        budget.setDateTimeTo(LocalDate.now());
        budget.setCategory(category);

        when(transactionService.getAllByUsernameAndPeriod(username, budget.getDateTimeFrom(), budget.getDateTimeTo())).thenReturn(List.of(transaction));

        // Act
        Double result = transactionService.getAmountSpentByUsernameAndPeriod(username, budget);

        // Assert
        assertEquals(500.0, result);
    }

    @Test
    void testGetAmountSpentByUsernameAndPeriod_MultipleTransactions() {
        // Arrange
        String username = "testUser";
        Category category = new Category();
        User user = new User();
        user.setUsername(username);
        Transaction transaction1 = new Transaction(500.00, "description", LocalDateTime.now(), category, user);
        Transaction transaction2 = new Transaction(700.00, "description", LocalDateTime.now(), category, user);
        Budget budget = new Budget();
        budget.setDateTimeFrom(LocalDate.now().minusDays(5));
        budget.setDateTimeTo(LocalDate.now());
        budget.setCategory(category);

        when(transactionService.getAllByUsernameAndPeriod(username, budget.getDateTimeFrom(), budget.getDateTimeTo())).thenReturn(List.of(transaction1, transaction2));

        // Act
        Double result = transactionService.getAmountSpentByUsernameAndPeriod(username, budget);

        // Assert
        assertEquals(1200.0, result);
    }









    //FOR deleteTransaction METHOD

    @Test
    public void testDeleteTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1);
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).deleteById(1);

        // Act
        Transaction result = transactionService.deleteTransaction(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }








    //FOR createTransaction METHOD

    @Test
    public void testCreateTransaction() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");

        Category category = new Category();
        category.setId(1);

        TransactionDto transactionDto = new TransactionDto(500.00, "description", LocalDateTime.now(), 1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // Act
        transactionService.createTransaction(transactionDto, "testUser");

        // Assert
        // Verify createTransaction is called
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }







    //FOR updateTransaction METHOD

    @Test
    public void testUpdateTransaction() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");

        Category category = new Category();
        category.setId(1);

        TransactionDto transactionDto = new TransactionDto(1, 500.00, "description", LocalDateTime.now(), 1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // Act
        transactionService.updateTransaction(transactionDto, "testUser");

        // Assert
        // Verify updateTransaction is called
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}