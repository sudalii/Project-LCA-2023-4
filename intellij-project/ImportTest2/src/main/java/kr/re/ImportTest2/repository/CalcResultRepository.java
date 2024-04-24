package kr.re.ImportTest2.repository;

import kr.re.ImportTest2.domain.CalcResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalcResultRepository extends JpaRepository<CalcResult, Long> {

}
