package application.receipt;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Receipt {

    private final Integer paymentId;
    private final Integer memberId;
    private final Integer subscriptionId;

    private final BigDecimal originalAmount;
    private final BigDecimal discount;
    private final BigDecimal paidAmount;

    private final String paymentMethod;
    private final LocalDateTime paymentDate;

    public Receipt(
            Integer paymentId,
            Integer memberId,
            Integer subscriptionId,
            BigDecimal originalAmount,
            BigDecimal discount,
            BigDecimal paidAmount,
            String paymentMethod,
            LocalDateTime paymentDate
    ) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.subscriptionId = subscriptionId;
        this.originalAmount = originalAmount;
        this.discount = discount;
        this.paidAmount = paidAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public Integer getMemberId() {
        return memberId;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
}