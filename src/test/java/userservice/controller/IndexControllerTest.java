package userservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import userservice.entity.Role;
import userservice.repository.RolesRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IndexControllerTest {

    @Autowired
    private RolesRepository rolesRepository;

    @Test
    public void testH2DatabaseWorking() {
        Role role = new Role("ROLE_TEST");
        rolesRepository.save(role);
    }
}