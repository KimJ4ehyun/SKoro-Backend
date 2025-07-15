package skala.skoro.domain.evaluation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skala.skoro.domain.evaluation.entity.FinalEvaluation;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FinalEvaluationReportResponse {
    private Long finalEvaluationReportId;
    private Map<String, Object> report;

    public static FinalEvaluationReportResponse from(FinalEvaluation finalEvaluation) {
        return new FinalEvaluationReportResponse(finalEvaluation.getId(), finalEvaluation.getReport());
    }
}
