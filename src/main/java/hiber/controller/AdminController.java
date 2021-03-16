package hiber.controller;

import hiber.model.Role;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    private User getUserByDetails(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;
		if(principal instanceof UserDetails){
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
    	return userService.getUserByEmail(email);
	}

    @GetMapping
	public String printStart(ModelMap model) {
		List<User> userList = userService.listUsers();
		User userData = getUserByDetails();

		model.addAttribute("userData", userData);
		model.addAttribute("users", userList);
		return "user-list";
	}
}
