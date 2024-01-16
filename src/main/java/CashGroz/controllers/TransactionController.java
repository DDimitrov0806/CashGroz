package CashGroz.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import CashGroz.dto.TransactionDto;
import CashGroz.models.Category;
import CashGroz.models.Transaction;
import CashGroz.services.CategoryService;
import CashGroz.services.TransactionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryService categoryService;
    private final long TIME_RANGE = -30;

    @GetMapping
    public String getTransactionsPage(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (toDate == null) {
            toDate = LocalDate.now().plusDays(2);
        }
        if (fromDate == null) {
            fromDate = toDate.plusDays(TIME_RANGE);
        }

        if (fromDate.compareTo(toDate) >= 0) {
            var swapDateTime = fromDate;
            fromDate = toDate;
            toDate = swapDateTime;
        }

        List<Transaction> transactions = transactionService.getAllByUsernameAndPeriod(userDetails.getUsername(),
                fromDate, toDate);
        model.addAttribute("transactions", transactions);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return "transactions/transactions";
    }

    @GetMapping("/create")
    public String getCreateTransactionPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Category> categories = categoryService.getAllByUsername(userDetails.getUsername());
        model.addAttribute("categories", categories);
        model.addAttribute("transaction", new Transaction());
        return "transactions/create-transaction";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute TransactionDto transactionDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();

            transactionService.createTransaction(transactionDto, username);
            return "redirect:/transactions";
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/edit/{id}")
    public String getEditTransactionPage(Model model, @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Transaction> transaction = transactionService.getTransactionByIdAndUsername(id,
                userDetails.getUsername());

        if (transaction.isPresent()) {
            List<Category> categories = categoryService.getAllByUsername(userDetails.getUsername());
            model.addAttribute("categories", categories);
            TransactionDto transactionDto = new TransactionDto(transaction.get());
            model.addAttribute("transaction", transactionDto);
            return "transactions/edit-transaction";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/edit")
    public String editTransaction(@ModelAttribute TransactionDto transactionDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            transactionService.updateTransaction(transactionDto, userDetails.getUsername());

            return "redirect:/transactions";
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Transaction result = transactionService.getTransactionByIdAndUsername(id, userDetails.getUsername())
                .orElseThrow();
        if (result != null) {
            return "redirect:/transactions";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
