package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "selected_process")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectedProcess {

    @Id @GeneratedValue
    @Column(name = "selected_process_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 없으면 국가 DB name을 default로 사용하기 (할 수 있다면...)
    private String processName;

    // 1. db name table에 저장 안하고 mapper method로 넘겨서 매핑된 Id를 내부에서 table에 저장
    // 2. 그냥 db name을 table에 저장하고 mapper로 넘겨서 매핑
    // 1번 방법 시도
    private Long mappedProcessId;

    private double processAmount;

    @Embedded
    private UserFlows flows;

    // default: false / 수송 Process일 경우 true
    private boolean isTransport;

    // 연관관계 Method
    public void setUser(User user) {
        this.user = user;
        user.getSelectedProcesses().add(this);
    }

    public void updateSelectedProcess(String processName, Long mappedProcessId, double processAmount) {
        this.processName = processName;
        this.mappedProcessId = mappedProcessId;
        this.processAmount = processAmount;

    }
}
