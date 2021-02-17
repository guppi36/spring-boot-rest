package hiber.controller;


import hiber.model.User;
import hiber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/user", method = RequestMethod.GET)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public String printStart(ModelMap model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if(principal instanceof UserDetails){
            email = ((UserDetails)principal).getUsername();
        } else {
            email = principal.toString();
        }

        List<User> userList = new ArrayList<>();
        User userData = userService.getUserByEmail(email);
        userList.add(userData);
        model.addAttribute("userData", userData);
        model.addAttribute("users", userList);
        return "user";
    }
}
