package com.anhngo.todolistweb.controller;

import com.anhngo.todolistweb.entity.ToDoListEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private List<ToDoListEntity> toDoList = new ArrayList<>();

    @Autowired
    private ObjectMapper mapper;

    @PostConstruct
    public void init() throws IOException {
        String path = "src/main/resources/toDoList.json";
        TypeReference<List<ToDoListEntity>> typeReference = new TypeReference<List<ToDoListEntity>>() {
        };
        List<ToDoListEntity> toDoList = mapper.readValue(new File(path), typeReference);
        this.toDoList = toDoList;
    }

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        model.addAttribute("toDoList", toDoList);
        return "index";
    }

    @RequestMapping("/add")
    public String add(@RequestParam(name = "name") String name,
                      @RequestParam(name = "done", defaultValue = "false") boolean done) {
        int id = toDoList.isEmpty() ? 1 : toDoList.get(toDoList.size() - 1).getId() + 1;
        toDoList.add(new ToDoListEntity(id, name, done));
        return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") int id) {
        toDoList.removeIf(item -> item.getId() == id);
        return "redirect:/";
    }

    @RequestMapping("/deleteAll")
    public String deleteAll() {
        toDoList.clear();
        return "redirect:/";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "search") String search, Model model) {
        List<ToDoListEntity> filteredList = toDoList.stream()
                .filter(item -> item.getTask().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
        model.addAttribute("toDoList", filteredList);
        return "index";
    }

    @RequestMapping("/doneAll")
    public String doneAll() {
        toDoList.forEach(item -> item.setCompleted(true));
        return "redirect:/";
    }

    @RequestMapping("/notDoneAll")
    public String notDoneAll() {
        toDoList.forEach(item -> item.setCompleted(false));
        return "redirect:/";
    }

    @RequestMapping("/done/{id}")
    public String done(@PathVariable(name = "id") int id) {
        toDoList.forEach(item -> {
            if (item.getId() == id) {
                item.setCompleted(true);
            }
        });
        return "redirect:/";
    }

    @RequestMapping("/notDone/{id}")
    public String notDone(@PathVariable(name = "id") int id) {
        toDoList.forEach(item -> {
            if (item.getId() == id) {
                item.setCompleted(false);
            }
        });
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") int id, Model model) {
        ToDoListEntity toDo = toDoList.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
        model.addAttribute("toDo", toDo);
        return "edit";
    }
}
