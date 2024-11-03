package auca.rw.PersonalExpensesMonitor.Services;

import auca.rw.PersonalExpensesMonitor.Model.ExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;

    public List<ExpensesTable> getExpensesByUser(UserTable user) {
        return expensesRepository.findByUser(user);
    }

    public ExpensesTable getExpenseById(long id) {
        return expensesRepository.findById(id).orElse(null);
    }

    public ExpensesTable createExpense(ExpensesTable expense) {
        return expensesRepository.save(expense);
    }

    public ExpensesTable updateExpense(long id, ExpensesTable expenseDetails) {
        ExpensesTable expense = expensesRepository.findById(id).orElse(null);
        if (expense != null) {
            expense.setDescription(expenseDetails.getDescription());
            expense.setAmount(expenseDetails.getAmount());
            expense.setCategory(expenseDetails.getCategory());
            expense.setUser(expenseDetails.getUser());
            expense.setCreatedAt(expenseDetails.getCreatedAt());
            return expensesRepository.save(expense);
        }
        return null;
    }

    public void deleteExpense(long id) {
        expensesRepository.deleteById(id);
    }
}
