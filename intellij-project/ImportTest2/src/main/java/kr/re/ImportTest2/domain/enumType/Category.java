package kr.re.ImportTest2.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

    GWP("지구온난화", "Global warming (GWP100a)", "CML-IA baseline"),
    WU("물 사용", "Water use", "AWARE"),
    ARD("자원고갈", "Abiotic depletion", "CML-IA baseline"),
    HT("인체독성", "Human toxicity", "CML-IA baseline"),
    EP("부영양화", "Eutrophication", "CML-IA baseline");

    private final String name;
    private final String oriName;
//    private final String cf; // 기준물질 정확한 명칭 찾아서 변경
    private final String method;

/*    private final double resultValue;
    private final String refUnit;*/

    public String getCategoryOriName(String cgName) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(cgName)) {
                return category.getOriName();
            }
        }
        return null;
    }

}
