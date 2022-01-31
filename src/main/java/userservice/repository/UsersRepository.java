package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import userservice.entity.Role;
import userservice.entity.User;

import javax.transaction.Transactional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    User findByUsername(@Param("username") String username);

    User findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Query(value = "INSERT INTO t_token_storage (token) VALUES (:token)", nativeQuery = true)
    @Transactional
    void sendToken(@Param("token") String token);

    @Query(value = "SELECT count(*) FROM t_token_storage WHERE t_token_storage.token = :token", nativeQuery = true)
    int getTokenCount(@Param("token") String token);

    @Query(value = "SELECT is_blocked FROM t_user u WHERE u.username = :username", nativeQuery = true)
    Boolean isUserBlocked(@Param("username") String username);

    @Query(value = "SELECT role_id FROM t_user u WHERE u.username = :username", nativeQuery = true)
    Integer findRoleIdByUsername(@Param("username") String username);

    @Modifying
    @Query(value = "UPDATE t_user u SET is_blocked = :is_blocked WHERE u.id = :id", nativeQuery = true)
    @Transactional
    void changeBlockingUser(@Param("is_blocked") boolean is_blocked, @Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM t_token_storage WHERE t_token_storage.token = :token", nativeQuery = true)
    @Transactional
    void deleteToken(@Param("token") String token);
}
