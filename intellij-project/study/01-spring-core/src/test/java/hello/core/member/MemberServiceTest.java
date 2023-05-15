package hello.core.member;

import hello.core.AppConfig;
import hello.core.order.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService;
    OrderService orderService;

    @BeforeEach // 각 테스트 실행 전에 먼저 실행 ex) 테스트 3개 실행 -> 각 테스트마다 실행하므로 총 3번 실행됨
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
         memberService = appConfig.memberService();
         orderService = appConfig.orderService();
    }

    @Test
    void join(){
        // given
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        // then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
