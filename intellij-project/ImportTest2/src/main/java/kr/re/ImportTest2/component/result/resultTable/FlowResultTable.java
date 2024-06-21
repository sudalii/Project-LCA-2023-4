package kr.re.ImportTest2.component.result.resultTable;


import lombok.extern.slf4j.Slf4j;
import org.openlca.core.model.descriptors.ImpactDescriptor;
import org.openlca.core.results.EnviFlowValue;
import org.openlca.core.results.LcaResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 물질 분석 표를 위한 Table Record
 * @param name: flow name
 * @param lciResult: inventory result (공정양 * 해당 flow 양)
 * @param cf: 해당 impact category 와 해당 flow 의 characterization factor
 * @param impactResult: 각 flow 의 최종 impact assessment result
 */
@Slf4j
public record FlowResultTable(String name, double lciResult, double cf, double impactResult) {

    // impactResult 값을 기준으로 내림차순 정렬
    public static List<FlowResultTable> sortByImpactResultDescending(List<FlowResultTable> flowResultTables) {
        List<FlowResultTable> sortedList = new ArrayList<>(flowResultTables);
        // Sort the list using a comparator that compares the impactResult field in descending order
        sortedList.sort(Comparator.comparingDouble(FlowResultTable::impactResult).reversed());
        if (sortedList.size() > 3) {
            return sortedList.subList(0, 3);
        } else
            return sortedList;
    }

    public static List<EnviFlowValue> sortByEnviFlowValue(List<EnviFlowValue> flowImpactsOf) {
        List<EnviFlowValue> sortedList = new ArrayList<>(flowImpactsOf);
        // Sort the list using a comparator that compares the impactResult field in descending order
        sortedList.sort(Comparator.comparingDouble(EnviFlowValue::value).reversed());
        if (sortedList.size() > 3) {
            return sortedList.subList(0, 3);
        } else
            return sortedList;
    }

    public static List<FlowResultTable> convertToFlowTables(List<EnviFlowValue> flowImpactsOf, LcaResult r, ImpactDescriptor impact) {
        List<EnviFlowValue> enviFlowValues = FlowResultTable.sortByEnviFlowValue(flowImpactsOf);
        List<FlowResultTable> convertedList = new ArrayList<>();
        FlowResultTable fTable;

        for (EnviFlowValue efv : enviFlowValues) {
            double factorOf = r.getImpactFactorOf(impact, efv.enviFlow());
            fTable = new FlowResultTable(
                    efv.flow().name, 0, factorOf, efv.value()); // 전과정의 flow result 값이므로 lciResult 생략
            convertedList.add(fTable);
        }
        return convertedList;
    }

}
