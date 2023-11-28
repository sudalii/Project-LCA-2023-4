package kr.re.ImportTest2.controller;

import lombok.extern.slf4j.Slf4j;
import org.openlca.geo.GeoJsonImport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    public String index(Model model) {
        log.info("home controller");
        model.addAttribute("data", "hello");
        return "index";
    }
}
