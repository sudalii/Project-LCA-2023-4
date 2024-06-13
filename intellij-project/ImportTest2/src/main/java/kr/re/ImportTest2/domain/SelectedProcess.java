package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "selected_process")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectedProcess {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "selected_process_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JoinColumn(name = "user_id", referencedColumnName = "`user_id`") // 백틱으로 감싸기
    private User user;

    // 없으면 국가 DB name을 default로 사용하기 (할 수 있다면...)
    @NotEmpty
    @Column(name="process_name", nullable = false, length = 50)
    private String processName;

    // 1. db name table에 저장 안하고 mapper method로 넘겨서 매핑된 Id를 내부에서 table에 저장
    // 2. 그냥 db name을 table에 저장하고 mapper로 넘겨서 매핑
    // 1번 방법 시도
    private String mappedProcessId;

    private Double processAmount;
    private String processAmountUnit;

    @Embedded
    private UserFlows flows;

    // enum default type: EnumType.ORDINAL -> DB에 index로 저장됨
    @Enumerated(EnumType.STRING)
    private ProcessType type;

    // 연관관계 Method
/*    public void setUser(User user) {
        this.user = user;
        user.getSelectedProcesses().add(this);
    }*/

    @Builder
    public SelectedProcess(Long id, User user, String processName, String mappedProcessId, Double processAmount, String processAmountUnit, UserFlows flows, ProcessType type) {
        this.id = id;
        this.processName = processName;
        this.mappedProcessId = mappedProcessId;
        this.processAmount = processAmount;
        this.processAmountUnit = processAmountUnit;
        this.flows = flows;
        this.type = type;

        // 연관관계 설정
        this.user = user;
        user.getSelectedProcesses().add(this);
    }
}
