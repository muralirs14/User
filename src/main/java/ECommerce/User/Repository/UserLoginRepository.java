package ECommerce.User.Repository;

import ECommerce.User.Entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {
    //here we are using java 8 features that is optional class we are using that here to create custom queries
    Optional<UserLogin> findByEmail(String email);
    Optional<UserLogin> findByUsernameOrEmail(String username, String email);
    Optional<UserLogin> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
