package lca.lca2023.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ImportTestController {

    @GetMapping("mvc-test")
    public String mvcTest(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "template-test";
      }

}
