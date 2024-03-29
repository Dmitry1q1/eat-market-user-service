package userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import userservice.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    public ResponseEntity testUser() {
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping(path = "/users/{id}/block")
    public ResponseEntity blockUser(@PathVariable("id") Long id) {
        return userService.changeBlockingUser(true, id);
    }

    @PostMapping(path = "/users/{id}/unblock")
    public ResponseEntity unblockUser(@PathVariable("id") Long id) {
        return userService.changeBlockingUser(false, id);
    }

}
