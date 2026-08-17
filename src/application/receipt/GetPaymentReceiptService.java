package application.receipt;

import domain.payment.Payment;
import domain.payment.PaymentRepository;
import domain.subscription.Subscription;
import domain.subscription.SubscriptionRepository;

public class GetPaymentReceiptService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    public GetPaymentReceiptService(
            PaymentRepository paymentRepository,
            SubscriptionRepository subscriptionRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Receipt get(Integer paymentId) {

        Payment payment =
                paymentRepository
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found."
                                )
                        );

        Subscription subscription =
                subscriptionRepository
                        .findById(
                                payment.getSubscriptionId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found."
                                )
                        );

        return new Receipt(
                payment.getId(),
                subscription.getMemberId(),
                subscription.getId(),
                payment.getOriginalAmount(),
                payment.getDiscount(),
                payment.getPaidAmount(),
                payment.getPaymentMethod().name(),
                payment.getPaymentDate()
        );
    }
}