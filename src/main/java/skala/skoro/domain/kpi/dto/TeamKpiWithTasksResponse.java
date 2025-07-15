package skala.skoro.domain.kpi.dto;

import lombok.*;
import skala.skoro.domain.kpi.entity.Grade;
import skala.skoro.domain.kpi.entity.TeamKpi;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamKpiWithTasksResponse {
    private Long teamKpiId;
    private String kpiName;
    private String goal; // kpi_description
    private Integer achievementRate;
    private Integer weight;
    private GradeResponse grade;
    private List<TaskSummaryResponse> tasks;

    public static TeamKpiWithTasksResponse of(TeamKpi teamKpi, Grade grade, List<TaskSummaryResponse> tasks) {
        return TeamKpiWithTasksResponse.builder()
                .teamKpiId(teamKpi.getId())
                .kpiName(teamKpi.getKpiName())
                .goal(teamKpi.getKpiDescription())
                .achievementRate(teamKpi.getAchievementRate())
                .weight(teamKpi.getWeight())
                .grade(GradeResponse.from(grade))
                .tasks(tasks)
                .build();
    }
}
