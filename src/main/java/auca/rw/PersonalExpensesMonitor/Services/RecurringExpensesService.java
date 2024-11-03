package auca.rw.PersonalExpensesMonitor.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.rw.PersonalExpensesMonitor.Model.RecurringExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Repository.RecurringExpensesRepository;

@Service
public class RecurringExpensesService {
    @Autowired
    private RecurringExpensesRepository recurringExpensesRepository;

    public List<RecurringExpensesTable> getRecurringExpensesByUser(UserTable user) {
        return recurringExpensesRepository.findByUser(user);
    }

    public RecurringExpensesTable getRecurringExpenseById(long id) {
        return recurringExpensesRepository.findById(id).orElse(null);
    }

    public RecurringExpensesTable createRecurringExpense(RecurringExpensesTable recurringExpense) {
        return recurringExpensesRepository.save(recurringExpense);
    }

    public RecurringExpensesTable updateRecurringExpense(long id, RecurringExpensesTable expenseDetails) {
        RecurringExpensesTable recurringExpense = recurringExpensesRepository.findById(id).orElse(null);
        if (recurringExpense != null) {
            recurringExpense.setDescription(expenseDetails.getDescription());
            recurringExpense.setAmount(expenseDetails.getAmount());
            recurringExpense.setRecurrenceType(expenseDetails.getRecurrenceType());
            recurringExpense.setStartDate(expenseDetails.getStartDate());
            recurringExpense.setEndDate(expenseDetails.getEndDate());
            recurringExpense.setNextDueDate(expenseDetails.getNextDueDate());
            return recurringExpensesRepository.save(recurringExpense);
        }
        return null;
    }

    public void deleteRecurringExpense(long id) {
        recurringExpensesRepository.deleteById(id);
    }
}
