package kr.re.ImportTest2.repository;

import kr.re.ImportTest2.domain.enumType.ProcessType;
import kr.re.ImportTest2.domain.SelectedProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelectedProcessRepository extends JpaRepository<SelectedProcess, Long> {

    List<SelectedProcess> findAllByType(ProcessType type);
}
