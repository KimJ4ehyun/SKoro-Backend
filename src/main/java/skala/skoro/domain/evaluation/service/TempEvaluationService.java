package skala.skoro.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.employee.entity.Role;
import skala.skoro.domain.employee.entity.Team;
import skala.skoro.domain.employee.service.EmployeeService;
import skala.skoro.domain.evaluation.dto.TempEvaluationRequest;
import skala.skoro.domain.evaluation.dto.TempEvaluationResponse;
import skala.skoro.domain.evaluation.entity.TempEvaluation;
import skala.skoro.domain.evaluation.repository.TempEvaluationRepository;
import skala.skoro.global.exception.CustomException;
import java.util.List;
import java.util.stream.StreamSupport;

import static skala.skoro.global.exception.ErrorCode.TEMP_EVALUATION_NOT_EXISTS;

@Service
@Transactional
@RequiredArgsConstructor
public class TempEvaluationService {

    private final EmployeeService employeeService;

    private final TempEvaluationRepository tempEvaluationRepository;

    @Transactional(readOnly = true)
    public List<TempEvaluationResponse> getTeamTempEvaluations(String empNo) {
        Team team = employeeService.findEmployeeByEmpNo(empNo).getTeam();

        List<String> empNos = employeeService.findByTeam(team).stream()
                .filter(employee -> Role.MEMBER.equals(employee.getRole()))
                .map(Employee::getEmpNo)
                .toList();

        Iterable<TempEvaluation> tempEvaluations = tempEvaluationRepository.findAllById(empNos);

        return StreamSupport.stream(tempEvaluations.spliterator(), false)
                .map(TempEvaluationResponse::from)
                .toList();
    }

    public void updateTeamMemberTempEvaluations(String empNo, TempEvaluationRequest request) {
        TempEvaluation previous = tempEvaluationRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new CustomException(TEMP_EVALUATION_NOT_EXISTS));

        tempEvaluationRepository.save(TempEvaluation.of(empNo, request, previous));
    }
}
