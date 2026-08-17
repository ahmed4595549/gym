package application.subscription;

import domain.member.Member;
import domain.member.MemberRepository;
import domain.plan.Plan;
import domain.plan.PlanRepository;
import domain.subscription.Subscription;
import domain.subscription.SubscriptionRepository;

import java.time.LocalDate;

public class SubscribeMemberService {

    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscribeMemberService(
            MemberRepository memberRepository,
            PlanRepository planRepository,
            SubscriptionRepository subscriptionRepository
    ) {
        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription subscribe(
            Integer memberId,
            Integer planId,
            LocalDate startDate
    ) {

        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Member not found."
                        )
                );

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

        return subscriptionRepository.save(
                subscription
        );
    }
}
