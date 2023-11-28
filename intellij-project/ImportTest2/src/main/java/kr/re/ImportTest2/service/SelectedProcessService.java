package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.result.SystemBuilder;
import lombok.RequiredArgsConstructor;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.ProductSystem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SelectedProcessService {
    
    private final RunDatabase runDatabase;
    private final SystemBuilder systemBuilder;

    public IDatabase db = null;
    public ProductSystem system = null;

    /**
     * 사용자가 화면에서 각 공정에서 국가 DB를 선택 클릭하면,
     * 그 클릭된 db name 과 db id를 매칭해서 return 한다.
     */

    public String dbMapper(String koreaDbName) {
        if (koreaDbName == null) {
            return "null";
        }


        return switch (koreaDbName) {
//            case "" -> "";
            case "공로수송" -> "be291b6aca084e80b683064f198925bd";

            case "혼합플라스틱매립" -> "cc26ff7ce8c0447a9c810f9ce36c7ec4"; // KR_mixed_plastic_landfill
            case "폐플라스틱소각" -> "eedf68a25a794397a1a36396641cd6bb"; // Mixed plastics incineration - KR
            case "펠렛용폐플라스틱재활용" -> "6660103878de4f7f9c16097572ee7cf0"; // Recycling_Pellet_Waste_Plastic - KR
            default -> throw new IllegalStateException(
                    "Unexpected value in dbMapper...: " + koreaDbName);
        };
    }

    public void runDb() {
        db = runDatabase.runDb();
    }

    public void addProcess(String processId, double amount) {
        systemBuilder.addProcess(db, processId, amount);
    }

    public void systemBuilder() {
        system = systemBuilder.createProductSystem(db);
    }

    public void closeDb() throws IOException {
        runDatabase.closeDb();
        db = null;
    }
}
