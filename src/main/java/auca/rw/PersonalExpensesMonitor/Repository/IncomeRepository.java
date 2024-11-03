package auca.rw.PersonalExpensesMonitor.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import auca.rw.PersonalExpensesMonitor.Model.IncomeTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeTable, Long> {
    List<IncomeTable> findByUser(UserTable user);
}