package application.payment;

import domain.payment.Payment;
import domain.payment.PaymentMethod;
import domain.payment.PaymentRepository;
import domain.subscription.Subscription;
import domain.subscription.SubscriptionRepository;

import java.math.BigDecimal;

public class RecordPaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    public RecordPaymentService(
            PaymentRepository paymentRepository,
            SubscriptionRepository subscriptionRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Payment pay(
            Integer subscriptionId,
            BigDecimal amount,
            BigDecimal discount,
            PaymentMethod paymentMethod
    ) {

        Subscription subscription =
                subscriptionRepository
                        .findById(subscriptionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found."
                                )
                        );

        Payment payment = new Payment(
                subscription.getId(),
                amount,
                discount,
                paymentMethod
        );

        return paymentRepository.save(payment);
    }
}
