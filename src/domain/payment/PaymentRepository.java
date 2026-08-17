package domain.payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Integer id);

    List<Payment> findByMemberId(Integer memberId);

    List<Payment> findByDate(LocalDate date);

    List<Payment> findAll();

    void update(Payment payment);
}