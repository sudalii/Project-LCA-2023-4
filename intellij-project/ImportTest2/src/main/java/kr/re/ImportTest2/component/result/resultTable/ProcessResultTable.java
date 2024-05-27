package kr.re.ImportTest2.component.result.resultTable;

import java.util.List;

public record ProcessResultTable(String name, double value, List<FlowResultTable> flowResults) {
}
