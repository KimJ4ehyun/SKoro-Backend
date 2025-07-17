package skala.skoro.domain.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import skala.skoro.domain.auth.dto.CustomUserDetails;
import skala.skoro.domain.evaluation.dto.TempEvaluationRequest;
import skala.skoro.domain.evaluation.dto.TempEvaluationResponse;
import skala.skoro.domain.evaluation.service.TempEvaluationService;

import java.util.List;

@Tag(name = "임시 평가(중간 산출물)")
@RestController
@RequestMapping("/temp-evaluations")
@RequiredArgsConstructor
public class TempEvaluationController {

    private final TempEvaluationService tempEvaluationService;

    @Operation(summary = "[팀장] 팀원 임시 평가 조회")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<List<TempEvaluationResponse>> getTeamTempEvaluations(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(tempEvaluationService.getTeamTempEvaluations(user.getUsername()));
    }

    @Operation(summary = "[팀장] 해당 팀원 임시 평가 수정")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{empNo}")
    public ResponseEntity<Void> updateTeamMemberTempEvaluations(@PathVariable String empNo, @RequestBody TempEvaluationRequest request) {
        tempEvaluationService.updateTeamMemberTempEvaluations(empNo, request);
        return ResponseEntity.ok().build();
    }
}
