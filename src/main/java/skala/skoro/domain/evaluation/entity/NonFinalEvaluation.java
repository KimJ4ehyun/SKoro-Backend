package skala.skoro.domain.evaluation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import skala.skoro.domain.common.BaseEntity;
import skala.skoro.domain.employee.entity.Employee;

import java.util.Map;

@Entity
@Table(name = "non_final_evaluations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"emp_no", "team_evaluation_id"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NonFinalEvaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "non_final_evaluation_id")
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "report", columnDefinition = "JSON")
    private Map<String, Object> report;

    private Integer ranking;

    @Column(name = "contribution_rate")
    private Integer contributionRate;

    private String attitude;

    @Column(name = "achievement_rate")
    private Integer achievementRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_evaluation_id")
    private TeamEvaluation teamEvaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_no")
    private Employee employee;

    public static NonFinalEvaluation of(TeamEvaluation teamEvaluation, Employee employee) {
        return NonFinalEvaluation.builder()
                .teamEvaluation(teamEvaluation)
                .employee(employee)
                .build();
    }
}
