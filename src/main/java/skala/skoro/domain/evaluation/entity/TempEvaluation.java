package skala.skoro.domain.evaluation.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import skala.skoro.domain.evaluation.dto.TempEvaluationRequest;
import java.io.Serializable;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("TempEvaluation") // Redis key TempEvaluation:{empNo}
public class TempEvaluation implements Serializable {
    @Id
    private String empNo;

    private String aiReason;

    private Double rawScore;

    private Double managerScore;

    private String comment;

    private String reason;

    @Builder.Default
    private Status status = Status.NOT_STARTED;

    public static TempEvaluation of(String empNo, TempEvaluationRequest request, TempEvaluation previousTempEvaluation){
        return TempEvaluation.builder()
                .empNo(empNo)
                .aiReason(previousTempEvaluation.getAiReason())
                .rawScore(previousTempEvaluation.getRawScore())
                .managerScore(request.getScore())
                .comment(request.getComment())
                .reason(request.getReason())
                .status(Status.COMPLETED)
                .build();
    }
}