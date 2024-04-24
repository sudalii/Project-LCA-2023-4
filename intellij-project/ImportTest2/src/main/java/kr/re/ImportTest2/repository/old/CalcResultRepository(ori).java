/*
package kr.re.ImportTest2.repository;

import jakarta.persistence.EntityManager;
import kr.re.ImportTest2.domain.CalcResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CalcResultRepository {

    private final EntityManager em;

    public void save(CalcResult calcResult) {
        em.persist(calcResult);
    }

    public CalcResult findOne(Long id) {
        return em.find(CalcResult.class, id);
    }

    public List<CalcResult> findAll() {
        return em.createQuery("select r from CalcResult r", CalcResult.class)
                .getResultList();
    }

}
*/
