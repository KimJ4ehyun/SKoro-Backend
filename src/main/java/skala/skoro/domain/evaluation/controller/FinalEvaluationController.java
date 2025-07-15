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
import skala.skoro.domain.evaluation.dto.FinalEvaluationReportResponse;
import skala.skoro.domain.evaluation.service.FinalEvaluationService;

@Tag(name = "최종 평가(사원)")
@RestController
@RequiredArgsConstructor
public class FinalEvaluationController {

    private final FinalEvaluationService finalEvaluationService;

    @Operation(summary = "[팀장] 팀원 최종 평가 레포트 조회")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/employees/{empNo}/final-evaluation-report/{periodId}")
    public ResponseEntity<FinalEvaluationReportResponse> getTeamMemberFinalEvaluationReport(
            @PathVariable String empNo,
            @PathVariable Long periodId) {
        return ResponseEntity.ok(finalEvaluationService.getTeamMemberFinalEvaluationReport(empNo, periodId));
    }

    @Operation(summary = "해당 기간의 본인의 분기 평가 레포트 조회")
    @GetMapping("/final-evaluation-report/{periodId}")
    public ResponseEntity<FinalEvaluationReportResponse> getFinalEvaluationReport(@PathVariable Long periodId, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(finalEvaluationService.getFinalEvaluationReport(periodId, user.getUsername()));
    }
}
