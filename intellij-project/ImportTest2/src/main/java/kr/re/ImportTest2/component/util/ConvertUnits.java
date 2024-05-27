package kr.re.ImportTest2.component.util;

import kr.re.ImportTest2.domain.SelectedProcess;
import lombok.extern.slf4j.Slf4j;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.UnitDao;
import org.openlca.core.database.UnitGroupDao;
import org.openlca.core.model.Unit;
import org.openlca.core.model.UnitGroup;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ConvertUnits {

    /**
     * 자동 final 지정
     * 접근자 매서드(get/set) 및 생성자, equals(), hashCode(), toString() 같은 표준 메서드 자동 생성
     */
    public record Converted(Double value, Unit unit) {
    }

    private Double value = 0.0;
    private String unitName;
    public static IDatabase db = null;
    private Unit unit;

    /**
     * Electricity, Water, Transportation, ... flows
     */
    public Converted convertUnits(Double userValue, String userUnit) {  // = userF.getIFlowNUnit();
        Double userValueLog = userValue;
        switch (userUnit) {
            case "MJ":
                userValue *= 0.27778;  // == userValue = userValue * 0.27778;
                // fall-through intended to normalize MJ to kWh and then handle as kWh
            case "kWh":
                value = userValue;
                unitName = "kWh";
                break;

            case "m3", "t": // "m3": m3를 kg로 변환 (물의 밀도 기준)
                userValue *= 1000;
                // fall-through
            case "kg":
                value = userValue;
                unitName = "kg";
                break;
            default:
                log.error("Unknown unit");
        }
        unit = new UnitDao(db).getForName(unitName).get(0);
        if(!Objects.equals(userValueLog, value))
            log.info("result converted data = {}{} -> {}{}", userValueLog, userUnit, value, unit.name);

        return new Converted(value, unit);
    }

    /**
     * Process, Product Amount
     */
    public Converted convertKgToTon(Double userValue, String userUnit) {  // = userF.getIFlowNUnit();
        switch (userUnit) {
            case "kg":
                userValue *= 0.001;
                // fall-through
            case "t":
                value = userValue;
                unitName = "km";
                break;
            default:
                log.error("Unknown unit");
        }
        unit = new UnitDao(db).getForName(unitName).get(0);
        return new Converted(value, unit);
    }


    public Converted convertTransport(Double distValue, Double weightValue, String distUnit, String weightUnit) {
        List<String> transport = new ArrayList<>();
        transport.add(distUnit);
        transport.add(weightUnit);

        double convertedDist = 0;
        double convertedWeight = 0;

        for (String s : transport) {
            switch (s) {
                case "t": // "t": transport 에서의 무게 변환, "m": 거리 변환
                    weightValue *= 1000;
                    // fall-through
                case "kg":
                    convertedWeight = weightValue;
                    break;
                case "m":
                    distValue *= 1000;
                    // fall-through
                case "km":
                    convertedDist = distValue;
                    break;
                default:
                    log.error("Unknown unit of transport");
            }
        }
        value = convertedWeight * convertedDist;
        unitName = "kg*km";
        unit = new UnitDao(db).getForName(unitName).get(0);

        return new Converted(value, unit);
    }

    public void printUnitList(IDatabase db) {
        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/";
        try (PrintWriter writer = new PrintWriter(prePath + "unit.txt")) {
            List<UnitGroup> unitGroups = new UnitGroupDao(db).getAll();
            writer.println("unitGroup list: ");
            for (UnitGroup unit : unitGroups) {
                writer.println("\nunit list: " + unit);
                for (Unit u : unit.units) {
                    writer.print("origin: " + u);
                    writer.println("     To string: " + u.name);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
