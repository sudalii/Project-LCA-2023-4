package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.controller.form.UserForm;
import kr.re.ImportTest2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Create a new product.
     */
    @GetMapping("/services")
    public String create(Model model) {

        model.addAttribute("userForm", new UserForm());
        return "/services";
    }

    @PostMapping("/services/{userId}/edit")
    public String updateUser(@PathVariable Long userId, @ModelAttribute("userForm") UserForm form) {
        userService.update(userId, form.getProductName(), form.getTargetAmount(), form.getTargetUnit());

        return "redirect:/services";
    }

}
