package CashGroz.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import CashGroz.dto.BudgetDto;
import CashGroz.models.Budget;
import CashGroz.models.Category;
import CashGroz.models.Transaction;
import CashGroz.models.User;
import CashGroz.repositories.BudgetRepository;
import CashGroz.repositories.CategoryRepository;
import CashGroz.repositories.UserRepository;

@Service
public class BudgetService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionService transactionService;

    public Double getAmountSpentByUsernameAndPeriod(String username, Budget budget) {
        List<Transaction> transactions = transactionService.getAllByUsernameAndPeriod(username,
                budget.getDateTimeFrom(), budget.getDateTimeTo());
        Double amountSpent = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getCategory() == budget.getCategory()) {
                amountSpent = amountSpent + transaction.getAmount();
            }
        }
        return amountSpent;
    }

    public List<Budget> getAllBudgetsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<Budget> budgets = user.getBudgets();

        if (budgets == null) {
            return new ArrayList<>();
        }

        return budgets;
    }

    public Optional<Budget> getBudgetByIdAndUsername(Integer id, String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Optional<Budget> budget = budgetRepository.findByIdAndUserId(id, user.getId());

        return budget;
    }

    public void createBudget(BudgetDto budgetDto, String username) throws NoSuchElementException, Exception {
        validateBudgetDto(budgetDto);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Category category = categoryRepository.findByIdAndUserId(budgetDto.getCategoryId(), user.getId()).orElseThrow();
        Budget budget = new Budget(budgetDto.getAmount(), user, budgetDto.getDateTimeFrom(), budgetDto.getDateTimeTo(),
                category);
        budgetRepository.save(budget);
    }

    public void updateBudget(BudgetDto budgetDto, String username) throws NoSuchElementException, Exception {
        validateBudgetDto(budgetDto);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Category category = categoryRepository.findByIdAndUserId(budgetDto.getCategoryId(), user.getId()).orElseThrow();

        Budget budget = new Budget(budgetDto.getAmount(), budgetDto.getId(), user, budgetDto.getDateTimeFrom(),
                budgetDto.getDateTimeTo(), category);

        budgetRepository.save(budget);
    }

    public Budget deleteBudget(Integer budgetId, String username)
            throws NoSuchElementException, UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<Budget> budget = budgetRepository.findByIdAndUserId(budgetId, user.getId());
        if (!budget.isPresent()) {
            throw new NoSuchElementException("Budget not found");
        }
        budgetRepository.deleteById(budgetId);
        return budget.get();
    }

    private void validateBudgetDto(BudgetDto budgetDto) throws NoSuchElementException {
        if (budgetDto.getDateTimeFrom() == null || budgetDto.getDateTimeTo() == null) {
            throw new NoSuchElementException("Date Time From and Date Time To must have a value");
        }

        if (budgetDto.getDateTimeFrom().compareTo(budgetDto.getDateTimeTo()) >= 0) {
            var swapDateTime = budgetDto.getDateTimeFrom();
            budgetDto.setDateTimeFrom(budgetDto.getDateTimeTo());
            budgetDto.setDateTimeTo(swapDateTime);
        }
    }
}