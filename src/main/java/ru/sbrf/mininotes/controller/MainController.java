package ru.sbrf.mininotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.mininotes.domain.Project;
import ru.sbrf.mininotes.domain.User;
import ru.sbrf.mininotes.domain.UserRole;
import ru.sbrf.mininotes.repository.NoteRepository;
import ru.sbrf.mininotes.repository.ProjectRepository;
import ru.sbrf.mininotes.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

/**
 * Класс контроллера.
 */
@Controller
@RequestMapping("/")
public class MainController {

    UserRepository userRepository;
    ProjectRepository projectRepository;
    NoteRepository noteRepository;
    PasswordEncoder passwordEncoder;

    /**
     * Конструктор класса.
     */
    @Autowired
    public MainController(UserRepository userRepository, ProjectRepository projectRepository, NoteRepository noteRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.noteRepository = noteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String getRegistration(Model model){
        return "registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute User user, @RequestParam String password1, Model model){
        if(Objects.equals(user.getPassword(), password1)) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setUserRole(UserRole.USER);
            userRepository.save(user);
            System.out.println("exist&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + userRepository.existsById(1));
            System.out.println("login: " + user.getUsername() + "-------" + user.getPassword());
            return "redirect:/login";
        }else{
            model.addAttribute("Invalid username and password", "Invalid username and password");
        }

        return "redirect:/registration";
    }

    @GetMapping("/home")
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("allProjects", projectRepository.findAll());
        return "home";
    }

    @GetMapping("/home/edit")
    public String homeEdit(Model model){
        model.addAttribute("allProjects", projectRepository.findAll());
        return "edit";
    }

    @PostMapping("/home/edit")
    public String addProject(@ModelAttribute Project project){
        projectRepository.save(project);
        return "redirect:/home/edit";
    }

    @PostMapping("/home/edit/delete")
    public String delProject(@ModelAttribute Project project){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.getUserByUsername(auth.getName());
        Project myProject = projectRepository.getReferenceById(project.getProjectId());
        if(user.isPresent()){
            System.out.println("!!!!!!!!!!!!!!");
            User myUser = user.get();
            myUser.delProject(myProject);
            userRepository.save(myUser);
            System.out.println("projectID "+ myProject.getProjectId() +" projectName "+ myProject.getProjectName());
        }
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }


}
