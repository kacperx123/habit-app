package com.habit.app.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
// Home Page -> register, login
// Login -> check user password, email. authenticate
// Register -> check user email, password, repeat password, check if match, send email.
// Main page(logged in) -> display username, (add to db)add task,(get tasks from db) see current tasks.
// Main page() -> logout button,
//
//



@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    public void addRole(UserRole userRole) {
        if (userRoles == null) {
            userRoles = new HashSet<>();  // Ensure userRoles is initialized
        }
        userRoles.add(userRole);
        userRole.setUser(this);
    }

    public void removeRole(UserRole userRole) {
        if (userRoles != null) {
            userRoles.remove(userRole);
            userRole.setUser(null);
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
