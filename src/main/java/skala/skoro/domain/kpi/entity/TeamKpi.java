package skala.skoro.domain.kpi.entity;

import jakarta.persistence.*;
import lombok.*;
import skala.skoro.domain.common.BaseEntity;
import skala.skoro.domain.employee.entity.Team;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team_kpis")
public class TeamKpi extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_kpi_id")
    private Long id;

    private Integer year;

    private String kpiName;

    private String kpiDescription;

    private Integer weight;

    @Column(name = "achievement_rate")
    private Integer achievementRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}
