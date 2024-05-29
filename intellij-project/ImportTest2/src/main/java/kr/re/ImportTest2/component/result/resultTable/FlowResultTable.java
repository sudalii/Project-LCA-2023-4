package kr.re.ImportTest2.component.result.resultTable;


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
/*
    public record flowResultTable(String name, double lciResult, double cf, double impactResult) {
    }


    public record flowResultTableString(String name, String lciResult, double cf, String impactResult) {
    }

    public static List<FlowResultTable> sort(List<FlowResultTable> tables) {
        // impactResult 값을 기준으로 내림차순 정렬
        List<FlowResultTable> sortedList = FlowResultTable.sortByImpactResultDescending(tables);

        // 정렬된 리스트의 impactResult 값을 formatDouble 메서드로 변환
        return FlowResultTable.formatImpactResults(sortedList);
    }

    public List<FlowResultTable> sortByImpactResultDescending(List<FlowResultTable> flowResultTables) {
        List<FlowResultTable> sortedList = new ArrayList<>(flowResultTables);
        // Sort the list using a comparator that compares the impactResult field in descending order
        sortedList.sort((flow1, flow2) -> {
            double impact1 = Double.parseDouble(flow1.impactResult());
            double impact2 = Double.parseDouble(flow2.impactResult());
            return Double.compare(impact2, impact1); // 내림차순
        });
        return sortedList;
    }



    // 소수점 4자리 초과 시 e상수 표기
    private String formatDouble(double value) {
        if (value >= 10) {
            DecimalFormat df = new DecimalFormat("0.####E0");
            return df.format(value);
        } else {
            return String.valueOf(value);
        }
    }

    // 정렬된 리스트의 impactResult 값을 formatDouble 메서드로 변환
    public List<FlowResultTable> formatImpactResults(List<FlowResultTable> sortedList) {
        List<FlowResultTable> formattedList = new ArrayList<>();
        for (FlowResultTable table : sortedList) {
            String formattedImpactResult = formatDouble(Double.parseDouble(table.impactResult()));
            formattedList.add(new FlowResultTable(table.name(), table.lciResult(), table.cf(), formattedImpactResult));
        }
        return formattedList;
    }
*/

}
