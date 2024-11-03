package auca.rw.PersonalExpensesMonitor.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import auca.rw.PersonalExpensesMonitor.Model.ExpensesCategoryTable;

@Repository
public interface ExpensesCategoryRepository extends JpaRepository<ExpensesCategoryTable, Long> {
}
