package kr.re.ImportTest2.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LciDb {

    // p2, 물질&부품제조
    PVC("폴리비닐클로라이드-PVC", "84d402297a1f487c81cb3c624b33f8e7", "p2"), // PVC_Poly_Vinyl_Chloride_KR
    HPP("호모폴리프로필렌-HPP", "e512cb0087de43388f1285c30f2bd7a6", "p2"), // HPP_Homo_Poly_Propylene_KR
    HDPE("고밀도폴리에틸렌-HDPE", "08dc37f5aa3844348f5f774791fb2573", "p2"), // HDPE_High_Density_Polyethylene_KR

    // p3, 가공
    컴파운딩PP압출("컴파운딩PP압출", "5ad9390eb108434b9f4ad14f0a014219", "p3"), // Compounding_pp_extrusion_KR
    PVC사출성형("PVC사출성형", "f0369c5663784b01bede3f8daa4da568", "p3"), // plastics_injection_molding_PVC_KR

    // p4, 수송
    수송("공로수송", "be291b6aca084e80b683064f198925bd", "p4"), // Transportation_KR

    // p5, 재활용 및 폐기
    매립("혼합플라스틱매립", "cc26ff7ce8c0447a9c810f9ce36c7ec4", "p5"), // mixed_plastic_landfill_KR
    소각("폐플라스틱소각", "eedf68a25a794397a1a36396641cd6bb", "p5"), // Mixed_plastics_incineration_KR
    재활용("펠렛용폐플라스틱재활용", "6660103878de4f7f9c16097572ee7cf0", "p5"); // Recycling_Pellet_Waste_Plastic_KR


    private final String name;
    private final String Id;
    private final String type;

    /**
     * 사용자가 화면에서 각 공정에서 국가 DB를 선택 클릭하면,
     * 그 클릭된 db name 과 db id를 매칭해서 return 한다.
     *
     * - Controller에서 Get으로 데이터 보여줄 때 사용
     */

    public static List<LciDb> getValuesByType(String type) {
        return Stream.of(LciDb.values())
                .filter(db -> db.getType().equals(type))
                .collect(Collectors.toList());
    }
}
