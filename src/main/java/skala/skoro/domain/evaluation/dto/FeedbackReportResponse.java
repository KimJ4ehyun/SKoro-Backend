package skala.skoro.domain.evaluation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skala.skoro.domain.evaluation.entity.NonFinalEvaluation;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedbackReportResponse {
    private Long feedbackReportId;
    private Map<String, Object> report;

    public static FeedbackReportResponse from(NonFinalEvaluation nonFinalEvaluation) {
        return new FeedbackReportResponse(nonFinalEvaluation.getId(), nonFinalEvaluation.getReport());
    }
}
