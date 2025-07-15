package skala.skoro.domain.employee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.employee.dto.*;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.employee.entity.Role;
import skala.skoro.domain.employee.entity.Team;
import skala.skoro.domain.employee.repository.EmployeeRepository;
import skala.skoro.domain.evaluation.entity.TeamEvaluation;
import skala.skoro.domain.evaluation.entity.TempEvaluation;
import skala.skoro.domain.evaluation.repository.NonFinalEvaluationRepository;
import skala.skoro.domain.evaluation.repository.FinalEvaluationRepository;
import skala.skoro.domain.evaluation.repository.TeamEvaluationRepository;
import skala.skoro.domain.evaluation.repository.TempEvaluationRepository;
import skala.skoro.domain.period.repository.PeriodRepository;
import skala.skoro.global.exception.CustomException;
import java.util.List;

import static skala.skoro.global.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final PeriodRepository periodRepository;

    private final EmployeeRepository employeeRepository;

    private final TeamEvaluationRepository teamEvaluationRepository;

    private final FinalEvaluationRepository finalEvaluationRepository;

    private final NonFinalEvaluationRepository nonFinalEvaluationRepository;

    private final TempEvaluationRepository tempEvaluationRepository;

    @Transactional(readOnly = true)
    public List<EmployeeSummaryResponse> getEmployeesByTeam(String empNo) {
        Team team = findEmployeeByEmpNo(empNo).getTeam();

        return employeeRepository.findByTeam(team).stream()
                .map(EmployeeSummaryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryAndStatusResponse> getEmployeesAndStatusByTeam(Long teamEvaluationId, String empNo) {
        Team team = findEmployeeByEmpNo(empNo).getTeam();

        return employeeRepository.findByTeam(team).stream()
                .filter(employee -> Role.MEMBER.equals(employee.getRole()))
                .map(employee -> {
                    TempEvaluation tempEvaluation = tempEvaluationRepository.findByEmployeeAndTeamEvaluation_Id(employee, teamEvaluationId)
                            .orElseThrow(() -> new CustomException(TEMP_EVALUATION_NOT_EXISTS));
                    return EmployeeSummaryAndStatusResponse.of(employee, tempEvaluation);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeDetailResponse getEmployeeDetailByEmpNo(String empNo) {
        return EmployeeDetailResponse.from(employeeRepository.findByEmpNo(empNo));
    }

    public Employee findEmployeeByEmpNo(String empNo){
         return employeeRepository.findById(empNo)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public List<Employee> findByTeam(Team team) {
        return employeeRepository.findByTeam(team);
    }
}
