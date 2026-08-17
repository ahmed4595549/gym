package domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private final Integer id;
    private final Integer subscriptionId;

    private final BigDecimal originalAmount;
    private final BigDecimal discount;
    private final BigDecimal paidAmount;

    private final PaymentMethod paymentMethod;
    private final LocalDateTime paymentDate;

    private PaymentStatus status;

    public Payment(
            Integer subscriptionId,
            BigDecimal originalAmount,
            BigDecimal discount,
            PaymentMethod paymentMethod
    ) {

        validate(
                subscriptionId,
                originalAmount,
                discount,
                paymentMethod
        );

        this.id = null;
        this.subscriptionId = subscriptionId;
        this.originalAmount = originalAmount;
        this.discount = discount;
        this.paidAmount = originalAmount.subtract(discount);
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.COMPLETED;
    }

    public Payment(
            Integer id,
            Integer subscriptionId,
            BigDecimal originalAmount,
            BigDecimal discount,
            BigDecimal paidAmount,
            PaymentMethod paymentMethod,
            LocalDateTime paymentDate,
            PaymentStatus status
    ) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.originalAmount = originalAmount;
        this.discount = discount;
        this.paidAmount = paidAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    private void validate(
            Integer subscriptionId,
            BigDecimal originalAmount,
            BigDecimal discount,
            PaymentMethod paymentMethod
    ) {

        if (subscriptionId == null || subscriptionId <= 0) {
            throw new IllegalArgumentException(
                    "Subscription ID must be positive."
            );
        }

        if (originalAmount == null ||
                originalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Original amount cannot be negative."
            );
        }

        if (discount == null ||
                discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Discount cannot be negative."
            );
        }

        if (discount.compareTo(originalAmount) > 0) {
            throw new IllegalArgumentException(
                    "Discount cannot exceed original amount."
            );
        }

        if (paymentMethod == null) {
            throw new IllegalArgumentException(
                    "Payment method cannot be null."
            );
        }
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}