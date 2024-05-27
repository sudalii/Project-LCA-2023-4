package kr.re.ImportTest2.component.result.resultTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record CategoryResultTable(String name, double value, String unit, List<ProcessResultTable> processResults) {

    // impactResult 값을 기준으로 내림차순 정렬
    public static List<CategoryResultTable> sortByImpactResultDescending(List<CategoryResultTable> CategoryResultTables) {
        List<CategoryResultTable> sortedList = new ArrayList<>(CategoryResultTables);
        // Sort the list using a comparator that compares the impactResult field in descending order
        sortedList.sort(Comparator.comparingDouble(CategoryResultTable::value).reversed());
        return sortedList;
    }
}
