package userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import userservice.component.JwtTokenProvider;
import userservice.dto.UserDTO;
import userservice.entity.User;
import userservice.repository.RolesRepository;
import userservice.repository.UsersRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
    }

    public ResponseEntity loginUser(UserDTO userDTO) {

        try {
            String username = userDTO.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    userDTO.getPassword()));

            User user;
            if (Objects.isNull(user = usersRepository.findByUsername(username))) {
                throw new UsernameNotFoundException("User not found");
            }
            String token = jwtTokenProvider.createToken(username, user.getRole(), user.getId());

            usersRepository.sendToken(token);
            Map<Object, Object> model = new HashMap<>();
            model.put("success", true);
            model.put("username", username);
            model.put("userid", user.getId());
            model.put("token", token);

            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (AuthenticationException e) {
            Map<Object, Object> errorModel = new HashMap<>();
            errorModel.put("success", false);
            errorModel.put("errorDescription", "Invalid username/password supplied");
            return new ResponseEntity<>(errorModel, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity addUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(rolesRepository.findRoleByName("ROLE_USER"));
        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirm())) {
            userDTO.setErrorDescription("Password copies are not equals");
        }

        if (usersRepository.findByUsername(userDTO.getUsername()) != null) {
            userDTO.setErrorDescription("Username is not unique");
        }

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (userDTO.getErrorDescription() == null) {
            usersRepository.save(user);
        }

        if (userDTO.getErrorDescription() != null && !userDTO.getErrorDescription().isEmpty()) {
            Map<Object, Object> errorModel = new HashMap<>();
            errorModel.put("success", false);
            errorModel.put("errorDescription", userDTO.getErrorDescription());
            return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
        }
        Map<Object, Object> model = new HashMap<>();
        model.put("success", true);
        model.put("description", "Successful registration");
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    public ResponseEntity logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        String token = jwtTokenProvider.resolveToken(request);
        usersRepository.deleteToken(token);

        Map<Object, Object> model = new HashMap<>();
        model.put("success", true);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
