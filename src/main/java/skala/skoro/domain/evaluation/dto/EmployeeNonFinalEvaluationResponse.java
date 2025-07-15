package skala.skoro.domain.evaluation.dto;

import lombok.*;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.evaluation.entity.NonFinalEvaluation;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmployeeNonFinalEvaluationResponse {
    private String empNo;
    private String empName;
    private String profileImage;
    private String position;
    private Integer contributionRate;
    private Integer achievementRate;
    private String attitude;
    private Integer ranking;

    public static EmployeeNonFinalEvaluationResponse from(NonFinalEvaluation nonFinalEvaluation) {
        Employee employee = nonFinalEvaluation.getEmployee();
        return EmployeeNonFinalEvaluationResponse.builder()
                .empNo(employee.getEmpNo())
                .empName(employee.getEmpName())
                .profileImage(employee.getProfileImage())
                .position(employee.getPosition())
                .contributionRate(nonFinalEvaluation.getContributionRate())
                .achievementRate(nonFinalEvaluation.getAchievementRate())
                .attitude(nonFinalEvaluation.getAttitude())
                .ranking(nonFinalEvaluation.getRanking())
                .build();
    }
}
