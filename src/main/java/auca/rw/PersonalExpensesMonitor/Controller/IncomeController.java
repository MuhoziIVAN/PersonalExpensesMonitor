package auca.rw.PersonalExpensesMonitor.Controller;

import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;
import auca.rw.PersonalExpensesMonitor.Model.IncomeTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Services.ExpensesCategoryService;
import auca.rw.PersonalExpensesMonitor.Services.IncomeService;
import auca.rw.PersonalExpensesMonitor.Services.UserService; // Import your user service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpensesCategoryService expensesCategoryService;

    @Autowired
    private UserService userService;

    // Display all expenses for the logged-in user
    @GetMapping
    public String listIncome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<ExpensesCategoryTable> categories = expensesCategoryService.findAll();
        model.addAttribute("categories", categories != null ? categories : new ArrayList<>()); // Adding empty list if null
        return "income/list"; // Thymeleaf template path
    }


    @GetMapping("/api")
    @ResponseBody
    public List<IncomeTable> getUserIncome(@AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        return incomeService.getIncomesByUser(user); // Return as JSON
    }

    // Show form for creating a new expense
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("income", new IncomeTable());

        // Fetching expense categories
        List<ExpensesCategoryTable> categories = expensesCategoryService.findAll(); // Fetch categories
        model.addAttribute("categories", categories);

        return "income/create"; // Path to your Thymeleaf template
    }

    @PostMapping("/create")
    public String createExpense(@ModelAttribute("income") IncomeTable income,
                                @AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        income.setUser(user); 
        income.setCreatedAt(LocalDateTime.now());

        incomeService.createIncome(income);
        return "redirect:/user/income"; 
    }


    // Show form for updating an existing income
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        IncomeTable income = incomeService.getIncomeById(id);
        if (income != null) {
            model.addAttribute("income", income);
            List<ExpensesCategoryTable> categories = expensesCategoryService.findAll();
            model.addAttribute("categories", categories);
            return "income/update"; // Path to your Thymeleaf template
        }
        return "redirect:/user/income"; // Redirect if the expense is not found
    }

    // Handle form submission for updating an existing expense
    @PostMapping("/update/{id}")
    public String updateIncome(@PathVariable("id") long id, @ModelAttribute IncomeTable incomeDetails) {
        IncomeTable income = incomeService.getIncomeById(id);
        income.setSource(incomeDetails.getSource());
        income.setAmount(incomeDetails.getAmount());
        income.setCategory(incomeDetails.getCategory());
        incomeService.updateIncome(id, income);
        return "redirect:/user/income"; 
    }

    // Handle deletion of an expense
    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable("id") long id) {
        incomeService.deleteIncome(id);
        return "redirect:/user/income"; 
    }
}
