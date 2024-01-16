package CashGroz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import CashGroz.dto.TransactionDto;
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

    // FOR getAllByUsernameAndPeriod METHOD
    @Test
    public void testGetTransactionsByUserAndDateRange() {
        User user = new User();
        user.setUsername("testUser");

        Transaction transaction1 = new Transaction(500.00, "description", LocalDateTime.now(), new Category(), user);
        Transaction transaction2 = new Transaction(700.00, "description", LocalDateTime.now(), new Category(), user);

        user.setTransactions(List.of(transaction1, transaction2));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        List<Transaction> result = transactionService.getAllByUsernameAndPeriod("testUser",
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(1));
        assertEquals(2, result.size());
        assertSame(result.get(0), transaction1);
        assertSame(result.get(1), transaction2);
    }

    @Test
    public void testGetTransactionsByUser_NoTransactionsInPeriod() {
        User user = new User();
        user.setUsername("testUser");

        Transaction transaction1 = new Transaction(500.00, "description", LocalDateTime.now().minusDays(10),
                new Category(), user);
        Transaction transaction2 = new Transaction(700.00, "description", LocalDateTime.now().minusDays(10),
                new Category(), user);

        user.setTransactions(List.of(transaction1, transaction2));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        List<Transaction> result = transactionService.getAllByUsernameAndPeriod("testUser",
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(1));

        assertTrue(result.isEmpty()); // There should be no transaction in this period
    }

    @Test
    public void testGetTransactionsByUser_NullDates() {
        User user = new User();
        user.setUsername("testUser");

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getAllByUsernameAndPeriod("testUser", null, null);
        });
    }

    // FOR getTransactionById METHOD

    @Test
    public void testGetTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setId(1);

        User user = new User();

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = transactionService.getTransactionByIdAndUsername(1, user.getUsername());

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void testGetTransactionById_TransactionNotFound() {
        User user = new User();

        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Transaction> result = transactionService.getTransactionByIdAndUsername(1, user.getUsername());

        assertFalse(result.isPresent()); // The result should be empty as there is no transaction with the given ID
    }

    @Test
    public void testGetTransactionById_NullId() {
        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactionByIdAndUsername(null, user.getUsername());
        });
    }

    // FOR deleteTransaction METHOD

    @Test
    public void testDeleteTransaction() {
        User user = new User();
        Transaction transaction = new Transaction();
        transaction.setId(1);
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).deleteById(1);

        Transaction result = transactionService.deleteTransaction(1, user.getUsername());

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    public void testDeleteTransaction_TransactionNotFound() {
        User user = new User();
        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        Transaction result = transactionService.deleteTransaction(1, user.getUsername());

        assertNull(result);
    }

    // FOR createTransaction METHOD

    @Test
    public void testCreateTransaction() {
        User user = new User();
        user.setUsername("testUser");

        Category category = new Category();
        category.setId(1);

        TransactionDto transactionDto = new TransactionDto(500.00, "description", LocalDateTime.now(), 1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        transactionService.createTransaction(transactionDto, "testUser");

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testCreateTransaction_UsernameNotFound() {
        when(userRepository.findByUsername("badUsername")).thenReturn(null);
        TransactionDto transactionDto = new TransactionDto(500.00, "description", LocalDateTime.now(), 1);

        assertThrows(NoSuchElementException.class, () -> {
            transactionService.createTransaction(transactionDto, "badUsername");
        });
    }

    @Test
    public void testCreateTransaction_CategoryIdNotFound() {
        User user = new User();
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        TransactionDto transactionDto = new TransactionDto(500.00, "description", LocalDateTime.now(), 1);

        assertThrows(NoSuchElementException.class, () -> {
            transactionService.createTransaction(transactionDto, "testUser");
        });
    }

    // FOR updateTransaction METHOD

    @Test
    public void testUpdateTransaction() {
        User user = new User();
        user.setUsername("testUser");

        Category category = new Category();
        category.setId(1);

        TransactionDto transactionDto = new TransactionDto(1, 500.00, "description", LocalDateTime.now(), 1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        transactionService.updateTransaction(transactionDto, "testUser");

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction_UsernameNotFound() {
        when(userRepository.findByUsername("badUsername")).thenReturn(null);
        TransactionDto transactionDto = new TransactionDto(500.00, "description", LocalDateTime.now(), 1);

        assertThrows(NullPointerException.class, () -> {
            transactionService.updateTransaction(transactionDto, "badUsername");
        });
    }

    @Test
    public void testUpdateTransaction_CategoryIdNotFound() {
        User user = new User();
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        TransactionDto transactionDto = new TransactionDto(500.00, "description", LocalDateTime.now(), 1);

        assertThrows(NullPointerException.class, () -> {
            transactionService.updateTransaction(transactionDto, "testUser");
        });
    }
}