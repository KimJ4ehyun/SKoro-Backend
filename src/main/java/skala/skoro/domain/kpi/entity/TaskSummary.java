package skala.skoro.domain.kpi.entity;

import jakarta.persistence.*;
import lombok.*;
import skala.skoro.domain.common.BaseEntity;
import skala.skoro.domain.period.entity.Period;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "task_summaries")
public class TaskSummary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_summary_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String taskSummary;

    @Column(name = "task_performance", columnDefinition = "TEXT")
    private String taskPerformance;

    @Column(name = "contribution_score")
    private Integer contributionScore;

    @Column(name = "achievement_rate")
    private Integer achievementRate;

    @Column(name = "assessed_grade")
    @Enumerated(EnumType.STRING)
    private GradeLevel assessedGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private Period period;
}
