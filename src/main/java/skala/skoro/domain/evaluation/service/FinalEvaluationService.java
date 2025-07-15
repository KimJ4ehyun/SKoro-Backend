package skala.skoro.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.evaluation.dto.EmployeeFinalEvaluationResponse;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.employee.entity.Team;
import skala.skoro.domain.employee.service.EmployeeService;
import skala.skoro.domain.evaluation.dto.FinalEvaluationReportResponse;
import skala.skoro.domain.evaluation.entity.TeamEvaluation;
import skala.skoro.domain.evaluation.repository.FinalEvaluationRepository;
import skala.skoro.domain.evaluation.repository.TeamEvaluationRepository;
import skala.skoro.domain.period.entity.Period;
import skala.skoro.domain.period.repository.PeriodRepository;
import skala.skoro.global.exception.CustomException;

import java.util.List;

import static skala.skoro.global.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FinalEvaluationService {

    private final EmployeeService employeeService;

    private final TeamEvaluationService teamEvaluationService;

    private final FinalEvaluationRepository finalEvaluationRepository;

    private final TeamEvaluationRepository teamEvaluationRepository;

    private final PeriodRepository periodRepository;

    @Transactional(readOnly = true)
    public FinalEvaluationReportResponse getTeamMemberFinalEvaluationReport(String empNo, Long periodId) {
        return getFinalEvaluationReportInternal(empNo, periodId);
    }

    @Transactional(readOnly = true)
    public FinalEvaluationReportResponse getFinalEvaluationReport(Long periodId, String empNo) {
        return getFinalEvaluationReportInternal(empNo, periodId);
    }

    private FinalEvaluationReportResponse getFinalEvaluationReportInternal(String empNo, Long periodId) {
        Employee employee = employeeService.findEmployeeByEmpNo(empNo);

        TeamEvaluation teamEvaluation = teamEvaluationService.findTeamEvaluationByEmployeeAndPeriodId(employee, periodId);

        return finalEvaluationRepository
                .findByTeamEvaluationAndEmployee(teamEvaluation, employee)
                .map(FinalEvaluationReportResponse::from)
                .orElseThrow(() -> new CustomException(FINAL_EVALUATION_REPORT_DOES_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    public List<EmployeeFinalEvaluationResponse> getFinalEmployeeEvaluationsByPeriod(Long periodId, String empNo) {
        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new CustomException(PERIOD_DOES_NOT_EXIST));

        if (!Boolean.TRUE.equals(period.getIsFinal())) {
            throw new CustomException(INVALID_FINAL_EVALUATION_REQUEST);
        }

        Team team = employeeService.findEmployeeByEmpNo(empNo).getTeam();

        TeamEvaluation teamEvaluation = teamEvaluationRepository.findByTeamAndPeriodId(team, periodId)
                .orElseThrow(() -> new CustomException(TEAM_EVALUATION_DOES_NOT_EXIST));

        return finalEvaluationRepository.findByTeamEvaluationIdOrderByRankingAsc(teamEvaluation.getId()).stream()
                .map(EmployeeFinalEvaluationResponse::from)
                .toList();
    }
}
