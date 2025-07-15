package skala.skoro.domain.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import skala.skoro.domain.auth.dto.CustomUserDetails;
import skala.skoro.domain.evaluation.dto.EmployeeNonFinalEvaluationResponse;
import skala.skoro.domain.evaluation.dto.FeedbackReportResponse;
import skala.skoro.domain.evaluation.service.NonFinalEvaluationService;

import java.util.List;

@Tag(name = "최종이 아닌 평가(사원)")
@RestController
@RequiredArgsConstructor
public class NonFinalEvaluationController {

    private final NonFinalEvaluationService nonFinalEvaluationService;

    @Operation(summary = "[팀장] 해당 기간의 팀원의 평가 레포트 조회")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/employees/{empNo}/feedback-report/{periodId}")
    public ResponseEntity<FeedbackReportResponse> getTeamMemberFeedbackReport(
            @PathVariable("empNo") String empNo,
            @PathVariable("periodId") Long periodId) {
        return ResponseEntity.ok(nonFinalEvaluationService.getTeamMemberFeedbackReport(empNo, periodId));
    }

    @Operation(summary = "해당 기간의 본인의 평가 레포트 조회")
    @GetMapping("/feedback-report/{periodId}")
    public ResponseEntity<FeedbackReportResponse> getFeedbackReport(@PathVariable("periodId") Long periodId, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(nonFinalEvaluationService.getFeedbackReport(periodId, user.getUsername()));
    }

    @Operation(summary = "[팀장] 팀 관리 화면 - 분기 평가 카드 조회")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{periodId}/non-final")
    public ResponseEntity<List<EmployeeNonFinalEvaluationResponse>> getNonFinalEmployeeSummary(@PathVariable Long periodId, @AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(nonFinalEvaluationService.getNonFinalEmployeeEvaluationsByPeriod(periodId, user.getUsername()));
    }
}
