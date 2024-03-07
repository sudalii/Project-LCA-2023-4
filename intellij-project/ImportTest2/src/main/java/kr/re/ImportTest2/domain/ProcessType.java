package kr.re.ImportTest2.domain;

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
            case "raw-materials" -> RAW_MATERIALS;
            case "processing" -> PROCESSING;
            case "transportation" -> TRANSPORTATION;
            case "use" -> USE;
            case "end-of-life" -> END_OF_LIFE;

            default -> null;
        };
    }

}


