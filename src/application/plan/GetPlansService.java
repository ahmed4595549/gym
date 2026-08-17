package application.plan;

import domain.plan.Plan;
import domain.plan.PlanRepository;

import java.util.List;

public class GetPlansService {

    private final PlanRepository planRepository;

    public GetPlansService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> getAll() {
        return planRepository.findAll();
    }
}