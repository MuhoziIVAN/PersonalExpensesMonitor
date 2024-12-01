package auca.rw.PersonalExpensesMonitor.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.rw.PersonalExpensesMonitor.Model.IncomeTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Repository.IncomeRepository;

@Service
public class IncomeService {
    @Autowired
    private IncomeRepository incomeRepository;

    public List<IncomeTable> getAllIncomes() {
        return incomeRepository.findAll();
    }
    public List<IncomeTable> getIncomesByUser(UserTable user) {
        return incomeRepository.findByUser(user);
    }

    public IncomeTable getIncomeById(long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public IncomeTable createIncome(IncomeTable income) {
        return incomeRepository.save(income);
    }

    public IncomeTable updateIncome(long id, IncomeTable incomeDetails) {
        IncomeTable income = incomeRepository.findById(id).orElse(null);
        if (income != null) {
            income.setSource(incomeDetails.getSource());
            income.setAmount(incomeDetails.getAmount());
            income.setCategory(incomeDetails.getCategory());
            income.setUser(incomeDetails.getUser());
            income.setCreatedAt(incomeDetails.getCreatedAt());
            return incomeRepository.save(income);
        }
        return null;
    }

    public void deleteIncome(long id) {
        incomeRepository.deleteById(id);
    }
}
