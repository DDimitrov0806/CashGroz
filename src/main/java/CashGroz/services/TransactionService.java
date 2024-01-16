package CashGroz.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import CashGroz.dto.TransactionDto;
import CashGroz.models.Budget;
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

    public List<Transaction> getAllByUsernameAndPeriod(String username, LocalDate fromDate, LocalDate toDate) {
        User user = userRepository.findByUsername(username);
        List<Transaction> transactions = user.getTransactions()
                .stream()
                .filter(p -> fromDate.isBefore(p.getDateTime().toLocalDate())
                        && p.getDateTime().toLocalDate().isBefore(toDate))
                .toList();

        return transactions;
    }

    public Double getAmountSpentByUsernameAndPeriod(String username, Budget budget) {
        List<Transaction> transactions = getAllByUsernameAndPeriod(username, budget.getDateTimeFrom(), budget.getDateTimeTo());
        Double amountSpent = transactions.stream()
            .filter(transaction -> transaction.getCategory().equals(budget.getCategory()))
            .mapToDouble(Transaction::getAmount)
            .sum();
        return amountSpent;
    }

    public Optional<Transaction> getTransactionById(Integer id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        return transaction;
    }

    public Transaction deleteTransaction(@NonNull Integer id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        if (transaction.isPresent()) {
            transactionRepository.deleteById(id);
            return transaction.get();
        }

        return null;
    }

    public void createTransaction(TransactionDto transactionDto, String username) {
        User user = userRepository.findByUsername(username);
        Category category = categoryRepository.findById(transactionDto.getCategoryId()).get();
        Transaction transaction = new Transaction(transactionDto.getAmount(), transactionDto.getDescription(),
                transactionDto.getDateTime(), category, user);
        transactionRepository.save(transaction);
    }

    public void updateTransaction(TransactionDto transactionDto, String username) {
        User user = userRepository.findByUsername(username);
        Category category = categoryRepository.findById(transactionDto.getCategoryId()).get();
        Transaction transaction = new Transaction(transactionDto.getId(), transactionDto.getAmount(),
                transactionDto.getDescription(), transactionDto.getDateTime(), category, user);
        transactionRepository.save(transaction);
    }
}
