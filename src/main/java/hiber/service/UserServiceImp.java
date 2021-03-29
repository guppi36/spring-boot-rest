package hiber.service;

import hiber.model.Role;
import hiber.model.User;
import hiber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

   private final UserRepository userRepository;

   @Autowired
   public UserServiceImp(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   public void add(User user) {
      userRepository.save(user);
   }

   @Override
   public List<User> listUsers() {
      return userRepository.findAll();
   }

   @Override
   public void delete(User user) { userRepository.delete(user); }

   @Override
   public void deleteById(Integer id) {
      userRepository.delete(getUserById(id));
   }

   @Override
   public void update(User user) {
      User newUser = user;
      String role = user.getAllNormalRoles();
      Set<Role> roles = new HashSet<>();
      if(role.equals("none ")) {
         roles = userRepository.findByID(newUser.getId()).getRoles();
      } else {
         roles.add(Role.getUserRole());
         if (role.equals("ADMIN ")) roles.add(Role.getAdminRole());
      }
      newUser.setRoles(roles);
      userRepository.deleteById(newUser.getId());
      userRepository.save(newUser);
   }

   @Override
   public User getUserByName(String username) {
      return userRepository.findByFirstName(username);
   }

   @Override
   public User getUserByEmail(String email) {
      return userRepository.findByEmail(email);
   }

   @Override
   public User getUserById(Integer id) {
      return userRepository.findById(id.longValue()).get();
   }
}
