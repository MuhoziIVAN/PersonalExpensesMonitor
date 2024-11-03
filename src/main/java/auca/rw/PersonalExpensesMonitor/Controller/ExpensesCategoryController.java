package auca.rw.PersonalExpensesMonitor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;
import auca.rw.PersonalExpensesMonitor.Services.ExpensesCategoryService;

import java.util.List;

@Controller
@RequestMapping("/user/expenses-categories")
public class ExpensesCategoryController {

    @Autowired
    private ExpensesCategoryService service;

    @GetMapping
    public String listCategories(Model model) {
        List<ExpensesCategoryTable> categories = service.findAll();
        model.addAttribute("categories", categories);
        return "expenses-categories/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new ExpensesCategoryTable());
        return "expenses-categories/form";
    }

    @PostMapping
    public String createCategory(@ModelAttribute ExpensesCategoryTable category) {
        service.save(category);
        return "redirect:/user/expenses-categories";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ExpensesCategoryTable category = service.findById(id);
        model.addAttribute("category", category);
        return "expenses-categories/form";
    }

    @PostMapping("/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute ExpensesCategoryTable category) {
        category.setId(id);
        service.save(category);
        return "redirect:/user/expenses-categories";
    }

    @GetMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/user/expenses-categories";
    }
}
