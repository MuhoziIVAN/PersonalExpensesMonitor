package auca.rw.PersonalExpensesMonitor.Controller;

import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;
import auca.rw.PersonalExpensesMonitor.Model.ExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Services.ExpensesCategoryService;
import auca.rw.PersonalExpensesMonitor.Services.ExpensesService;
import auca.rw.PersonalExpensesMonitor.Services.UserService; // Import your user service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    @Autowired
    private ExpensesCategoryService expensesCategoryService; // Service to fetch expense categories

    @Autowired
    private UserService userService; 
    
    private final String UPLOAD_DIRECTORY = "uploads/";

    // Display all expenses for the logged-in user
    @GetMapping
    public String listExpenses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<ExpensesCategoryTable> categories = expensesCategoryService.findAll();
        model.addAttribute("categories", categories != null ? categories : new ArrayList<>()); // Adding empty list if null
        return "expenses/list"; // Thymeleaf template path
    }


    @GetMapping("/api")
    @ResponseBody
    public List<ExpensesTable> getUserExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        return expensesService.getExpensesByUser(user); // Return as JSON
    }

    // Show form for creating a new expense
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("expense", new ExpensesTable());

    // Fetching expense categories
    List<ExpensesCategoryTable> categories = expensesCategoryService.findAll(); // Fetch categories
    model.addAttribute("categories", categories);

        return "expenses/create"; // Path to your Thymeleaf template
    }

    @PostMapping("/create")
public String createExpense(@ModelAttribute("expense") ExpensesTable expense,
                             @RequestParam(value = "file", required = false) MultipartFile file, 
                             @AuthenticationPrincipal UserDetails userDetails) {
    UserTable user = userService.findByUsername(userDetails.getUsername());
    expense.setUser(user); 
    expense.setCreatedAt(LocalDateTime.now());

    // Save the file to the specified directory if it's not empty
    if (file != null && !file.isEmpty()) {
        try {
            // Create the directory if it doesn't exist
            File directory = new File(UPLOAD_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the file to the specified path
            String filePath = UPLOAD_DIRECTORY + file.getOriginalFilename();
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());
            expense.setFileUrl(filePath); // Ensure ExpensesTable has a field for file URL
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error (e.g., show an error message to the user)
        }
    }

    expensesService.createExpense(expense);
    return "redirect:/user/expenses"; // Redirect to the list of expenses after creation
}


    // Show form for updating an existing expense
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        ExpensesTable expense = expensesService.getExpenseById(id);
        if (expense != null) {
            model.addAttribute("expense", expense);
            List<ExpensesCategoryTable> categories = expensesCategoryService.findAll();
            model.addAttribute("categories", categories);
            return "expenses/update"; // Path to your Thymeleaf template
        }
        return "redirect:/user/expenses"; // Redirect if the expense is not found
    }

    // Handle form submission for updating an existing expense
    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable("id") long id, @ModelAttribute ExpensesTable expenseDetails) {
        ExpensesTable expense = expensesService.getExpenseById(id);
        expense.setDescription(expenseDetails.getDescription());
        expense.setAmount(expenseDetails.getAmount());
        expense.setCategory(expenseDetails.getCategory());
        expensesService.updateExpense(id, expense);
        return "redirect:/user/expenses"; // Redirect to the expenses list
    }

    // Handle deletion of an expense
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") long id) {
        expensesService.deleteExpense(id);
        return "redirect:/user/expenses"; // Redirect to the expenses list
    }
}
