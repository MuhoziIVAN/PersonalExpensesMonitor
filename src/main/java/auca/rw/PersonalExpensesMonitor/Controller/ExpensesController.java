package auca.rw.PersonalExpensesMonitor.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails; // Import your user service
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;
import auca.rw.PersonalExpensesMonitor.Model.ExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Services.ExpensesCategoryService;
import auca.rw.PersonalExpensesMonitor.Services.ExpensesService;
import auca.rw.PersonalExpensesMonitor.Services.UserService;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    @Autowired
    private ExpensesCategoryService expensesCategoryService;

    @Autowired
    private UserService userService; 
    
    private final String UPLOAD_DIRECTORY = "uploads/";

    // Fetch user expenses
    @GetMapping
    @ResponseBody
    public List<ExpensesTable> getUserExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        UserTable user = userService.findByUsername(userDetails.getUsername());
        return expensesService.getExpensesByUser(user);
    }

    // Create expense
   @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<ExpensesTable> createExpenseWithFile(
        @RequestPart("expense") ExpensesTable expense,
        @RequestPart(value = "file", required = false) MultipartFile file,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            UserTable user = userService.findByUsername(userDetails.getUsername());
            expense.setUser(user); 
            expense.setCreatedAt(LocalDateTime.now());

            // Handle file upload
            if (file != null && !file.isEmpty()) {
                // Create the directory if it doesn't exist
                File directory = new File(UPLOAD_DIRECTORY);
                if (!directory.exists()) {
                    directory.mkdirs(); // Use mkdirs() to create parent directories if needed
                }

                // Generate a unique filename to prevent overwriting
                String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIRECTORY + uniqueFileName);
                
                try {
                    Files.write(filePath, file.getBytes());
                    expense.setFileUrl(filePath.toString());
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
                }
            }

            ExpensesTable createdExpense = expensesService.createExpense(expense);
            return ResponseEntity.ok(createdExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            // Create the directory if it doesn't exist
            File directory = new File(UPLOAD_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a unique filename
            String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIRECTORY + uniqueFileName);
            
            // Write file
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok(filePath.toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed");
        }
    }

    // Get expense by ID
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ExpensesTable> getExpenseById(@PathVariable long id) {
        ExpensesTable expense = expensesService.getExpenseById(id);
        if (expense != null) {
            return ResponseEntity.ok(expense);
        }
        return ResponseEntity.notFound().build();
    }

    // Update expense
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ExpensesTable> updateExpense(
        @PathVariable long id, 
        @RequestBody ExpensesTable expenseDetails
    ) {
        ExpensesTable existingExpense = expensesService.getExpenseById(id);
        if (existingExpense == null) {
            return ResponseEntity.notFound().build();
        }

        existingExpense.setDescription(expenseDetails.getDescription());
        existingExpense.setAmount(expenseDetails.getAmount());
        existingExpense.setCategory(expenseDetails.getCategory());

        ExpensesTable updatedExpense = expensesService.updateExpense(id, existingExpense);
        return ResponseEntity.ok(updatedExpense);
    }

    // Delete expense
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteExpense(@PathVariable long id) {
        ExpensesTable expense = expensesService.getExpenseById(id);
        if (expense == null) {
            return ResponseEntity.notFound().build();
        }
        
        expensesService.deleteExpense(id);
        return ResponseEntity.ok().build();
    }

    // Fetch expense categories
    @GetMapping("/categories")
    @ResponseBody
    public List<ExpensesCategoryTable> getExpenseCategories() {
        return expensesCategoryService.findAll();
    }
}
