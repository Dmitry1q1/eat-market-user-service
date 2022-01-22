package userservice.controller;

import org.springframework.web.bind.annotation.*;
import userservice.entity.User;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping(path = "/")
    public List<User> getUsers() {
        return null;
    }

}
