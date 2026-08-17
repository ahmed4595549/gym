package application.plan;

import domain.plan.Plan;
import domain.plan.PlanRepository;

import java.math.BigDecimal;

public class UpdatePlanPriceService {

    private final PlanRepository planRepository;

    public UpdatePlanPriceService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public void updatePrice(
            Integer planId,
            BigDecimal newPrice
    ) {

        Plan plan = planRepository
                .findById(planId)
                .orElseThrow(() ->
                        new RuntimeException("Plan not found.")
                );

        plan.changePrice(newPrice);

        planRepository.update(plan);
    }
}