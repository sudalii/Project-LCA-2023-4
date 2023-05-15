package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)   // ORDINAL: column이 숫자로 들어감. 단, 중간에 다른게 들어가면 망한다!
                                    // 때문에 꼭 STRING으로 넣어야 함.
    private DeliveryStatus status;  // READY, COMP

}
