package ru.sbrf.mininotes.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.mininotes.domain.Note;
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

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

    @GetMapping("/home/profile")
    public String profile(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "profile";
    }
    @GetMapping("/registration")
    public String getRegistration(Model model){
        model.addAttribute("user", new User());
        return "registration2";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("user") @Valid User user,Errors errors, @RequestParam String password1){
        if(errors.hasErrors()){
            return "registration2";
        }
        Optional<User> userOptional = userRepository.getUserByUsername(user.getUsername());
        if(userOptional.isPresent()){
            errors.rejectValue("username", "user.exist", "Пользователь с таким именем уже существует");
            return "registration2";
        }
        if(Objects.equals(user.getPassword(), password1)) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setUserRole(UserRole.USER);
            userRepository.save(user);
            return "redirect:/login";
        }else{
            errors.rejectValue("password", "password.no.match", "пароли не совпадают");
            return "registration2";
        }
    }

    @GetMapping("/home")
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        Optional<User> userOptional = userRepository.getUserByUsername(auth.getName());
        if(userOptional.isPresent()){
            User myUser = userOptional.get();
            model.addAttribute("allProjects", myUser.getProjects());
            model.addAttribute("project", new Project());
            return "home";
        }
        return "redirect:/error";
    }

    @GetMapping("/home/edit")
    public String homeEdit(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.getUserByUsername(auth.getName());
        if(userOptional.isPresent()){
            User myUser = userOptional.get();
            model.addAttribute("allProjects", myUser.getProjects());
        }
        return "edit";
    }

    @PostMapping("/home/edit")
    public String addProject(@ModelAttribute Project project){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.getUserByUsername(auth.getName());
        if(user.isPresent()) {
            User myUser = user.get();
            myUser.addProject(project);
            userRepository.save(myUser);
        }
        return "redirect:/home/edit";
    }

    @PostMapping("/home/edit/delete")
    public String delProject(@ModelAttribute Project project){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        Optional<User> user = userRepository.getUserByUsername(auth.getName());
        System.out.println(project.toString());
        if(user.isPresent()){
            User myUser = user.get();
            Optional<Project> projectOptional = myUser.getProjects().stream().filter(project1 -> project1.getProjectId()==project.getProjectId()).findAny();
            if(projectOptional.isPresent()){
                Project myProject = projectOptional.get();
                myUser.delProject(myProject);
                userRepository.save(myUser);
            }
        }
        return "redirect:/home/edit";
    }

    @GetMapping("/home/edit/projects/{projectId}")
    public String getProject(@PathVariable int projectId, Model model){
        model.addAttribute("project", projectRepository.getReferenceById(projectId));
        return "projects";
    }

    @PostMapping("/home/edit/projects/{projectId}")
    public String addNoteInProject(@PathVariable int projectId, @ModelAttribute Note note){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.getUserByUsername(auth.getName());
        if(user.isPresent()) {
            User myUser = user.get();
            Optional<Project> projectOptional = myUser.getProjects().stream().filter(project1 -> project1.getProjectId()==projectId).findAny();
            if(projectOptional.isPresent()){
                Project myProject = projectRepository.getReferenceById(projectId);
                myProject.addNote(note);
                projectRepository.save(myProject);
            }
        }
        return "redirect:/home/edit/projects/"+projectId;
    }

    @PostMapping("/home/edit/projects/{projectId}/delete")
    public String delNoteInProject(@PathVariable int projectId, @ModelAttribute Note note){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.getUserByUsername(auth.getName());
        if(user.isPresent()){
            User myUser = user.get();
            Optional<Project> projectOptional = myUser.getProjects().stream().filter(project1 -> project1.getProjectId()==projectId).findAny();
            if(projectOptional.isPresent()){
                Project myProject = projectOptional.get();
                Optional<Note> noteOptional = myProject.getNotes().stream().filter(note1 -> note1.getNoteId()==note.getNoteId()).findAny();
                if(noteOptional.isPresent()){
                    Note myNote = noteOptional.get();
                    myProject.delNote(myNote);
                    projectRepository.save(myProject);
                }
            }
        }
        return "redirect:/home/edit/projects/"+projectId;
    }

    @GetMapping("/home/edit/projects/{projectId}/notes/{noteId}")
    public String getNote(@PathVariable int projectId, @PathVariable int noteId, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.getUserByUsername(auth.getName());
        if(userOptional.isPresent()){
            User myUser = userOptional.get();
            model.addAttribute("note", noteRepository.getReferenceById(noteId));
            model.addAttribute("project", projectRepository.getReferenceById(projectId));
            return "notes";
        }
        return "redirect:/error";
    }

    @PostMapping("/home/edit/projects/{projectId}/notes/{noteId}")
    public String addContentInNote(@PathVariable int projectId, @PathVariable int noteId, @ModelAttribute Note note){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.getUserByUsername(auth.getName());
        if(user.isPresent()) {
            User myUser = user.get();
            Optional<Project> projectOptional = myUser.getProjects().stream().filter(project1 -> project1.getProjectId()==projectId).findAny();
            if(projectOptional.isPresent()){
                Project myProject = projectOptional.get();
                Optional<Note> noteOptional = myProject.getNotes().stream().filter(note1 -> note1.getNoteId()==noteId).findAny();
                if(noteOptional.isPresent()){
                    Note myNote = noteOptional.get();
                    myNote.setContent(note.getContent());
                    noteRepository.save(myNote);
                }
            }
        }
        return "redirect:/home/edit/projects/"+projectId +"/notes/" + noteId;
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login2";
    }


}
