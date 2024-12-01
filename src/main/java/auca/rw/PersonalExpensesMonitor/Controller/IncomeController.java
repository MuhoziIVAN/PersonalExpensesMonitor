package auca.rw.PersonalExpensesMonitor.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin; // Import your user service
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;
import auca.rw.PersonalExpensesMonitor.Model.IncomeTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Services.ExpensesCategoryService;
import auca.rw.PersonalExpensesMonitor.Services.IncomeService;
import auca.rw.PersonalExpensesMonitor.Services.UserService;

@RestController
@RequestMapping("/api/income")
@CrossOrigin(origins = "http://localhost:3000")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpensesCategoryService expensesCategoryService;

    @Autowired
    private UserService userService;

     // Get all income for the logged-in user
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<IncomeTable>> getUserIncome(@AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        List<IncomeTable> incomes = incomeService.getIncomesByUser(user);
        return ResponseEntity.ok(incomes);
    }

    // Get all expense categories (for dropdown or selection)
    @GetMapping("/categories")
    @ResponseBody
    public ResponseEntity<List<ExpensesCategoryTable>> getCategories() {
        List<ExpensesCategoryTable> categories = expensesCategoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    // Create a new income entry
    @PostMapping
    @ResponseBody
    public ResponseEntity<IncomeTable> createIncome(
        @RequestBody IncomeTable income, 
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        income.setUser(user); 
        income.setCreatedAt(LocalDateTime.now());
        

        IncomeTable createdIncome = incomeService.createIncome(income);
        return ResponseEntity.ok(createdIncome);
    }

    // Get a specific income entry by ID
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<IncomeTable> getIncomeById(@PathVariable long id) {
        IncomeTable income = incomeService.getIncomeById(id);
        if (income != null) {
            return ResponseEntity.ok(income);
        }
        return ResponseEntity.notFound().build();
    }

    // Update an existing income entry
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<IncomeTable> updateIncome(
        @PathVariable long id, 
        @RequestBody IncomeTable incomeDetails
    ) {
        IncomeTable existingIncome = incomeService.getIncomeById(id);
        if (existingIncome == null) {
            return ResponseEntity.notFound().build();
        }

        existingIncome.setSource(incomeDetails.getSource());
        existingIncome.setAmount(incomeDetails.getAmount());
        existingIncome.setCategory(incomeDetails.getCategory());

        IncomeTable updatedIncome = incomeService.updateIncome(id, existingIncome);
        return ResponseEntity.ok(updatedIncome);
    }

    // Delete an income entry
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteIncome(@PathVariable long id) {
        IncomeTable income = incomeService.getIncomeById(id);
        if (income == null) {
            return ResponseEntity.notFound().build();
        }
        
        incomeService.deleteIncome(id);
        return ResponseEntity.ok().build();
    }
}
