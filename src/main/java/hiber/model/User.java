package hiber.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    // @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    public User() {

    }

    public User(String firstName, String username, String password) {
        this.firstName = firstName;
        this.email = username;
        this.password = password;
    }

    public User(String firstName, String lastName, Integer age, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = username;
        this.password = password;
    }

    public User(Long id, String firstName, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.email = username;
        this.password = password;
    }
    public User(Long id, String firstName, String username, String password, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.email = username;
        this.password = password;
        this.roles = roles;
    }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAllRoles(){
        StringBuilder sb = new StringBuilder();
        roles.forEach(r -> sb.append(r.getRoleNoRole()).append(" "));
        return sb.toString();
    }

    public String getAllNormalRoles(){
        StringBuilder sb = new StringBuilder();
        roles.forEach(r -> sb.append(r.getRole()).append(" "));
        return sb.toString();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String Username) { this.email = Username; }

    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        String rolesString = "";
        if(!roles.isEmpty())
            for ( Role r : roles) {
                rolesString += r.getRole() + " ";
            }
        return id + " " + firstName + " " + lastName + " " + age + " " + password + " " + email + " " + rolesString;
    }
}
