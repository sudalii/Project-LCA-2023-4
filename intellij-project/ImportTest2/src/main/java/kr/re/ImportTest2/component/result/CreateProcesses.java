package kr.re.ImportTest2.component.result;

import kr.re.ImportTest2.component.util.ConvertUnits;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.UserFlows;
import lombok.extern.slf4j.Slf4j;
import org.openlca.core.database.FlowDao;
import org.openlca.core.database.FlowPropertyDao;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.database.UnitDao;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CreateProcesses extends SystemBuilder {

    public double targetAmount = 0.0;
    public String targetUnit = null;
    public String productName = null;

    protected CreateProcesses() {
    }

    protected Process createProcesses(User user) {
        productName = user.getProductName();
        targetAmount = user.getTargetAmount();
        targetUnit = user.getTargetUnit();

        // baseFlow: Processes 생성을 위한 processes 기준흐름 가져오거나 생성
        Flow baseFlow = createProductBasedFlow();

        // 여러 process들을 flow로 받아서 product system으로 만들 process 가져오거나 생성
        Process processes = createProcess(baseFlow);

        processes.quantitativeReference.unit = new UnitDao(db).getForName(targetUnit).get(0);
        processes.quantitativeReference.amount = targetAmount;

        return processes;
    }

    private Flow createProductBasedFlow() {
        List<Flow> searchF = new FlowDao(db).getForName(productName);

        if (searchF.isEmpty()) {
            log.info("{} flow is null, create this flow.", productName);
            FlowProperty mass = new FlowPropertyDao(db).getForName("Mass").get(0);
            Flow f = Flow.product(productName, mass);
            new FlowDao(db).insert(f);
        }
        Flow baseFlow = new FlowDao(db).getForName(productName).get(0);
        log.info("create baseFlow = {}, flowType = {}", baseFlow, baseFlow.flowType);

        return baseFlow;
    }

    private Process createProcess(Flow baseFlow) {
        List<Process> searchP = new ProcessDao(db).getForName(productName);
        if (!searchP.isEmpty()) {
            for (Process p : searchP) {
                new ProcessDao(db).delete(p);
            }
        }
        log.info("{} processes is null, create this processes.", productName);
        Process p = Process.of(productName, baseFlow);
        new ProcessDao(db).insert(p);

        Process ps = new ProcessDao(db).getForName(productName).get(0);
        log.info("processes = {}, quantitativeReference = {}", ps, ps.quantitativeReference);

        return ps;
    }


    protected void addProcess(Process processes, SelectedProcess userP) {
        // 사용자가 입력한 process name에 매칭된 process id로 국가DB 검색해서 가져오기
        Process koreaDb = new ProcessDao(db).getForRefId(userP.getMappedProcessId());
        Process customized = customizeAProcess(userP, koreaDb); // CustomizeLciDb()로 수정할까.

        // 해당 국가DB의 이름을 가진 product type flow 가져오기
        // import 시 자동 생성되어 get으로 가져오면 됨
        Exchange ref = customized.quantitativeReference;
        ref.amount = userP.getProcessAmount();
        ref.unit = new UnitDao(db).getForName(userP.getProcessAmountUnit()).get(0);
        ref.defaultProviderId = customized.id;
        processes.exchanges.add(ref);
    }

    private Process customizeAProcess(SelectedProcess userP, Process koreaP) {
        log.info("CustomizeAProcess method start");
        // 기존에 혹여나 있을 customize한 process는 지우고 시작
        List<Process> exist = new ProcessDao(db).getForName(userP.getProcessName());
        if (!exist.isEmpty()) {
            for (Process p : exist) {
                log.info("delete process of {}", p.name);
                new ProcessDao(db).delete(p);
            }
        }
        Process copy = koreaP.copy();
        copy.name = userP.getProcessName();

        // 배송은 물질 단위에서 계산할 것이 없으므로 패스
        if (!getType(userP, "TRANSPORTATION")) {
            copy = calculateEnvironmentalLoad(userP, copy);
        } else {
            log.info("The userP type is TRANSPORTATION.");
        }

        // 확인코드
        Exchange ref = copy.quantitativeReference; // 국가DB의 기준물질 적재
        log.info("{}의 기준물질 = {}, {}{}\n", copy.name, ref.flow.name, ref.amount, ref.unit.name);

        setInputByType(userP, ref);

//        return new ProcessDao(db).insert(copy);
        return new ProcessDao(db).update(copy);
    }

    private void setInputByType(SelectedProcess userP, Exchange ref) {
        if (getType(userP, "END_OF_LIFE")) {
            ref.isInput = false;
        } else {
            ref.isInput = true;
        }
    }

    /**
     * 계산 시 무조건 t->kg, MJ->kWh, m3->kg로 변환
     */
    private Process calculateEnvironmentalLoad(SelectedProcess userP, Process copyKoreaP) {
        log.info("calculateEnvironmentalLoad method start");
        log.info("Custom LCI DB 계산식: 국가DB 물질 양 + (국가DB 공정 양(기준흐름) * 사용자입력 물질 값)/사용자입력 공정 값");
        UserFlows userF = userP.getFlows();
        ConvertUnits convert = new ConvertUnits();
        ConvertUnits.Converted userPAmount = convert.convertUnits(userP.getProcessAmount(), userP.getProcessAmountUnit());

        // 사용자 입력과 국가 DB 데이터를 기반으로 환경 부하 계산
        // 전기 = iFlow1, 물 = iFlow2 & oFlow1
        for (Exchange e : copyKoreaP.exchanges) {
            Flow f = e.flow;
            if ((e.isInput) && (userF.getIFlow2() != 0)) { // iWater 분별
                if (getType(userP, "RAW_MATERIALS")) {
                    if (f.name.equals("Water (fresh water)")) {
                        log.info("e.amount of {} = {}", f.name, e.amount);
                        ConvertUnits.Converted iWater = convert.convertUnits(userF.getIFlow2(), userF.getIFlow2Unit());
                        log.info("{} + ({} * {})/{}", e.amount, copyKoreaP.quantitativeReference.amount, iWater.value(), userPAmount.value());
                        e.amount = e.amount + (copyKoreaP.quantitativeReference.amount * iWater.value()) / userPAmount.value();
                        log.info("Adjusted Amount for {} = {}\n", e.flow.name, e.amount);
                    }
                } else {    // No Raw_materials
                    if (f.name.equals("Water, unspecified natural origin/kg")) {
                        log.info("e.amount of {} = {}", f.name, e.amount);
                        ConvertUnits.Converted iWater = convert.convertUnits(userF.getIFlow2(), userF.getIFlow2Unit());
                        log.info("{} + ({} * {})/{}", e.amount, copyKoreaP.quantitativeReference.amount, iWater.value(), userPAmount.value());
                        e.amount = e.amount + (copyKoreaP.quantitativeReference.amount * iWater.value()) / userPAmount.value();
                        log.info("Adjusted Amount for {} = {}\n", e.flow.name, e.amount);
                    }
                }
            }
            else if ((!e.isInput) && (userF.getOFlow1() != 0)) { // oWater 분별
                if (f.name.equals("Water/kg")) {
                    ConvertUnits.Converted oWater = convert.convertUnits(userF.getOFlow1(), userF.getOFlow1Unit());
                    log.info("isInput={}, e.amount of {} = {}", e.isInput, f.name, e.amount);
                    log.info("{} + ({} * {})/{}", e.amount, copyKoreaP.quantitativeReference.amount, oWater.value(), userPAmount.value());
                    e.amount = e.amount + (copyKoreaP.quantitativeReference.amount * oWater.value()) / userPAmount.value();
                    log.info("Adjusted Amount for {} = {}\n", e.flow.name, e.amount);
                }
            }
        }
        return addElec(userP, copyKoreaP, userPAmount);
    }

    private Process addElec(SelectedProcess userP, Process copyKoreaP, ConvertUnits.Converted userPAmount) {
        UserFlows userF = userP.getFlows();
        ConvertUnits convert = new ConvertUnits();

        // 국가DB에서 전기 DB(=전기 flow) 가져오기
        Process elec = new ProcessDao(db).getForRefId("0bbf3ad4-480a-4b0b-bb86-5958945503a8");
//        log.info("electric db = {}", elec);

        Exchange exElec = elec.quantitativeReference;
        ConvertUnits.Converted iElec = convert.convertUnits(userF.getIFlow1(), userF.getIFlow1Unit());
        exElec.amount = 1;

        log.info("before calc exElec amount = {} {}{}", exElec.flow.name, exElec.amount, exElec.unit.name);
        exElec.amount = (copyKoreaP.quantitativeReference.amount * iElec.value()) / userPAmount.value();
        log.info("{} = ({} * {})/{}", exElec.amount, copyKoreaP.quantitativeReference.amount, iElec.value(), userPAmount.value());
        exElec.unit = new UnitDao(db).getForName("kWh").get(0); // -> 무조건 kWh.
        exElec.isInput = true;
        exElec.defaultProviderId = elec.id;
        log.info("Add Elec = {}\n", exElec);

        copyKoreaP.exchanges.add(exElec);

        return copyKoreaP;
    }

    boolean getType(SelectedProcess userP, String type) {
        return userP.getType().name().equals(type);
    }

}
