package application.statistics;
import application.statistics.DashboardStatistics;
import domain.statistics.StatisticsRepository;

import java.math.BigDecimal;

public class GetDashboardStatisticsService {

    private final StatisticsRepository repository;

    public GetDashboardStatisticsService(
            StatisticsRepository repository
    ) {
        this.repository = repository;
    }

    public DashboardStatistics get() {

        return new DashboardStatistics(
                repository.totalMembers(),
                repository.activeMembers(),
                repository.expiredMembers(),
                repository.newMembersThisMonth(),
                repository.paymentsToday(),
                repository.paymentsThisMonth(),
                repository.expiringThisWeek(),
                repository.expiredSubscriptions(),
                repository.activeSubscriptions(),
                repository.renewals()
        );
    }
}