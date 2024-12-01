package auca.rw.PersonalExpensesMonitor.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import auca.rw.PersonalExpensesMonitor.Services.ExpensesCategoryService;

@RestController
@RequestMapping("/api/expenses-categories")  // Changed to /api endpoint
@CrossOrigin(origins = "http://localhost:3000")
public class ExpensesCategoryController {

    @Autowired
    private ExpensesCategoryService service;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ExpensesCategoryTable>> listCategories() {
        List<ExpensesCategoryTable> categories = service.findAll();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ExpensesCategoryTable> createCategory(@RequestBody ExpensesCategoryTable category) {
        ExpensesCategoryTable savedCategory = service.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ExpensesCategoryTable> updateCategory(
        @PathVariable Long id, 
        @RequestBody ExpensesCategoryTable category) {
        category.setId(id);
        ExpensesCategoryTable updatedCategory = service.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
