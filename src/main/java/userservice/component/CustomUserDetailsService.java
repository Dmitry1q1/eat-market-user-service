package userservice.component;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import userservice.entity.User;
import userservice.repository.UsersRepository;

import java.util.Objects;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (Objects.nonNull(user = usersRepository.findByUsername(username))) {
            return user;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
