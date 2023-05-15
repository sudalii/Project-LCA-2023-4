package lca.lca2023.service;

import lca.lca2023.domain.Member;
import lca.lca2023.repository.JdbcTemplateMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final JdbcTemplateMemberRepository memberRepository;

    @Autowired
    public MemberService(JdbcTemplateMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
    // 이름 변경 필요
    public int join(Member member){

    }

    private void validateDuplicatedMember(Member member){
        memberRepository.findById()
    }
    */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }


}
