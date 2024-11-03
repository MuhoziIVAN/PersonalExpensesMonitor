package auca.rw.PersonalExpensesMonitor.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity 
public class IncomeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String source;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserTable user;

    @ManyToOne 
    @JoinColumn(name = "category_id", nullable = false)
    private ExpensesCategoryTable category;

    private double amount;

    private LocalDateTime createdAt; 
}
