package infrastructure.repository;

import domain.payment.*;
import infrastructure.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlPaymentRepository implements PaymentRepository {


    @Override
    public Payment save(Payment payment) {

        String sql = """
            INSERT INTO payments
            (
                subscription_id,
                original_amount,
                discount,
                paid_amount,
                payment_method,
                payment_date
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setInt(
                    1,
                    payment.getSubscriptionId()
            );

            statement.setBigDecimal(
                    2,
                    payment.getOriginalAmount()
            );

            statement.setBigDecimal(
                    3,
                    payment.getDiscount()
            );

            statement.setBigDecimal(
                    4,
                    payment.getPaidAmount()
            );

            statement.setString(
                    5,
                    payment.getPaymentMethod().name()
            );

            statement.setTimestamp(
                    6,
                    Timestamp.valueOf(
                            payment.getPaymentDate()
                    )
            );

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {

                    return new Payment(
                            keys.getInt(1),
                            payment.getSubscriptionId(),
                            payment.getOriginalAmount(),
                            payment.getDiscount(),
                            payment.getPaidAmount(),
                            payment.getPaymentMethod(),
                            payment.getPaymentDate(),
                            payment.getStatus()
                    );
                }
            }

            throw new SQLException(
                    "Failed to retrieve payment ID."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving payment.",
                    e
            );
        }
    }

    @Override
    public Optional<Payment> findById(Integer id) {

        String sql = """
                SELECT *
                FROM payments
                WHERE payment_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(
                            mapToPayment(resultSet)
                    );
                }
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding payment.",
                    e
            );
        }
    }

    @Override
    public List<Payment> findByMemberId(Integer memberId) {

        String sql = """
                SELECT *
                FROM payments
                WHERE member_id = ?
                ORDER BY payment_date DESC
                """;

        List<Payment> payments = new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    payments.add(
                            mapToPayment(resultSet)
                    );
                }
            }

            return payments;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding member payments.",
                    e
            );
        }
    }

    @Override
    public List<Payment> findByDate(LocalDate date) {

        String sql = """
                SELECT *
                FROM payments
                WHERE DATE(payment_date) = ?
                ORDER BY payment_date DESC
                """;

        List<Payment> payments = new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDate(
                    1,
                    Date.valueOf(date)
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    payments.add(
                            mapToPayment(resultSet)
                    );
                }
            }

            return payments;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding payments by date.",
                    e
            );
        }
    }

    @Override
    public List<Payment> findAll() {

        String sql = """
                SELECT *
                FROM payments
                ORDER BY payment_date DESC
                """;

        List<Payment> payments = new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                payments.add(
                        mapToPayment(resultSet)
                );
            }

            return payments;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding payments.",
                    e
            );
        }
    }

    @Override
    public void update(Payment payment) {

        String sql = """
                UPDATE payments
                SET status = ?
                WHERE payment_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    payment.getStatus().name()
            );

            statement.setInt(
                    2,
                    payment.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating payment.",
                    e
            );
        }
    }

    private Payment mapToPayment(
            ResultSet resultSet
    ) throws SQLException {

        return new Payment(
                resultSet.getInt("payment_id"),

                resultSet.getInt("subscription_id"),

                resultSet.getBigDecimal("original_amount"),

                resultSet.getBigDecimal("discount"),

                resultSet.getBigDecimal("paid_amount"),

                PaymentMethod.valueOf(
                        resultSet.getString("payment_method")
                ),

                resultSet.getTimestamp(
                        "payment_date"
                ).toLocalDateTime(),

                PaymentStatus.COMPLETED
        );
    }
    }
