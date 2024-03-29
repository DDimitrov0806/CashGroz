package CashGroz.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import CashGroz.dto.BudgetDto;
import CashGroz.models.Budget;
import CashGroz.models.Category;
import CashGroz.services.BudgetService;
import CashGroz.services.CategoryService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/budgets")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getBudgetsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Budget> budgets = budgetService.getAllBudgetsByUsername(userDetails.getUsername());
        List<Double> amountLeft = new ArrayList<>();
        for (Budget budget : budgets) {
            Double amountSpent = budgetService.getAmountSpentByUsernameAndPeriod(userDetails.getUsername(), budget);
            amountLeft.add(budget.getAmount() - amountSpent);
        }

        model.addAttribute("budgets", budgets);
        model.addAttribute("amountLeft", amountLeft);
        return "budgets/budgets";
    }

    @GetMapping("/create")
    public String getCreateBudgetPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Category> categories = categoryService.getAllByUsername(userDetails.getUsername());
        model.addAttribute("categories", categories);
        model.addAttribute("budget", new BudgetDto());
        return "budgets/create-budget";
    }

    @PostMapping("/create")
    public String createBudget(@ModelAttribute BudgetDto budgetDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            budgetService.createBudget(budgetDto, username);
            return "redirect:/budgets";
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/edit/{id}")
    public String getEditBudgetPage(Model model, @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Budget> budget = budgetService.getBudgetByIdAndUsername(id, userDetails.getUsername());

        if (budget.isPresent()) {
            try {
                List<Category> categories = categoryService.getAllByUsername(userDetails.getUsername());
                model.addAttribute("categories", categories);
                BudgetDto budgetDto = new BudgetDto(budget.get());
                model.addAttribute("budget", budgetDto);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }

            return "budgets/edit-budget";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/edit")
    public String editBudget(@ModelAttribute BudgetDto budgetDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            budgetService.updateBudget(budgetDto, userDetails.getUsername());
            return "redirect:/budgets";
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Budget result = budgetService.deleteBudget(id, userDetails.getUsername());
        if (result != null) {
            return "redirect:/budgets";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}