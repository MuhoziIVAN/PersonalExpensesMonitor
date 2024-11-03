package auca.rw.PersonalExpensesMonitor.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import auca.rw.PersonalExpensesMonitor.Model.RecurringExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;

@Repository
public interface RecurringExpensesRepository extends JpaRepository<RecurringExpensesTable, Long> {
    List<RecurringExpensesTable> findByUser(UserTable user);
}
