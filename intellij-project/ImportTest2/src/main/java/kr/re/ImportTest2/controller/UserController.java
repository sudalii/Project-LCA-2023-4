package kr.re.ImportTest2.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.re.ImportTest2.controller.dto.ProcessDto;
import kr.re.ImportTest2.controller.dto.UserDto;
import kr.re.ImportTest2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Create a new product.
     */

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/services/services-cover")
    public String cover() {
        return "services/services-cover";
    }

    @GetMapping("/services/p1/new")
    public String createUser(Model model, UserDto userDto, HttpSession session) {
        log.info("get - UserDto: {}", userDto);
        model.addAttribute("user", userDto);

        return "services/p1/createForm";
    }

    @PostMapping("/services/p1/new")
    public String saveUser(@Valid @ModelAttribute("user") UserDto userDto,
                           RedirectAttributes redirect) {
        log.info("post - UserDto: {}", userDto);
        Long userId = userService.saveUser(userDto);

        redirect.addAttribute("typeStr", "p2");

        return "redirect:/services/" + userId + "/{typeStr}";
    }

    @GetMapping("/services/{userId}/p1")
    public String updateUser(@PathVariable("userId") Long userId, Model model) {
        UserDto userDto = userService.updateUser(userId);
        model.addAttribute("user", userDto);
        
        return "services/p1/updateForm";
    }

    @PutMapping("/services/{userId}/p1")
    public String saveUpdateUser(@Valid @ModelAttribute("user") UserDto userDto) {
        log.info("put-saveUpdateUser-getId = {}", userDto.getId());
        userService.saveUser(userDto);

        return "redirect:/services/" + userDto.getId() + "/p2";
    }
}
