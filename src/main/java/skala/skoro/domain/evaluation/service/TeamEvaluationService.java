package skala.skoro.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.employee.entity.Role;
import skala.skoro.domain.employee.repository.TeamRepository;
import skala.skoro.domain.employee.service.EmployeeService;
import skala.skoro.domain.evaluation.dto.*;
import skala.skoro.domain.evaluation.entity.*;
import skala.skoro.domain.evaluation.repository.NonFinalEvaluationRepository;
import skala.skoro.domain.evaluation.repository.FinalEvaluationRepository;
import skala.skoro.domain.evaluation.repository.TeamEvaluationRepository;
import skala.skoro.domain.evaluation.repository.TempEvaluationRepository;
import skala.skoro.domain.period.entity.Period;
import skala.skoro.domain.period.repository.PeriodRepository;
import skala.skoro.global.exception.CustomException;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

import static skala.skoro.global.exception.ErrorCode.INCOMPLETE_DOWNWARD_EVALUATIONS;
import static skala.skoro.global.exception.ErrorCode.TEAM_EVALUATION_DOES_NOT_EXIST;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamEvaluationService {

    private final EmployeeService employeeService;

    private final TeamEvaluationRepository teamEvaluationRepository;

    private final PeriodRepository periodRepository;

    private final TeamRepository teamRepository;

    private final FinalEvaluationRepository finalEvaluationRepository;

    private final NonFinalEvaluationRepository nonFinalEvaluationRepository;

    private final TempEvaluationRepository tempEvaluationRepository;

    @Transactional(readOnly = true)
    public List<TeamEvaluationDetailResponse> findTeamEvaluationsByYear(String empNo) {
        Employee employee = employeeService.findEmployeeByEmpNo(empNo);

        return periodRepository.findByYearOrderByOrderInYearDesc(LocalDate.now().getYear()).stream()
                .map(period -> teamEvaluationRepository.findByTeamAndPeriod(employee.getTeam(), period)
                        .map(TeamEvaluationDetailResponse::from))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamEvaluationReportResponse getTeamEvaluationReportByPeriodId(Long periodId, String empNo) {
        Employee employee = employeeService.findEmployeeByEmpNo(empNo);

        return TeamEvaluationReportResponse.from(findTeamEvaluationByEmployeeAndPeriodId(employee, periodId));
    }

    @Transactional(readOnly = true)
    public TeamEvaluationReportResponse getTeamMiddleReportByPeriodId(Long periodId, String empNo) {
        Employee employee = employeeService.findEmployeeByEmpNo(empNo);

        return TeamEvaluationReportResponse.fromMiddle(findTeamEvaluationByEmployeeAndPeriodId(employee, periodId));
    }

    public TeamEvaluation findTeamEvaluationByEmployeeAndPeriodId(Employee employee, Long periodId) {
        return teamEvaluationRepository.findByTeamAndPeriodId(employee.getTeam(), periodId)
                .orElseThrow(() -> new CustomException(TEAM_EVALUATION_DOES_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    public List<FinalEvaluationAchievementStatsResponse> getFinalTeamAndAllAverageAchievementRate(String empNo) {
        Employee employee = employeeService.findEmployeeByEmpNo(empNo);

        return teamEvaluationRepository.findTeamAndAllAverageByYear(employee.getTeam().getId()).stream()
                .map(FinalEvaluationAchievementStatsResponse::from)
                .toList();
    }

    public void submitEvaluation(Long teamEvaluationId) {
        TeamEvaluation teamEvaluation = teamEvaluationRepository.findById(teamEvaluationId)
                .orElseThrow(() -> new CustomException(TEAM_EVALUATION_DOES_NOT_EXIST));

        boolean hasUncompleted = tempEvaluationRepository.existsByTeamEvaluationAndStatusNot(teamEvaluation, Status.COMPLETED);

        if (hasUncompleted) {
            throw new CustomException(INCOMPLETE_DOWNWARD_EVALUATIONS);
        }

        teamEvaluation.updateStatus(TeamEvaluationStatus.SUBMITTED);
    }

    @Transactional(readOnly = true)
    public List<TeamEvaluationStatusResponse> getTeamEvaluationStatus(String empNo) {
        Employee employee = employeeService.findEmployeeByEmpNo(empNo);

        List<TeamEvaluationStatus> excluded = List.of(
                TeamEvaluationStatus.SUBMITTED,
                TeamEvaluationStatus.COMPLETED
        );

        return teamEvaluationRepository.findByTeamAndStatusNotIn(employee.getTeam(), excluded).stream()
                .map(TeamEvaluationStatusResponse::from)
                .toList();
    }

    public void createAllTeamEvaluations(Period period) {
        teamRepository.findAll()
                .forEach(team -> {
                    TeamEvaluation teamEvaluation = teamEvaluationRepository.save(
                            TeamEvaluation.of(team, period, TeamEvaluationStatus.NOT_STARTED)
                    );

                    List<Employee> members = employeeService.findByTeam(team).stream()
                            .filter(employee -> Role.MEMBER.equals(employee.getRole()))
                            .toList();

                    if (period.getIsFinal()) {
                        members.forEach(employee ->
                                finalEvaluationRepository.save(FinalEvaluation.of(teamEvaluation, employee))
                        );
                    } else {
                        members.forEach(employee ->
                                nonFinalEvaluationRepository.save(NonFinalEvaluation.of(teamEvaluation, employee))
                        );
                    }
                });
    }

    @Transactional(readOnly = true)
    public Boolean isAllManagerEvaluationSubmitted(Long periodId) {
        return teamEvaluationRepository.findByPeriod_Id(periodId).stream()
                .allMatch(teamEvaluation ->  TeamEvaluationStatus.SUBMITTED.equals(teamEvaluation.getStatus()));
    }
}
