package application.statistics;

import java.math.BigDecimal;

public class DashboardStatistics {

    private final int totalMembers;
    private final int activeMembers;
    private final int expiredMembers;
    private final int newMembersThisMonth;

    private final BigDecimal paymentsToday;
    private final BigDecimal paymentsThisMonth;

    private final int expiringThisWeek;
    private final int expiredSubscriptions;
    private final int activeSubscriptions;
    private final int renewals;

    public DashboardStatistics(
            int totalMembers,
            int activeMembers,
            int expiredMembers,
            int newMembersThisMonth,
            BigDecimal paymentsToday,
            BigDecimal paymentsThisMonth,
            int expiringThisWeek,
            int expiredSubscriptions,
            int activeSubscriptions,
            int renewals
    ) {
        this.totalMembers = totalMembers;
        this.activeMembers = activeMembers;
        this.expiredMembers = expiredMembers;
        this.newMembersThisMonth = newMembersThisMonth;
        this.paymentsToday = paymentsToday;
        this.paymentsThisMonth = paymentsThisMonth;
        this.expiringThisWeek = expiringThisWeek;
        this.expiredSubscriptions = expiredSubscriptions;
        this.activeSubscriptions = activeSubscriptions;
        this.renewals = renewals;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public int getActiveMembers() {
        return activeMembers;
    }

    public int getExpiredMembers() {
        return expiredMembers;
    }

    public int getNewMembersThisMonth() {
        return newMembersThisMonth;
    }

    public BigDecimal getPaymentsToday() {
        return paymentsToday;
    }

    public BigDecimal getPaymentsThisMonth() {
        return paymentsThisMonth;
    }

    public int getExpiringThisWeek() {
        return expiringThisWeek;
    }

    public int getExpiredSubscriptions() {
        return expiredSubscriptions;
    }

    public int getActiveSubscriptions() {
        return activeSubscriptions;
    }

    public int getRenewals() {
        return renewals;
    }
}