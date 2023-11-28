package kr.re.ImportTest2.component.result;

import lombok.RequiredArgsConstructor;
import org.openlca.core.database.*;
import org.openlca.core.math.SystemCalculator;
import org.openlca.core.matrix.ProductSystemBuilder;
import org.openlca.core.matrix.cache.MatrixCache;
import org.openlca.core.matrix.index.EnviFlow;
import org.openlca.core.matrix.linking.LinkingConfig;
import org.openlca.core.matrix.linking.ProviderLinking;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.core.results.LcaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
// 부모-자식 관계를 이용한 extends&생성자 사용은 나중에 시도해보는 걸로...
public class Calculation {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static IDatabase db = null;
    public ProductSystem system = null;


    public LcaResult calculate(String methodName) {
        var method = new ImpactMethodDao(db).getForName(methodName).get(0);
        CalculationSetup setup = CalculationSetup.of(system).withImpactMethod(method);
        SystemCalculator calc = new SystemCalculator(db);

        return calc.calculate(setup);
    }

    public void categoryResult(LcaResult r) {

/*        int i=0;
        while (i < r.impactIndex().size()) {


            // switch문은 String.contains()가 안먹힘
            if (r.impactIndex().at(i).toString().contains("GWP")) {
                var impact =  r.impactIndex().at(i);
                System.out.printf("%n%s  -> %.6f %s%n%n",
                        impact.name, r.getTotalImpactResult(impact), impact.referenceUnit);
                break;
            }
            else if (r.impactIndex().at(i).toString().contains("Abiotic depletion")) {
                var impact =  r.impactIndex().at(i);
                System.out.printf("%n%s  -> %.6f %s%n%n",
                        impact.name, r.getTotalImpactResult(impact), impact.referenceUnit);
                break;
            }
            else if (r.impactIndex().at(i).toString().contains("GWP")) {
                var impact =  r.impactIndex().at(i);
                System.out.printf("%n%s  -> %.6f %s%n%n",
                        impact.name, r.getTotalImpactResult(impact), impact.referenceUnit);
                break;
            }
            i++;
        }*/
    }

    public void flowResult(LcaResult r) {
        EnviFlow f = r.enviIndex().at(0);

        System.out.println(f.flow().name + "  -> " + r.getTotalFlowValueOf(f));
        var impact =  r.impactIndex().at(0);
        System.out.println(impact.name + "  -> " + r.getTotalImpactValueOf(impact));
    }

}
