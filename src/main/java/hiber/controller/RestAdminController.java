package hiber.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.google.gson.Gson;
import hiber.model.Role;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class RestAdminController {

    private final UserService userService;

    @Autowired
    public RestAdminController(UserService clientService) {
        this.userService = clientService;
    }

    @GetMapping(value = "/loginUser")
    public ResponseEntity<User> loginUser() {
        final User user = getUserByDetails();
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/users")
    public ResponseEntity<?> create(String user,String role) {
        Gson g = new Gson();
        User newUser = g.fromJson(user, User.class);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.getUserRole());
        if(role.equals("ADMIN")) roles.add(Role.getAdminRole());

        newUser.setRoles(roles);

        userService.add(newUser);
        return new ResponseEntity<>(userService.getUserByEmail(newUser.getUsername()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> read() {
        final List<User> clients = userService.listUsers();

        return clients != null &&  !clients.isEmpty()
                ? new ResponseEntity<>(clients, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") int id) {
        final User client = userService.getUserById(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/users")
    public ResponseEntity<?> update(String user,@RequestParam(defaultValue = "none") String role) {
        Gson g = new Gson();
        User newUser = g.fromJson(user, User.class);

        Set<Role> roles = new HashSet<>();
        if(role.equals("none")) {
            User oldUser = userService.getUserById(newUser.getId().intValue());
            roles = oldUser.getRoles();
        } else {
            roles.add(Role.getUserRole());
            if (role.equals("ADMIN")) roles.add(Role.getAdminRole());
        }
        newUser.setRoles(roles);

        userService.update(newUser);
        return new ResponseEntity<>(userService.getUserByEmail(newUser.getUsername()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        userService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
