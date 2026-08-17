package application.plan;

import domain.plan.AccessType;
import domain.plan.Plan;
import domain.plan.PlanRepository;

import java.math.BigDecimal;

public class CreatePlanService {

    private final PlanRepository planRepository;

    public CreatePlanService(
            PlanRepository planRepository
    ) {
        this.planRepository = planRepository;
    }

    public Plan create(
            String name,
            BigDecimal price,
            int durationDays,
            AccessType accessType
    ) {

        Plan plan = new Plan(
                name,
                price,
                durationDays,
                accessType
        );

        return planRepository.save(plan);
    }
}