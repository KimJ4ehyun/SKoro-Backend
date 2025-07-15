package skala.skoro.domain.kpi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skala.skoro.domain.employee.dto.EmployeeSummaryResponse;
import skala.skoro.domain.employee.repository.EmployeeRepository;
import skala.skoro.domain.evaluation.repository.NonFinalEvaluationRepository;
import skala.skoro.domain.evaluation.repository.FinalEvaluationRepository;
import skala.skoro.domain.kpi.dto.MyContributionResponse;
import skala.skoro.domain.kpi.dto.MyFinalScoreResponse;
import skala.skoro.domain.kpi.dto.MyTaskResponse;
import skala.skoro.domain.kpi.entity.Task;
import skala.skoro.domain.kpi.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberHomeService {

    private final TaskRepository taskRepository;

    private final NonFinalEvaluationRepository nonFinalEvaluationRepository;

    private final FinalEvaluationRepository finalEvaluationRepository;

    public List<MyTaskResponse> getMyTasks(String empNo) {
        int thisYear = LocalDate.now().getYear();
        List<Task> myTasks = taskRepository.findMyTasksThisYear(empNo, thisYear);

        return myTasks.stream().map(task -> {
            String kpiName = task.getTeamKpi().getKpiName();

            // 1. 같은 업무(TaskName + KPI)로 팀원 조회
            List<Task> members = taskRepository.findByTaskNameAndTeamKpi_Id(
                    task.getTaskName(), task.getTeamKpi().getId()
            );
            List<EmployeeSummaryResponse> memberDtos = members.stream().map(t -> {
                var emp = t.getEmployee();
                return EmployeeSummaryResponse.builder()
                        .empNo(emp.getEmpNo())
                        .empName(emp.getEmpName())
                        .profileImage(emp.getProfileImage())
                        .build();
            }).toList();

            return MyTaskResponse.builder()
                    .taskId(task.getId())
                    .taskName(task.getTaskName())
                    .targetLevel(task.getTargetLevel())
                    .kpiName(kpiName)
                    .startDate(task.getStartDate())
                    .endDate(task.getEndDate())
                    .teamMembers(memberDtos)
                    .build();
        }).toList();
    }

    public List<MyContributionResponse> getMyContributions(String empNo) {
        return nonFinalEvaluationRepository.findContributionByEmpNoGrouped(empNo);
    }

    public List<MyFinalScoreResponse> getMyFinalScores(String empNo) {
        return finalEvaluationRepository.findFinalScoreByEmpNoGrouped(empNo);
    }
}
