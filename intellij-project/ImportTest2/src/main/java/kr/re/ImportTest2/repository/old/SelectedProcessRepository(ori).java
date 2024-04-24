/*
package kr.re.ImportTest2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import kr.re.ImportTest2.domain.ProcessType;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.UserFlows;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SelectedProcessRepository {

    private final EntityManager em;

    public void save(SelectedProcess selectedProcess) {
        UserFlows flows = selectedProcess.getFlows();
        em.persist(flows);
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

    */
/**
     * Type 별로 process name 리스트를 뽑아오기 위한 method
     * p2 = p2name1, p2name2, p2name3, ...
     * p3 = p3name1, p3name2, p3name4, ...
     * ...
     *//*

    public List<SelectedProcess> findAllByType(ProcessType type) {
        return em.createQuery(
                        "select sp from SelectedProcess sp " +
                                "where sp.type = :type",
                                SelectedProcess.class)
                .setParameter("type", type)
                .getResultList();

    }

    public List<SelectedProcess> findByProcessName(String processName) {
        return em.createQuery("select sp from SelectedProcess sp " +
                                "where sp.processName = :processName",
                        SelectedProcess.class)
                .setParameter("processName", processName)
                .getResultList();
    }

    public void delete(Long id) {
        SelectedProcess selectedProcess = findOne(id);
        em.remove(selectedProcess);
    }
}
*/
