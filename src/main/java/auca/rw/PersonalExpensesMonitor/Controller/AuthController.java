package auca.rw.PersonalExpensesMonitor.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Services.CustomUserDetailsService;
import auca.rw.PersonalExpensesMonitor.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/signup")
    public String showSignupForm(HttpServletRequest request, Model model) {
        // Invalidate the session to clear existing user authentication
        request.getSession().invalidate();
        model.addAttribute("user", new UserTable());
        return "signup";
    }

    @GetMapping("/403")
    public String unautharized() {
        return "403";
    }

    // Handle signup submission
   @PostMapping("/signup")
public ResponseEntity<?> registerUser(@RequestBody Map<String, String> requestBody) {
    String firstName = requestBody.get("firstName");
    String lastName = requestBody.get("lastName");
    String email = requestBody.get("email");
    String username = requestBody.get("username");
    String dateOfBirth = requestBody.get("dateOfBirth");
    String gender = requestBody.get("gender");
    String phone = requestBody.get("phone");
    String password = requestBody.get("password");
    String confirmPassword = requestBody.get("confirmPassword");

    // Validate passwords match
    if (!password.equals(confirmPassword)) {
        return ResponseEntity.badRequest().body("Passwords do not match.");
    }

    // Check for uniqueness
    if (userService.isEmailExists(email)) {
        return ResponseEntity.badRequest().body("Email already exists.");
    }
    if (userService.isPhoneExists(phone)) {
        return ResponseEntity.badRequest().body("Phone number already exists.");
    }
    if (userService.isUsernameExists(username)) {
        return ResponseEntity.badRequest().body("Username already exists.");
    }

      LocalDate dob;
    try {
        dob = LocalDate.parse(dateOfBirth); // Parse the String into LocalDate
    } catch (DateTimeParseException e) {
        return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD.");
    }

    try {
        // Create and register user
        UserTable user = new UserTable();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setDob(dob);
        user.setGender(gender);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));

        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }
}


    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model) {
        // Invalidate the session to clear existing user authentication
        request.getSession().invalidate();
        return "login";
    }


@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
    String username = requestBody.get("username");
    String password = requestBody.get("password");

    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Explicitly set SecurityContextHolder in session
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        String redirectUrl = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
                ? "/admin/dashboard"
                : "/dashboard";

        return ResponseEntity.ok(Map.of("message", "Login successful!", "redirectUrl", redirectUrl));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}





    @GetMapping("/forgot-password")
    public String showResetPasswordRequestForm() {
        return "reset-password-request"; // View for password reset request
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> requestBody) {
    String email = requestBody.get("email");
    try {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset email sent successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password"; // View for resetting password
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        String newPassword = requestBody.get("newPassword");
        
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
    try {
        // Invalidate the session to log out the user
        request.getSession().invalidate();
        return ResponseEntity.ok("Logout successful");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout");
    }
}

}
