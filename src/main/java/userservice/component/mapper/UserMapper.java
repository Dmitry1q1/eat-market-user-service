package userservice.component.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import userservice.dto.UserDTO;
import userservice.entity.User;
import userservice.repository.RolesRepository;
import userservice.repository.UsersRepository;

import static userservice.component.enums.Roles.ROLE_USER;

@Component
public class UserMapper {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserMapper(UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
    }

    public User dtoToUser(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .phoneNumber(userDTO.getPhoneNumber())
                .role(rolesRepository.findRoleByName(ROLE_USER.value))
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
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
