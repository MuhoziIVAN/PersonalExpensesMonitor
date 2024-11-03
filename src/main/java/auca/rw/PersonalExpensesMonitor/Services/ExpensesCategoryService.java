package auca.rw.PersonalExpensesMonitor.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;
import auca.rw.PersonalExpensesMonitor.Repository.ExpensesCategoryRepository;

@Service
public class ExpensesCategoryService {

    @Autowired
    private ExpensesCategoryRepository repository;

    public List<ExpensesCategoryTable> findAll() {
        return repository.findAll();
    }

    public ExpensesCategoryTable findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public ExpensesCategoryTable save(ExpensesCategoryTable category) {
        return repository.save(category);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
