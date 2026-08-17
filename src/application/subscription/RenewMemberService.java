package application.subscription;

import domain.payment.Payment;
import domain.payment.PaymentMethod;
import domain.payment.PaymentRepository;
import domain.plan.Plan;
import domain.plan.PlanRepository;
import domain.subscription.Subscription;
import domain.subscription.SubscriptionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RenewMemberService {

    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    public RenewMemberService(
            PlanRepository planRepository,
            SubscriptionRepository subscriptionRepository,
            PaymentRepository paymentRepository
    ) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
    }

    public Subscription renew(
            Integer memberId,
            Integer planId,
            LocalDate startDate,
            BigDecimal discount,
            PaymentMethod paymentMethod
    ) {

        Plan plan = planRepository
                .findById(planId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Plan not found."
                        )
                );

        if (plan.getStatus() !=
                domain.plan.PlanStatus.ACTIVE) {

            throw new RuntimeException(
                    "Plan is inactive."
            );
        }

        LocalDate endDate =
                startDate.plusDays(
                        plan.getDurationDays() - 1
                );

        Subscription subscription =
                new Subscription(
                        memberId,
                        planId,
                        plan.getPrice(),
                        startDate,
                        endDate
                );

        Subscription savedSubscription =
                subscriptionRepository.save(
                        subscription
                );

        Payment payment =
                new Payment(
                        savedSubscription.getId(),
                        plan.getPrice(),
                        discount,
                        paymentMethod
                );

        paymentRepository.save(payment);

        return savedSubscription;
    }
}
