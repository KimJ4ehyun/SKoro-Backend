package skala.skoro.domain.evaluation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import skala.skoro.domain.common.BaseEntity;
import skala.skoro.domain.employee.entity.Team;
import skala.skoro.domain.period.entity.Period;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team_evaluations")
public class TeamEvaluation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_evaluation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TeamEvaluationStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "middle_report", columnDefinition = "JSON")
    private Map<String, Object> middleReport;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Map<String, Object> report;

    private Integer averageAchievementRate;

    private String relativePerformance;

    private Integer yearOverYearGrowth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private Period period;

    public static TeamEvaluation of(Team team, Period period, TeamEvaluationStatus status) {
        return TeamEvaluation.builder()
                .team(team)
                .period(period)
                .status(status)
                .build();
    }

    public void updateStatus(TeamEvaluationStatus status) {
        this.status = status;
    }
}
