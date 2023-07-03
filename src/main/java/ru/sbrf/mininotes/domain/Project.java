package ru.sbrf.mininotes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
/**
 * Класс прооекта пользователя со свойствами <b>projectId</b>, <b>projectName</b>, <b>notes</b> и <b>user</b>.
 */
@Entity
@Table(name = "Project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int projectId;

    private String  projectName;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Note> notes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    public Project(){};


    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public User getUser() {
        return owner;
    }

    public void setUser(User owner) {
        this.owner = owner;
    }

}
