package skala.skoro.domain.evaluation.repository;

import org.springframework.data.repository.CrudRepository;
import skala.skoro.domain.evaluation.entity.TempEvaluation;
import java.util.Optional;

public interface TempEvaluationRepository extends CrudRepository<TempEvaluation, String> {
    Optional<TempEvaluation> findByEmpNo(String empNo);
}