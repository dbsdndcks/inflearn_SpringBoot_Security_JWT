package Security_JWT.demo.repository;

import Security_JWT.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Integer> {
    //findBy 규칙 -> Username문법
    //select * from user where username = ?
    public User findByUsername(String username);

    //select * from user where email = ?
    public User findByEmail(String email);
}
