package ru.sbrf.mininotes.domain;

import jakarta.persistence.*;

import java.util.Date;
/**
 * Класс заметки из прооекта пользователя со свойствами <b>noteId</b>, <b>content</b>, <b>project</b>.
 */
@Entity
@Table(name = "Note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int noteId;
    private String noteTitle;
    private String content;
    private Date noteDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public Note(){};
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
