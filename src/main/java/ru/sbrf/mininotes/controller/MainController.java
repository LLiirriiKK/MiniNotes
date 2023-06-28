package ru.sbrf.mininotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sbrf.mininotes.domain.User;
import ru.sbrf.mininotes.repository.NoteRepository;
import ru.sbrf.mininotes.repository.ProjectRepository;
import ru.sbrf.mininotes.repository.UserRepository;
/**
 * Класс контроллера.
 */
@Controller
@RequestMapping("/")
public class MainController {

    UserRepository userRepository;
    ProjectRepository projectRepository;
    NoteRepository noteRepository;

    /**
     * Конструктор класса.
     */
    @Autowired
    public MainController(UserRepository userRepository, ProjectRepository projectRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.noteRepository = noteRepository;
    }


    @GetMapping("/home")
    public String home(Model model, User user){
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }


}
