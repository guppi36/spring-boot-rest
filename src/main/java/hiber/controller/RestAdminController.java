package hiber.controller;

import ch.qos.logback.core.net.server.Client;
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal instanceof UserDetails ? ((UserDetails)principal).getUsername() : principal.toString();

        final User user = userService.getUserByEmail(email);
        ResponseEntity<User> resp = user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();

        return resp;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/users")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody User user) {
        User newUser = user;
        Set<Role> roles = new HashSet<>();
        roles.add(Role.getUserRole());
        if(user.getAllNormalRoles().equals("ADMIN ")) roles.add(Role.getAdminRole());
        newUser.setRoles(roles);

        userService.add(newUser);
        ResponseEntity<User> resp = ResponseEntity.status(HttpStatus.CREATED).body(userService.getUserByEmail(newUser.getUsername()));
        return resp;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> read() {
        final List<User> clients = userService.listUsers();
        ResponseEntity<List<User>> resp = (clients != null &&  !clients.isEmpty()) ? ResponseEntity.ok(clients) : ResponseEntity.notFound().build();

        return resp;
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<User> read(@PathVariable(name = "id") int id) {
        final User client = userService.getUserById(id);
        ResponseEntity<User> resp = (client != null) ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();

        return resp;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/users")
    public ResponseEntity<?> update(@RequestBody User user) {
        User newUser = user;
        String role = user.getAllNormalRoles();
        Set<Role> roles = new HashSet<>();
        if(role.equals("none ")) {
            User oldUser = userService.getUserById(newUser.getId().intValue());
            roles = oldUser.getRoles();
        } else {
            roles.add(Role.getUserRole());
            if (role.equals("ADMIN ")) roles.add(Role.getAdminRole());
        }
        newUser.setRoles(roles);
        userService.update(newUser);

        ResponseEntity<User> resp = ResponseEntity.status(HttpStatus.CREATED).body(userService.getUserByEmail(newUser.getUsername()));
        return resp;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        userService.deleteById(id);
        ResponseEntity<Integer> resp = ResponseEntity.ok(id);
        return resp;
    }
}
