package auca.rw.PersonalExpensesMonitor.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auca.rw.PersonalExpensesMonitor.Services.IncomeService;

@RestController // Change to @RestController for JSON responses
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for React frontend
public class DashboardController {

    @Autowired
    private IncomeService incomeService;

    // Endpoint to fetch dashboard data
    @GetMapping
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        try {
            // Fetch income data
            List<?> incomeData = incomeService.getAllIncomes();
            data.put("income", incomeData);

            // Placeholder for expenses and categories
            data.put("expenses", List.of()); // Replace with actual data
            data.put("categories", List.of()); // Replace with actual data
        } catch (Exception e) {
            System.err.println("Error fetching dashboard data: " + e.getMessage());
            e.printStackTrace();
            data.put("error", "Failed to fetch dashboard data.");
        }
        return data; // Return JSON response
    }

    // Endpoint to get form data for adding expense
    @GetMapping("/add-expense")
    public Map<String, Object> getAddExpenseForm() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("expense", new HashMap<>()); // Replace with actual expense data structure
        } catch (Exception e) {
            System.err.println("Error fetching add-expense form: " + e.getMessage());
            response.put("error", "Failed to fetch add-expense form.");
        }
        return response; // Return JSON response
    }

    // Endpoint to get form data for adding recurring expense
    @GetMapping("/add-recurring-expense")
    public Map<String, Object> getAddRecurringExpenseForm() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("recurringExpense", new HashMap<>()); // Replace with actual recurring expense data
        } catch (Exception e) {
            System.err.println("Error fetching add-recurring-expense form: " + e.getMessage());
            response.put("error", "Failed to fetch add-recurring-expense form.");
        }
        return response; // Return JSON response
    }

    // Endpoint to get form data for adding expense category
    @GetMapping("/add-expense-category")
    public Map<String, Object> getAddExpenseCategoryForm() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("expenseCategory", new HashMap<>()); // Replace with actual category data
        } catch (Exception e) {
            System.err.println("Error fetching add-expense-category form: " + e.getMessage());
            response.put("error", "Failed to fetch add-expense-category form.");
        }
        return response; // Return JSON response
    }

@GetMapping("/profile")
public ResponseEntity<Map<String, String>> getUserProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName(); // Get the logged-in user's username
    return ResponseEntity.ok(Map.of("username", username));
}
}
