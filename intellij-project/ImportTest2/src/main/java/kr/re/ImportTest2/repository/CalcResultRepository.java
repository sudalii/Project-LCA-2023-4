package kr.re.ImportTest2.repository;

import kr.re.ImportTest2.domain.CalcResult;
import kr.re.ImportTest2.domain.SelectedProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalcResultRepository extends JpaRepository<CalcResult, Long> {

    List<CalcResult> findAllByUserId(Long userId);
}
