package auca.rw.PersonalExpensesMonitor.Repository;

import auca.rw.PersonalExpensesMonitor.Model.ExpensesTable;
import auca.rw.PersonalExpensesMonitor.Model.UserTable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends JpaRepository<ExpensesTable, Long> {
    List<ExpensesTable> findByUser(UserTable user);
}
