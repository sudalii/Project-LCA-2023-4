package kr.re.ImportTest2.service;

import kr.re.ImportTest2.controller.dto.UserDto;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.repository.SelectedProcessRepository;
import kr.re.ImportTest2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SelectedProcessRepository spRepository;

    /**
     * - 유저 전체 조회
     * : 현재는 한 사람의 Product System을 기준으로 하고 있기 때문에,
     *   한 사람의 여러 Product System들이 출력될 것임
     */
    public List<UserDto> findProducts() {
        List<User> users = userRepository.findAll();
        List<UserDto> products = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .productName(user.getProductName())
                    .build();
            products.add(userDto);
        }

        return products;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID: " + id));
    }

    @Transactional
    public Long saveUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity()).getId();
    }

    public UserDto updateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Not found pId for updateUser: " + id));;

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .productName(user.getProductName())
                .targetAmount(user.getTargetAmount())
                .targetUnit(user.getTargetUnit())
                .build();
        return userDto;
    }
}
