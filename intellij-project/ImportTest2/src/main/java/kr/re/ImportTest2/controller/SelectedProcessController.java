package kr.re.ImportTest2.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.re.ImportTest2.controller.dto.ProcessDto;
import kr.re.ImportTest2.domain.enumType.LciDb;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import kr.re.ImportTest2.service.SelectedProcessService;
import kr.re.ImportTest2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/services/{userId}/{typeStr}")
public class SelectedProcessController {

    private final SelectedProcessService spService;

    /**
     *   html 화면은 return에 적힌 url로 이동
     * - save는 두 가지임
     *    - new process 의 save
     *    - 기존 process 의 save
     */
    @GetMapping("")
    public String index(@PathVariable("userId") Long userId,
                        @PathVariable String typeStr, Model model) {
        model.addAttribute("userId", userId);

        return "/services/" + typeStr + "/index";
    }

    @GetMapping("/add")
    public String createProcess(@PathVariable("userId") Long userId,
                                @PathVariable("typeStr") String typeStr,
                                Model model, ProcessDto processDto) {
        log.info("get-createProcess-dto: {}", processDto);

        List<LciDb> lciDbs = LciDb.getValuesByType(typeStr);
        model.addAttribute("lciDbs", lciDbs);
        model.addAttribute("sp", processDto);

        return "services/" + typeStr + "/createForm";
    }

    @PostMapping("/add")
    public String saveProcess(@PathVariable("userId") Long userId,
                              @PathVariable("typeStr") String typeStr,
                              @Valid @ModelAttribute("sp") ProcessDto processDto) {
        log.info("post-saveProcess-dto: {}", processDto);

        String pId = spService.saveProcess(processDto, userId, typeStr).toString();

        return "redirect:/services/" + userId + "/" +  typeStr + "/" + pId;
    }

    @GetMapping("/{pId}")
    public String selectProcess(@PathVariable("typeStr") String typeStr,
                                @PathVariable("pId") Long pId, Model model) {
        ProcessDto processDto = spService.updateForm(pId);
        log.info("get-selectProcess-userId 확인 = {}", processDto.getUserId());
        List<LciDb> lciDbs = LciDb.getValuesByType(typeStr);
        model.addAttribute("lciDbs", lciDbs);
        model.addAttribute("sp", processDto);

        log.info("get-selectProcess-pId 확인 = {}", processDto.getId());

        return "services/" + typeStr + "/updateForm";
    }

    @PutMapping("/{pId}")
    public String updateProcess(@PathVariable("typeStr") String typeStr,
                                @Valid ProcessDto processDto) {
        log.info("put-selectProcess-pId 확인 = {}", processDto.getId());
        spService.updateProcess(processDto);

        return "redirect:/services/" + processDto.getUserId() + "/" +  typeStr + "/" + processDto.getId();
//        return "redirect:/services/" + processDto.getUserId() + "/" + typeStr;
    }

    @DeleteMapping("/{pId}")
    public String deleteProcess(@PathVariable("userId") String userId,
                                @PathVariable("typeStr") String typeStr,
                                @PathVariable("pId") Long pId) {   // id 부분 수정
        log.info("delete-deleteProcess-pId: {}", pId);
        spService.deleteProcess(pId);
        return "redirect:/services/" + userId + "/" + typeStr; // /index.html
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, List<ProcessDto>>> listByType(
                                @PathVariable("userId") Long userId) {
        log.info("Returning list by type as JSON");
        Map<String, List<ProcessDto>> processes = new HashMap<>();
        processes.put("p2", spService.findAllByType(userId, ProcessType.RAW_MATERIALS));
        processes.put("p3", spService.findAllByType(userId, ProcessType.PROCESSING));
        processes.put("p4", spService.findAllByType(userId, ProcessType.TRANSPORTATION));
        processes.put("p5", spService.findAllByType(userId, ProcessType.END_OF_LIFE));

        return ResponseEntity.ok(processes);
    }

}