package userservice.component.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import userservice.dto.UserDTO;
import userservice.entity.Role;
import userservice.entity.User;
import userservice.repository.RolesRepository;
import userservice.repository.UsersRepository;

import static userservice.component.enums.Roles.ROLE_USER;

@Component
public class UserMapper {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;

    public UserMapper(UsersRepository usersRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User dtoToUser(UserDTO userDTO) {
        Integer id = usersRepository.findRoleIdByUsername(userDTO.getUsername());
        Role role;
        if (id == null) {
            role = rolesRepository.findRoleByName(ROLE_USER.value);
        }  else {
            role = rolesRepository.findRoleById(id);
        }
        Boolean isBlocked = usersRepository.isUserBlocked(userDTO.getUsername());
        if (isBlocked == null) {
            isBlocked = false;
        }
        return new User(userDTO.getUsername(), userDTO.getFirstName(),
                userDTO.getLastName(), passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getPhoneNumber(), role, isBlocked);
    }

    public UserDTO userToDto(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .passwordConfirm(user.getPassword())
                .build();
    }
}
