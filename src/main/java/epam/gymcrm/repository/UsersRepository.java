package epam.gymcrm.repository;

import epam.gymcrm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.username = :username")
    void changePassword(String username, String newPassword);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isActive = :isActive WHERE u.username = :username")
    void updateIsActive(String username, boolean isActive);

}
