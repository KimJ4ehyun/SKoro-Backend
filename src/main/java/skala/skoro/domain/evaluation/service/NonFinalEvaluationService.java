package skala.skoro.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.employee.service.EmployeeService;
import skala.skoro.domain.evaluation.dto.FeedbackReportResponse;
import skala.skoro.domain.evaluation.repository.NonFinalEvaluationRepository;
import skala.skoro.global.exception.CustomException;

import static skala.skoro.global.exception.ErrorCode.FEEDBACK_REPORT_DOES_NOT_EXIST;

@Service
@Transactional
@RequiredArgsConstructor
public class NonFinalEvaluationService {

    private final EmployeeService employeeService;

    private final TeamEvaluationService teamEvaluationService;

    private final NonFinalEvaluationRepository nonFinalEvaluationRepository;

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
}
