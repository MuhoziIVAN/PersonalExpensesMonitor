package auca.rw.PersonalExpensesMonitor.Repository;

import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Long> {
    
    Optional<UserTable> findByUsername(String username);
    Optional<UserTable> findByEmail(String email);
    Optional<UserTable> findByResetToken(String resetToken);
    Optional<UserTable> findByPhone(String phone);
    
}