package skala.skoro.domain.employee.dto;

import lombok.*;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.evaluation.entity.FinalEvaluation;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmployeeFinalEvaluationResponse {
    private String empNo;
    private String empName;
    private String profileImage;
    private String position;
    private Integer contributionRate;
    private Integer achievementRate;
    private Double score;
    private Integer ranking;

    public static EmployeeFinalEvaluationResponse from(FinalEvaluation finalEvaluation) {
        Employee employee = finalEvaluation.getEmployee();
        return EmployeeFinalEvaluationResponse.builder()
                .empNo(employee.getEmpNo())
                .empName(employee.getEmpName())
                .profileImage(employee.getProfileImage())
                .position(employee.getPosition())
                .contributionRate(finalEvaluation.getContributionRate())
                .achievementRate(finalEvaluation.getAchievementRate())
                .score(finalEvaluation.getScore())
                .ranking(finalEvaluation.getRanking())
                .build();
    }
}
