package skala.skoro.domain.evaluation.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TempEvaluationRequest {
    private Double score;
    private String comment;
    private String reason;
}
