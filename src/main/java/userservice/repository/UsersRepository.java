package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import userservice.entity.User;

import javax.transaction.Transactional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    User findByUsername(@Param("username") String username);

    @Modifying
    @Query(value = "INSERT INTO t_token_storage (token) VALUES (:token)", nativeQuery = true)
    @Transactional
    void sentToken(@Param("token") String token);

    @Query(value = "SELECT count(*) FROM t_token_storage WHERE t_token_storage.token = :token", nativeQuery = true)
    int getTokenCount(@Param("token") String token);

    @Modifying
    @Query(value = "DELETE FROM t_token_storage WHERE t_token_storage.token = :token", nativeQuery = true)
    void deleteToken(@Param("token") String token);
}
