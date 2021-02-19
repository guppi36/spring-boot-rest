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

    @GetMapping(value = "/")
	public String printStart(ModelMap model) {
		List<User> userList = userService.listUsers();


		User userData = getUserByDetails();

		model.addAttribute("userData", userData);
		model.addAttribute("users", userList);
		return "user-list";
	}

   	@PostMapping(value = "/add")
	public String addUser2(@RequestParam String username,
						   @RequestParam String lastName,
						   @RequestParam Integer age,
						   @RequestParam String email,
						   @RequestParam String password,
						   @RequestParam String role, ModelMap model) {
		User newUser = new User(username, email, password);
		newUser.setLastName(lastName);
		newUser.setAge(age);

		Set<Role> roles = new HashSet<>();
		roles.add(Role.getUserRole());
		if(role.equals("ADMIN")) roles.add(Role.getAdminRole());

		newUser.setRoles(roles);
		System.out.println(newUser + " // " + role);
		userService.add(newUser);

		List<User> userList = userService.listUsers();
		model.addAttribute("userData", getUserByDetails());
		model.addAttribute("users", userList);
		return "user-list";
	}

	@PostMapping(value = "/update")
	public String updateUser(@ModelAttribute(value="user") User user,
							 @RequestParam(defaultValue = "none") String role,ModelMap model) {
		Set<Role> roles = new HashSet<>();
		if(role.equals("none")) {
			User oldUser = userService.getUserById(user.getId().intValue());
			roles = oldUser.getRoles();
		} else {
			roles.add(Role.getUserRole());
			if (role.equals("ADMIN")) roles.add(Role.getAdminRole());
		}
		user.setRoles(roles);
		System.out.println(user);
		userService.update(user);

		List<User> userList = userService.listUsers();

		model.addAttribute("userData", getUserByDetails());
		model.addAttribute("users", userList);
		return "user-list";
	}

	@PostMapping(value = "/delete")
	public String deleteUser(@ModelAttribute(value="user") User user, ModelMap model) {
        System.out.println(user);
		userService.delete(user);
		List<User> userList = userService.listUsers();

		model.addAttribute("userData", getUserByDetails());
		model.addAttribute("users", userList);
		return "user-list";
	}

	@GetMapping("/findOne")
	@ResponseBody
	public User findOne(Integer id){
    	User user = userService.getUserById(id);
    	return user;
	}
}
