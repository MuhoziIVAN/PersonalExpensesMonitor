package auca.rw.PersonalExpensesMonitor.Model;

import java.sql.Date;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class ExpensesCategoryTable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private int categoryId; // Changed to follow Java naming conventions
    private String categoryName; // Changed to String and follow Java naming conventions
    private String description;
    private Date createdAt; // Changed to follow Java naming conventions

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
