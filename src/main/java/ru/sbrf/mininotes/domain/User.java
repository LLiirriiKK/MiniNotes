package ru.sbrf.mininotes.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс пользователя со свойствами <b>userId</b>, <b>userName</b>, <b>userRole</b> и <b>projects</b>.
 */
@Entity
@Table(name = "Client")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    private String userName;

    private String password;

    private enum Role{
        ADMIN, USER
    }

    private Role userRole;

    private enum Status{
        ACTIVE, BLOCKED
    }

    private Status status;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();

    public User(){};

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
