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
        return new User(userDTO.getUsername(), userDTO.getFirstName(), userDTO.getLastName(),
                passwordEncoder.encode(userDTO.getPassword()), userDTO.getPhoneNumber(),
                rolesRepository.findRoleByName(ROLE_USER.value));
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
