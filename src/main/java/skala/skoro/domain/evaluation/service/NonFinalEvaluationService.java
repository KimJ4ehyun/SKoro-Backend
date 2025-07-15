package skala.skoro.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.evaluation.dto.EmployeeNonFinalEvaluationResponse;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.employee.entity.Team;
import skala.skoro.domain.employee.service.EmployeeService;
import skala.skoro.domain.evaluation.dto.FeedbackReportResponse;
import skala.skoro.domain.evaluation.entity.TeamEvaluation;
import skala.skoro.domain.evaluation.repository.NonFinalEvaluationRepository;
import skala.skoro.domain.evaluation.repository.TeamEvaluationRepository;
import skala.skoro.domain.period.entity.Period;
import skala.skoro.domain.period.repository.PeriodRepository;
import skala.skoro.global.exception.CustomException;

import java.util.List;

import static skala.skoro.global.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NonFinalEvaluationService {

    private final EmployeeService employeeService;

    private final TeamEvaluationService teamEvaluationService;

    private final NonFinalEvaluationRepository nonFinalEvaluationRepository;

    private final PeriodRepository periodRepository;

    private final TeamEvaluationRepository teamEvaluationRepository;

    @Transactional(readOnly = true)
    public FeedbackReportResponse getTeamMemberFeedbackReport(String empNo, Long periodId) {
        return getFeedbackReportInternal(empNo, periodId);
    }

    @Transactional(readOnly = true)
    public FeedbackReportResponse getFeedbackReport(Long periodId, String empNo) {
        return getFeedbackReportInternal(empNo, periodId);
    }

    private FeedbackReportResponse getFeedbackReportInternal(String empNo, Long periodId) {
        Employee employee = employeeService.findEmployeeByEmpNo(empNo);

        return nonFinalEvaluationRepository.findByTeamEvaluationAndEmployee(
                        teamEvaluationService.findTeamEvaluationByEmployeeAndPeriodId(employee, periodId), employee)
                .map(FeedbackReportResponse::from)
                .orElseThrow(() -> new CustomException(FEEDBACK_REPORT_DOES_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    public List<EmployeeNonFinalEvaluationResponse> getNonFinalEmployeeEvaluationsByPeriod(Long periodId, String empNo) {
        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new CustomException(PERIOD_DOES_NOT_EXIST));

        if (!Boolean.TRUE.equals(period.getIsFinal())) {
            throw new CustomException(INVALID_FINAL_EVALUATION_REQUEST);
        }

        Team team = employeeService.findEmployeeByEmpNo(empNo).getTeam();

        TeamEvaluation teamEvaluation = teamEvaluationRepository.findByTeamAndPeriodId(team, periodId)
                .orElseThrow(() -> new CustomException(TEAM_EVALUATION_DOES_NOT_EXIST));

        return nonFinalEvaluationRepository.findByTeamEvaluationIdOrderByRankingAsc(teamEvaluation.getId()).stream()
                .map(EmployeeNonFinalEvaluationResponse::from)
                .toList();
    }
}
