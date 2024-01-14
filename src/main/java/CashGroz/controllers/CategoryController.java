package CashGroz.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import CashGroz.dto.CategoryDto;
import CashGroz.models.Category;
import CashGroz.services.CategoryService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/create")
    public String getCreateCategoryPage(Model model) {
        model.addAttribute("category", new CategoryDto());
        return "categories/create-category";
    }

    @GetMapping
    public String getCategoriesPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        List<Category> categories = categoryService.getAllByUsername(username);
        model.addAttribute("categories", categories);
        return "categories/categories";
    }

    @GetMapping("/edit/{id}")
    public String getEditCategoryPage(Model model, @PathVariable Integer id) {
        Optional<Category> category = categoryService.getCategoryById(id);

        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categories/edit-category";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute CategoryDto category,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();

            categoryService.createCategory(category, username);
            return "redirect:/categories";
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category, @AuthenticationPrincipal UserDetails userDetails) {
        categoryService.updateCategory(category, userDetails.getUsername());

        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        Category result = categoryService.deleteCategory(id);
        if (result != null) {
            return "redirect:/categories";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
