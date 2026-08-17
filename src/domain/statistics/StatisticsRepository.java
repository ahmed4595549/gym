package domain.statistics;

import java.math.BigDecimal;

public interface StatisticsRepository {

    int totalMembers();

    int activeMembers();

    int expiredMembers();

    int newMembersThisMonth();

    BigDecimal paymentsToday();

    BigDecimal paymentsThisMonth();

    int expiringThisWeek();

    int expiredSubscriptions();

    int activeSubscriptions();

    int renewals();
}
