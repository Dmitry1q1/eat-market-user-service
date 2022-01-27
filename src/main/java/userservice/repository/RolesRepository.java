package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import userservice.entity.Role;

public interface RolesRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(@Param("name") String roleName);
}
