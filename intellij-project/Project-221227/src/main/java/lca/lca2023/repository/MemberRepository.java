package lca.lca2023.repository;

import lca.lca2023.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(int id);
    List<Member> findAll();
}
