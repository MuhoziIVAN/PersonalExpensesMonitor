package auca.rw.PersonalExpensesMonitor.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ExpensesTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne // Establish relationship with UserTable
    @JoinColumn(name = "user_id", nullable = false)
    private UserTable user;

    @ManyToOne // Establish relationship with ExpensesCategoryTable
    @JoinColumn(name = "category_id", nullable = false)
    private ExpensesCategoryTable category;

    private String description;

    private double amount; // Changed to double

    private LocalDateTime createdAt; // Added createdAt

    private String fileUrl;

}
