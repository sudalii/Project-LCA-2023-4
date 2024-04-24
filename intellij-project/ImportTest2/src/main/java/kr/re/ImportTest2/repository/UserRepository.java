package kr.re.ImportTest2.repository;

import kr.re.ImportTest2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByProductName(String productName);
}
