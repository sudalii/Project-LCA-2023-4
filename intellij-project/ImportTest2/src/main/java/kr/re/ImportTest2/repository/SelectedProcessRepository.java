package kr.re.ImportTest2.repository;

import jakarta.persistence.EntityManager;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SelectedProcessRepository {

    private final EntityManager em;

    public void save(SelectedProcess selectedProcess) {
        em.persist(selectedProcess);
    }

    public SelectedProcess findOne(Long id) {
        return em.find(SelectedProcess.class, id);
    }

    public List<SelectedProcess> findAll() {
        return em.createQuery("select sp from SelectedProcess sp",
                        SelectedProcess.class)
                .getResultList();
    }

    public List<SelectedProcess> findByProcessName(String processName) {
        return em.createQuery("select sp from SelectedProcess sp " +
                                "where sp.processName = :processName",
                        SelectedProcess.class)
                .setParameter("processName", processName)
                .getResultList();
    }
}
