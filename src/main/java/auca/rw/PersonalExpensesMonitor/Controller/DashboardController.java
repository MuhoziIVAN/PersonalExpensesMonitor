package auca.rw.PersonalExpensesMonitor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;
import auca.rw.PersonalExpensesMonitor.Model.ExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.RecurringExpensesTable;
import auca.rw.PersonalExpensesMonitor.Services.IncomeService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private IncomeService incomeService;

    // Existing dashboard view
    @GetMapping
    public String showDashboard(Model model) {
        // Fetch data and add it to the model
        // ...
        return "redirect:/menus"; // dashboard.html
    }

    // Route to add expense
    @GetMapping("/add-expense")
    public String addExpenseForm(Model model) {
        model.addAttribute("expense", new ExpensesTable()); // Empty Expense object
        return "Expenses"; // add-expense.html
    }

    // Route to add recurring expense
    @GetMapping("/add-recurring-expense")
    public String addRecurringExpenseForm(Model model) {
        model.addAttribute("recurringExpense", new RecurringExpensesTable());
        return "RecurringExpenses"; // add-recurring-expense.html
    }

    // Route to add expense category
    @GetMapping("/add-expense-category")
    public String addExpenseCategoryForm(Model model) {
        model.addAttribute("expenseCategory", new ExpensesCategoryTable());
        return "ExpensesCategoty"; // add-expense-category.html
    }
}






