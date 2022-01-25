package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import userservice.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

}
