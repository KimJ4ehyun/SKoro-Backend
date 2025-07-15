package skala.skoro.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skala.skoro.domain.employee.entity.Employee;
import skala.skoro.domain.evaluation.dto.EvaluationFeedbackSaveRequest;
import skala.skoro.domain.evaluation.entity.EvaluationFeedback;
import skala.skoro.domain.evaluation.entity.TeamEvaluation;
import skala.skoro.domain.evaluation.repository.EvaluationFeedbackRepository;
import skala.skoro.domain.evaluation.repository.TeamEvaluationRepository;
import skala.skoro.domain.period.entity.Period;
import skala.skoro.domain.period.repository.PeriodRepository;

@Service
@RequiredArgsConstructor
public class EvaluationFeedbackService {

    private final EvaluationFeedbackRepository evaluationFeedbackRepository;
    private final TeamEvaluationRepository teamEvaluationRepository;
    private final PeriodRepository periodRepository;

    @Transactional
    public void saveFeedback(EvaluationFeedbackSaveRequest request, Employee employee) {
        TeamEvaluation teamEvaluation = teamEvaluationRepository.findByTeamAndPeriod_Id(employee.getTeam(), request.getPeriodId())
                .orElseThrow(() -> new IllegalArgumentException("팀 평가 정보가 없습니다."));
        Period period = periodRepository.findById(request.getPeriodId())
                .orElseThrow(() -> new IllegalArgumentException("기간 정보가 없습니다."));

        EvaluationFeedback feedback = EvaluationFeedback.builder()
                .content(request.getContent())
                .teamEvaluation(teamEvaluation)
                .build();
        evaluationFeedbackRepository.save(feedback);
    }
}
