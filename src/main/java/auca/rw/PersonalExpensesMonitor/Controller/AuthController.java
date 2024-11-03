package auca.rw.PersonalExpensesMonitor.Controller;

import auca.rw.PersonalExpensesMonitor.Model.UserTable;
import auca.rw.PersonalExpensesMonitor.Services.CustomUserDetailsService;
import auca.rw.PersonalExpensesMonitor.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

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
    public String registerUser(@ModelAttribute UserTable user, @RequestParam String confirmpassword, Model model) {
        // Check if password and confirmation match
        if (!user.getPassword().equals(confirmpassword)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            return "signup";
        }

        // Check for uniqueness
        if (userService.isEmailExists(user.getEmail())) {
            model.addAttribute("errorMessage", "Email already exists.");
            return "signup";
        }
        if (userService.isPhoneExists(user.getPhone())) {
            model.addAttribute("errorMessage", "Phone number already exists.");
            return "signup";
        }
        if (userService.isUsernameExists(user.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists.");
            return "signup";
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.registerUser(user);
            model.addAttribute("successMessage", "User registered successfully");
            return "redirect:/login"; // Redirect to login page after successful signup
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
    }


    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model) {
        // Invalidate the session to clear existing user authentication
        request.getSession().invalidate();
        return "login";
    }


@PostMapping("/login")
public String loginUser(@RequestParam String username, @RequestParam String password, 
                        HttpServletRequest request, RedirectAttributes redirectAttributes) {
    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Explicitly set SecurityContextHolder in session
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        redirectAttributes.addFlashAttribute("successMessage", "Login successful!");

        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
        return "redirect:/login";
    }
}




    @GetMapping("/forgot-password")
    public String showResetPasswordRequestForm() {
        return "reset-password-request"; // View for password reset request
    }

    @PostMapping("/forgot-password")
    public String requestPasswordReset(@RequestParam String email, Model model) {
        try {
            userService.initiatePasswordReset(email);
            model.addAttribute("successMessage", "Password reset email sent successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "reset-password-request";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password"; // View for resetting password
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword, Model model) {
        try {
            userService.resetPassword(token, newPassword);
            model.addAttribute("successMessage", "Password reset successful");
            return "redirect:/login"; // Redirect to login after successful reset
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("token", token);
            return "reset-password"; // Return to reset password view
        }
    }

}
