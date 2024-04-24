package kr.re.ImportTest2.domain.enumType;

import org.openlca.util.Strings;

public enum ProcessType {
    // 물질&부품제조
    RAW_MATERIALS,

    // 가공
    PROCESSING,

    // 수송
    TRANSPORTATION,

    // 사용
    USE,

    // 폐기&재활용
    END_OF_LIFE
    ;


    public static ProcessType of(String label) {
        if (Strings.nullOrEmpty(label))
            return null;
        return switch (label.trim().toLowerCase()) {
            case "물질&부품제조", "raw-materials", "p2" -> RAW_MATERIALS;
            case "가공", "processing", "p3" -> PROCESSING;
            case "수송", "transportation", "p4" -> TRANSPORTATION;
            case "사용", "use" -> USE;  // 구현 X
            case "재활용 및 폐기", "end-of-life", "p5" -> END_OF_LIFE;

            default -> null;
        };
    }

}


