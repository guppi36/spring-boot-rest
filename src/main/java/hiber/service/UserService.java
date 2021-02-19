package hiber.service;

import hiber.model.User;

import java.util.List;

public interface UserService {
    void add(User user);
    List<User> listUsers();
    void delete(User user);
    void deleteById(Integer id);
    void update(User user);
    User getUserByName(String username);
    User getUserByEmail(String email);
    User getUserById(Integer id);
}
