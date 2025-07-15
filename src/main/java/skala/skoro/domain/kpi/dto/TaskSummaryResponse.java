package skala.skoro.domain.kpi.dto;

import lombok.*;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.kpi.entity.Grade;
import skala.skoro.domain.kpi.entity.Task;
import skala.skoro.domain.kpi.entity.TaskSummary;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TaskSummaryResponse {
    private Long taskId;
    private String taskName;
    private String taskDetail;
    private String goal; // targetLevel
    private Integer weight;
    private LocalDate startDate;
    private LocalDate endDate;
    private EmployeeSimple employee;
    private GradeResponse grade;
    private Integer achievementRate;
    private Integer contributionScore;

    public static TaskSummaryResponse of(Task task, Employee employee, TaskSummary taskSummary, Grade grade) {
        return TaskSummaryResponse.builder()
                .taskId(task.getId())
                .taskName(task.getTaskName())
                .taskDetail(task.getTaskDetail())
                .goal(task.getTargetLevel())
                .weight(task.getWeight())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .employee(EmployeeSimple.from(employee))
                .grade(GradeResponse.from(grade))
                .achievementRate(taskSummary != null ? taskSummary.getAchievementRate() : null)
                .contributionScore(taskSummary != null ? taskSummary.getContributionScore() : null)
                .build();
    }
}
