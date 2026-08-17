package application.subscription;

import domain.subscription.Subscription;
import domain.subscription.SubscriptionRepository;

import java.time.LocalDate;

public class ExpireSubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;

    public ExpireSubscriptionsService(
            SubscriptionRepository subscriptionRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void expire(
            Integer subscriptionId
    ) {

        Subscription subscription =
                subscriptionRepository
                        .findById(subscriptionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found."
                                )
                        );

        if (subscription
                .getEndDate()
                .isBefore(LocalDate.now())) {

            subscription.expire();

            subscriptionRepository.update(
                    subscription
            );
        }
    }
}