package auca.rw.PersonalExpensesMonitor.Controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import auca.rw.PersonalExpensesMonitor.DTO.UserDto;

import org.springframework.security.core.Authentication;



@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            String role = authentication.getAuthorities().toString();

            model.addAttribute("user", new UserDto(username, role));
        }

        return "menus"; 
    }
}
