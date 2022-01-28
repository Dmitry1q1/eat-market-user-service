package userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/users/{username}/block")
    public ResponseEntity blockUser(@PathVariable("username") String username) {
        return userService.blockUser(username);
    }

    @PostMapping(path = "/users/{username}/unblock")
    public ResponseEntity unblockUser(@PathVariable("username") String username) {
        return userService.unblockUser(username);
    }

}
