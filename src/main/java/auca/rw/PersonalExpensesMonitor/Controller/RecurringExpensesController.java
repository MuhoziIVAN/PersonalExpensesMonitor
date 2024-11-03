package auca.rw.PersonalExpensesMonitor.Controller;

import auca.rw.PersonalExpensesMonitor.Model.RecurringExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Services.RecurringExpensesService;
import auca.rw.PersonalExpensesMonitor.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user/recurring-expenses")
public class RecurringExpensesController {

    @Autowired
    private RecurringExpensesService recurringExpensesService;

    @Autowired
    private UserService userService;

    // Display all recurring expenses for the logged-in user
    @GetMapping
    public String listRecurringExpenses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        List<RecurringExpensesTable> recurringExpenses = recurringExpensesService.getRecurringExpensesByUser(user);
        model.addAttribute("recurringExpenses", recurringExpenses);
        return "recurring-expenses/list"; // Thymeleaf template path
    }

    @GetMapping("/api")
    @ResponseBody
    public List<RecurringExpensesTable> getUserRecurringExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        return recurringExpensesService.getRecurringExpensesByUser(user); // Return as JSON
    }

    // Show form for creating a new recurring expense
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("recurringExpense", new RecurringExpensesTable());
        return "recurring-expenses/create"; // Path to your Thymeleaf template
    }

    @PostMapping("/create")
    public String createRecurringExpense(@ModelAttribute("recurringExpense") RecurringExpensesTable recurringExpense,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        recurringExpense.setUser(user);
        recurringExpense.setCreatedAt(LocalDateTime.now());
        recurringExpensesService.createRecurringExpense(recurringExpense);
        return "redirect:/user/recurring-expenses"; 
    }

    // Show form for updating an existing recurring expense
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        RecurringExpensesTable recurringExpense = recurringExpensesService.getRecurringExpenseById(id);
        if (recurringExpense != null) {
            model.addAttribute("recurringExpense", recurringExpense); // Add recurringExpense to the model
            return "recurring-expenses/update"; // Path to your Thymeleaf template
        }
        return "redirect:/user/recurring-expenses"; // Redirect if the expense is not found
    }

    // Handle form submission for updating an existing recurring expense
    @PostMapping("/update/{id}")
    public String updateRecurringExpense(@PathVariable("id") long id, 
                                         @ModelAttribute RecurringExpensesTable recurringExpenseDetails) {
        recurringExpensesService.updateRecurringExpense(id, recurringExpenseDetails); // Use updated details directly
        return "redirect:/user/recurring-expenses"; 
    }

    // Handle deletion of a recurring expense
    @GetMapping("/delete/{id}")
    public String deleteRecurringExpense(@PathVariable("id") long id) {
        recurringExpensesService.deleteRecurringExpense(id);
        return "redirect:/user/recurring-expenses"; 
    }
}
