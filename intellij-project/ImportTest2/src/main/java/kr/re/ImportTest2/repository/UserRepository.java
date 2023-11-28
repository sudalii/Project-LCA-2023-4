package kr.re.ImportTest2.repository;

import jakarta.persistence.EntityManager;
import kr.re.ImportTest2.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findByProductName(String productName) {
        return em.createQuery("select u from User u where u.productName = :productName",
                        User.class)
                .setParameter("productName", productName)
                .getResultList();
    }
    
}
