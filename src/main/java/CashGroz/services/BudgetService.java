package CashGroz.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Budget> getAllBudgetsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        List<Budget> budgets = user.getBudgets();

        return budgets;
    }

    public Optional<Budget> getBudgetById(Integer id) {
        Optional<Budget> budget = budgetRepository.findById(id);

        return budget;
    }

    public void createBudget(BudgetDto budgetDto, String username) throws NoSuchElementException, Exception {
        validateBudgetDto(budgetDto);

        User user = userRepository.findByUsername(username);
        Category category = categoryRepository.findById(budgetDto.getCategoryId()).orElseThrow();
        Budget budget = new Budget(budgetDto.getAmount(), user, budgetDto.getDateTimeFrom(), budgetDto.getDateTimeTo(),
                category);
        budgetRepository.save(budget);
    }

    public void updateBudget(BudgetDto budgetDto, String username) throws NoSuchElementException, Exception{
        validateBudgetDto(budgetDto);

        User user = userRepository.findByUsername(username);

        Category category = categoryRepository.findById(budgetDto.getCategoryId()).orElseThrow();

        Budget budget = new Budget(budgetDto.getAmount(), budgetDto.getId(), user, budgetDto.getDateTimeFrom(),
                budgetDto.getDateTimeTo(), category);

        budgetRepository.save(budget);
    }

    public Budget deleteBudget(Integer budgetId) {
        Optional<Budget> budget = budgetRepository.findById(budgetId);
        if (budget.isPresent()) {
            budgetRepository.deleteById(budgetId);
            return budget.get();
        }
        return null;
    }

    private void validateBudgetDto(BudgetDto budgetDto) throws Exception{
        if(budgetDto.getDateTimeFrom() == null || budgetDto.getDateTimeTo() == null) {
            throw new Exception("Date Time From and Date Time To must have a value");
        }

        if(budgetDto.getDateTimeFrom().compareTo(budgetDto.getDateTimeTo()) >= 0) {
            var swapDateTime = budgetDto.getDateTimeFrom();
            budgetDto.setDateTimeFrom(budgetDto.getDateTimeTo());
            budgetDto.setDateTimeTo(swapDateTime);
        }
    }
}