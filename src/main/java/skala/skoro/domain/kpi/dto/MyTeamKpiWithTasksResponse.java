package skala.skoro.domain.kpi.dto;

import lombok.*;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.kpi.entity.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MyTeamKpiWithTasksResponse {
    private Long teamKpiId;
    private String teamKpiName;
    private String teamKpiGoal; // kpi_description
    private Integer teamKpiAchievementRate;
    private Integer teamKpiWeight;
    private GradeResponse TeamKpiGrade;

    private Long taskId;
    private String taskName;
    private String taskDetail;
    private String taskGoal; // targetLevel
    private Integer taskWeight;
    private LocalDate taskStartDate;
    private LocalDate taskEndDate;
    private EmployeeSimple employee;
    private GradeResponse taskGrade;
    private Integer taskAchievementRate;
    private Integer taskContributionScore;
    private GradeLevel assessedGrade;

    private List<EmployeeSimple> participants;

    public static MyTeamKpiWithTasksResponse of(TeamKpi teamKpi, Grade teamKpiGrade, Task task, Employee employee, TaskSummary taskSummary, Grade taskGrade, List<EmployeeSimple> participants) {
        return MyTeamKpiWithTasksResponse.builder()
                .teamKpiId(teamKpi.getId())
                .teamKpiName(teamKpi.getKpiName())
                .teamKpiGoal(teamKpi.getKpiDescription())
                .teamKpiAchievementRate(teamKpi.getAchievementRate())
                .teamKpiWeight(teamKpi.getWeight())
                .TeamKpiGrade(GradeResponse.from(teamKpiGrade))
                .taskId(task.getId())
                .taskName(task.getTaskName())
                .taskDetail(task.getTaskDetail())
                .taskGoal(task.getTargetLevel())
                .taskWeight(task.getWeight())
                .taskStartDate(task.getStartDate())
                .taskEndDate(task.getEndDate())
                .employee(EmployeeSimple.from(employee))
                .taskGrade(GradeResponse.from(taskGrade))
                .taskAchievementRate(taskSummary != null ? taskSummary.getAchievementRate() : null)
                .taskContributionScore(taskSummary != null ? taskSummary.getContributionScore() : null)
                .assessedGrade(taskSummary != null ? taskSummary.getAssessedGrade() : null)
                .participants(participants)
                .build();
    }
}

