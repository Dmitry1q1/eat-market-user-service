package userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import userservice.component.JwtTokenProvider;
import userservice.dto.UserDTO;
import userservice.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class IndexController {

    private final UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/")
    public String index() {
        return "Hello, world!";
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
        return userService.loginUser(userDTO);
    }

    @PostMapping(path = "/registration")
    public ResponseEntity registration(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logoutUser(request, response);
    }
}
