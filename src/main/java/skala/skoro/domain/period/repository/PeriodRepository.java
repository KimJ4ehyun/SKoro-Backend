package skala.skoro.domain.period.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import skala.skoro.domain.period.entity.Period;
import skala.skoro.domain.period.entity.Unit;
import java.util.List;
import java.util.Optional;

public interface PeriodRepository extends JpaRepository<Period, Long> {
    // 연도와 Unit으로 가장 최근 orderInYear 조회
    Optional<Period> findTopByYearAndUnitOrderByOrderInYearDesc(Integer year, Unit unit);

    // 종료되지 않은 평가 기간 조회
    @Query("""
        SELECT p
        FROM Period p
        WHERE p.periodPhase != 'COMPLETED'
        ORDER BY p.startDate ASC
        """)
    List<Period> findAllNotCompleted();

    // 사번으로 해당 팀의 팀 평가가 있었던 기간 조회
    @Query("""
        SELECT te.period
        FROM Employee e
        JOIN e.team t
        JOIN TeamEvaluation te ON te.team = t
        WHERE e.empNo = :empNo
        ORDER BY te.period.year DESC, te.period.orderInYear ASC
    """)
    List<Period> findPeriodsByEmpNo(@Param("empNo") String empNo);

    // 사번으로 해당 사원의 평가가 있었던 기간 조회
    @Query("""
        SELECT te.period
        FROM Employee e
        JOIN e.team t
        JOIN TeamEvaluation te ON te.team = t
        WHERE e.empNo = :empNo
          AND (
                (te.period.isFinal = true AND EXISTS (
                    SELECT 1 FROM FinalEvaluation fer
                    WHERE fer.teamEvaluation = te AND fer.employee.empNo = :empNo
                ))
             OR
                (te.period.isFinal = false AND EXISTS (
                    SELECT 1 FROM NonFinalEvaluation fr
                    WHERE fr.teamEvaluation = te AND fr.employee.empNo = :empNo
                ))
          )
        ORDER BY te.period.year DESC, te.period.orderInYear ASC
    """)
    List<Period> findMemberPeriodsByEmpNo(@Param("empNo") String empNo);

    List<Period> findByYearOrderByOrderInYearDesc(int year);
}
