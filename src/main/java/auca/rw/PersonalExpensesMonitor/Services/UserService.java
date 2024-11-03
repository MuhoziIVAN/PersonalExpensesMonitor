package auca.rw.PersonalExpensesMonitor.Services;

import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public UserTable findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public UserTable registerUser(UserTable user) {
        Optional<UserTable> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(user);
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isPhoneExists(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean authenticate(String username, String password) {
        Optional<UserTable> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return passwordEncoder.matches(password, user.get().getPassword());
        }
        return false;
    }

    public void initiatePasswordReset(String email) {
        Optional<UserTable> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("User with email not found");
        }

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        emailService.sendEmail(email, resetToken);
        UserTable userToUpdate = user.get();
        userToUpdate.setResetToken(resetToken);
        userRepository.save(userToUpdate);

    }

    public void resetPassword(String token, String newPassword) {
        Optional<UserTable> user = userRepository.findByResetToken(token);
        if (user.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        UserTable userToUpdate = user.get();
        userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        userToUpdate.setResetToken(null);
        userRepository.save(userToUpdate);
    }
}
