package domain.subscription;

import domain.plan.Plan;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Subscription {

    private final Integer id;
    private final Integer memberId;
    private final Integer planId;

    private final BigDecimal agreedPrice;

    private final LocalDate startDate;
    private final LocalDate endDate;

    private SubscriptionStatus status;

    public Subscription(
            Integer memberId,
            Integer planId,
            BigDecimal agreedPrice,
            LocalDate startDate,
            LocalDate endDate
    ) {

        validate(memberId, planId, agreedPrice, startDate, endDate);

        this.id = null;
        this.memberId = memberId;
        this.planId = planId;
        this.agreedPrice = agreedPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = SubscriptionStatus.ACTIVE;
    }

    public Subscription(
            Integer id,
            Integer memberId,
            Integer planId,
            BigDecimal agreedPrice,
            LocalDate startDate,
            LocalDate endDate,
            SubscriptionStatus status
    ) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }

        validate(memberId, planId, agreedPrice, startDate, endDate);

        if (status == null) {
            throw new IllegalArgumentException(
                    "Status cannot be null."
            );
        }

        this.id = id;
        this.memberId = memberId;
        this.planId = planId;
        this.agreedPrice = agreedPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    private void validate(
            Integer memberId,
            Integer planId,
            BigDecimal agreedPrice,
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException(
                    "Member ID must be positive."
            );
        }

        if (planId == null || planId <= 0) {
            throw new IllegalArgumentException(
                    "Plan ID must be positive."
            );
        }

        if (agreedPrice == null ||
                agreedPrice.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Agreed price cannot be negative."
            );
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Dates cannot be null."
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date."
            );
        }
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    public boolean isExpired() {
        return status == SubscriptionStatus.EXPIRED;
    }

    public Integer getId() {
        return id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public BigDecimal getAgreedPrice() {
        return agreedPrice;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }
}