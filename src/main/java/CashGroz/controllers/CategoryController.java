package CashGroz.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import CashGroz.dto.CategoryDto;
import CashGroz.models.Category;
import CashGroz.services.CategoryService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ModelAndView getAllCategories() {
        List<Category> categories = categoryService.getAllByUsername();
        ModelAndView modelAndView = new ModelAndView("addCategories");
        modelAndView.addObject("addCategories", categories);
        return modelAndView;
    }
    //For visualization in userCategories page
    @GetMapping("/userCategories")
    public ModelAndView showAllUserCategories() {
        List<Category> categories = categoryService.getAllByUsername();
        ModelAndView modelAndView = new ModelAndView("userCategories");
        modelAndView.addObject("userCategories", categories);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView updateModel(@PathVariable("id") Integer id) {
        Optional<Category> category = categoryService.getCategoryById(id);

        if(category.isPresent()){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("categories");
            modelAndView.addObject("category", category);
            return modelAndView;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping
    public ModelAndView createCategory(@RequestBody CategoryDto category) {
        try {
            categoryService.createCategory(category);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/addCategories");
            return modelAndView;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/edit")
    public ModelAndView updateCategory(@RequestParam("id") Integer id, @RequestParam("name") String name){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        Category result = categoryService.updateCategory(id, categoryDto);
        if(result != null){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/categories/userCategories");
            return modelAndView;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/delete")
    public ModelAndView deleteCategory(@RequestParam("id") Integer id) {
        Category result = categoryService.deleteCategory(id);
        if(result != null){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/categories/userCategories");
            
            return modelAndView;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // @PutMapping("/{id}")
    // public ModelAndView updateCategory(@PathVariable("id") Integer id, @RequestBody CategoryDto category) {
    //     Category result = categoryService.updateCategory(id, category);
    //     if(result != null){
    //         ModelAndView modelAndView = new ModelAndView();
    //         modelAndView.setViewName("categories");
    //         return modelAndView;
    //     }

    //     throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    // }

    // @DeleteMapping("/{id}")
    // public ModelAndView deleteCategory(@PathVariable("id") Integer id){
    //     Category category = categoryService.deleteCategory(id);

    //     if(category==null){
    //         ModelAndView modelAndView = new ModelAndView();
    //         modelAndView.setViewName("categories");
    //         return modelAndView;
    //     }

    //     throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    // }
}
