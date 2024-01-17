package CashGroz.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import CashGroz.dto.TransactionDto;
import CashGroz.models.Category;
import CashGroz.models.Transaction;
import CashGroz.models.User;
import CashGroz.repositories.CategoryRepository;
import CashGroz.repositories.TransactionRepository;
import CashGroz.repositories.UserRepository;

@Service
public class TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Transaction> getAllByUsernameAndPeriod(String username, LocalDate fromDate, LocalDate toDate)
            throws IllegalArgumentException {
        if (username == null || fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Username, fromDate and toDate cannot be null");
        }
        User user = userRepository.findByUsername(username);
        List<Transaction> transactions = user.getTransactions()
                .stream()
                .filter(p -> fromDate.isBefore(p.getDateTime().toLocalDate())
                        && p.getDateTime().toLocalDate().isBefore(toDate))
                .toList();

        return transactions;
    }

    public Optional<Transaction> getTransactionByIdAndUsername(@NonNull Integer id, String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Optional<Transaction> transaction = transactionRepository.findByIdAndUserId(id, user.getId());

        return transaction;
    }

    public Transaction deleteTransaction(@NonNull Integer id, String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<Transaction> transaction = transactionRepository.findByIdAndUserId(id, user.getId());

        if (transaction.isPresent()) {
            transactionRepository.deleteById(id);
            return transaction.get();
        }

        return null;
    }

    public void createTransaction(TransactionDto transactionDto, String username) throws NoSuchElementException, UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Category category = categoryRepository.findByIdAndUserId(transactionDto.getCategoryId(), user.getId())
                .orElseThrow();
        Transaction transaction = new Transaction(transactionDto.getAmount(), transactionDto.getDescription(),
                transactionDto.getDateTime(), category, user);
        transactionRepository.save(transaction);
    }

    public void updateTransaction(TransactionDto transactionDto, String username) throws NoSuchElementException, UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Category category = categoryRepository.findByIdAndUserId(transactionDto.getCategoryId(), user.getId())
                .orElseThrow();
        Transaction transaction = new Transaction(transactionDto.getId(), transactionDto.getAmount(),
                transactionDto.getDescription(), transactionDto.getDateTime(), category, user);
        transactionRepository.save(transaction);
    }
}
