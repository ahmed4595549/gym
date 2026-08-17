package application.plan;

import domain.plan.Plan;
import domain.plan.PlanRepository;

public class DeactivatePlanService {

    private final PlanRepository planRepository;

    public DeactivatePlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public void deactivate(Integer planId) {

        Plan plan = planRepository
                .findById(planId)
                .orElseThrow(() ->
                        new RuntimeException("Plan not found.")
                );

        plan.deactivate();

        planRepository.update(plan);
    }
}
