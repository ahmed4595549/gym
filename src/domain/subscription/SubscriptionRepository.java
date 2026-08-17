package domain.subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(Integer id);

    List<Subscription> findByMemberId(Integer memberId);

    Optional<Subscription> findActiveByMemberId(Integer memberId);

    List<Subscription> findAll();

    void update(Subscription subscription);
}