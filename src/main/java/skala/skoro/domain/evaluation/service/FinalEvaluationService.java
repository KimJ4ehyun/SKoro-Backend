package skala.skoro.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.employee.service.EmployeeService;
import skala.skoro.domain.evaluation.dto.FinalEvaluationReportResponse;
import skala.skoro.domain.evaluation.entity.TeamEvaluation;
import skala.skoro.domain.evaluation.repository.FinalEvaluationRepository;
import skala.skoro.global.exception.CustomException;
import static skala.skoro.global.exception.ErrorCode.FINAL_EVALUATION_REPORT_DOES_NOT_EXIST;

@Service
@Transactional
@RequiredArgsConstructor
public class FinalEvaluationService {

    private final EmployeeService employeeService;

    private final FinalEvaluationRepository finalEvaluationRepository;

    private final TeamEvaluationService teamEvaluationService;

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
}
