package userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import userservice.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping(path = "/")
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "username", "name", "surname"));
        return users;
    }

}
