package skala.skoro.domain.evaluation.entity;


import jakarta.persistence.*;
import lombok.*;
import skala.skoro.domain.common.BaseEntity;

@Entity
@Table(name = "evaluation_feedbacks")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationFeedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_feedback_id")
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 팀원 채팅만 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_evaluation_id")
    private TeamEvaluation teamEvaluation;
}