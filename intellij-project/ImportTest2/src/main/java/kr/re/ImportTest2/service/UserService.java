package kr.re.ImportTest2.service;

import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * - 유저 전체 조회
     * : 현재는 한 사람의 Product System을 기준으로 하고 있기 때문에,
     *   한 사람의 여러 Product System들이 출력될 것임
     */
    public List<User> findProducts() {
        return userRepository.findAll();
    }

    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }

    @Transactional
    public void update(Long id, String productName, double targetAmount, String targetUnit) {
        User user = userRepository.findOne(id);
        user.updateUser(productName, targetAmount, targetUnit);
    }

}
