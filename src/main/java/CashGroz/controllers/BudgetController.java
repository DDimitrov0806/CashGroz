package CashGroz.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import CashGroz.dto.BudgetDto;
import CashGroz.models.Budget;
import CashGroz.models.Category;
import CashGroz.models.Transaction;
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
            Double amountSpent = budgetService.getAmountSpentByUsernameAndPeriod(userDetails.getUsername(), budget.getDateTimeFrom().toLocalDate(), budget.getDateTimeTo().toLocalDate(), budget.getCategory());
            amountLeft.add(budget.getAmount() - amountSpent);
            budget.getCategory().getColor();
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/edit/{id}")
    public String getEditBudgetPage(Model model, @PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Inside getEditBudgetPage method");
        Optional<Budget> budget = budgetService.getBudgetById(id);
        
        if(budget.isPresent()) {
            List<Category> categories = categoryService.getAllByUsername(userDetails.getUsername());
            model.addAttribute("categories", categories);
            BudgetDto budgetDto = new BudgetDto(budget.get().getId(), budget.get().getAmount(), budget.get().getDateTimeFrom(), budget.get().getDateTimeTo(), budget.get().getCategory().getId());
            model.addAttribute("budget", budgetDto);
            System.out.println("Returning budgets/edit-budget");
            return "budgets/edit-budget";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/edit")
    public String editBudget(@ModelAttribute BudgetDto budgetDto,
                            @AuthenticationPrincipal UserDetails userDetails) {
        budgetService.updateBudget(budgetDto, userDetails.getUsername());
        return "redirect:/budgets";
    }

    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Integer id,
                            @AuthenticationPrincipal UserDetails userDetails) {
        Budget result = budgetService.deleteBudget(id);
        if (result != null) {
            return "redirect:/budgets";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}