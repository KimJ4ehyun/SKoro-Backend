package skala.skoro.domain.kpi.dto;

import lombok.Builder;
import lombok.Getter;
import skala.skoro.domain.employee.dto.EmployeeSummaryResponse;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MyTaskResponse {
    private Long taskId;
    private String taskName;
    private String targetLevel;
    private String kpiName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<EmployeeSummaryResponse> teamMembers;

}
