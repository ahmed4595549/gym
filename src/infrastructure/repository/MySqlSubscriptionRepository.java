package infrastructure.repository;

import domain.subscription.Subscription;
import domain.subscription.SubscriptionRepository;
import domain.subscription.SubscriptionStatus;
import infrastructure.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlSubscriptionRepository
        implements SubscriptionRepository {

    @Override
    public Subscription save(Subscription subscription) {

        String sql = """
                INSERT INTO subscriptions
                (
                    member_id,
                    plan_id,
                    agreed_price,
                    start_date,
                    end_date,
                    status
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
                    subscription.getMemberId()
            );

            statement.setInt(
                    2,
                    subscription.getPlanId()
            );

            statement.setBigDecimal(
                    3,
                    subscription.getAgreedPrice()
            );

            statement.setDate(
                    4,
                    Date.valueOf(
                            subscription.getStartDate()
                    )
            );

            statement.setDate(
                    5,
                    Date.valueOf(
                            subscription.getEndDate()
                    )
            );

            statement.setString(
                    6,
                    subscription.getStatus().name()
            );

            statement.executeUpdate();

            try (
                    ResultSet keys =
                            statement.getGeneratedKeys()
            ) {

                if (keys.next()) {

                    return new Subscription(
                            keys.getInt(1),
                            subscription.getMemberId(),
                            subscription.getPlanId(),
                            subscription.getAgreedPrice(),
                            subscription.getStartDate(),
                            subscription.getEndDate(),
                            subscription.getStatus()
                    );
                }
            }

            throw new SQLException(
                    "Failed to retrieve generated subscription ID."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving subscription.",
                    e
            );
        }
    }

    @Override
    public Optional<Subscription> findById(Integer id) {

        String sql = """
                SELECT
                    subscription_id,
                    member_id,
                    plan_id,
                    agreed_price,
                    start_date,
                    end_date,
                    status
                FROM subscriptions
                WHERE subscription_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return Optional.of(
                            mapToSubscription(resultSet)
                    );
                }
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding subscription.",
                    e
            );
        }
    }

    @Override
    public List<Subscription> findByMemberId(
            Integer memberId
    ) {

        String sql = """
                SELECT
                    subscription_id,
                    member_id,
                    plan_id,
                    agreed_price,
                    start_date,
                    end_date,
                    status
                FROM subscriptions
                WHERE member_id = ?
                ORDER BY start_date DESC
                """;

        List<Subscription> subscriptions =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, memberId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    subscriptions.add(
                            mapToSubscription(resultSet)
                    );
                }
            }

            return subscriptions;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding member subscriptions.",
                    e
            );
        }
    }

    @Override
    public Optional<Subscription> findActiveByMemberId(
            Integer memberId
    ) {

        String sql = """
                SELECT
                    subscription_id,
                    member_id,
                    plan_id,
                    agreed_price,
                    start_date,
                    end_date,
                    status
                FROM subscriptions
                WHERE member_id = ?
                  AND status = 'ACTIVE'
                ORDER BY end_date DESC
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, memberId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return Optional.of(
                            mapToSubscription(resultSet)
                    );
                }
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding active subscription.",
                    e
            );
        }
    }

    @Override
    public List<Subscription> findAll() {

        String sql = """
                SELECT
                    subscription_id,
                    member_id,
                    plan_id,
                    agreed_price,
                    start_date,
                    end_date,
                    status
                FROM subscriptions
                ORDER BY subscription_id
                """;

        List<Subscription> subscriptions =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                subscriptions.add(
                        mapToSubscription(resultSet)
                );
            }

            return subscriptions;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding subscriptions.",
                    e
            );
        }
    }

    @Override
    public void update(Subscription subscription) {

        String sql = """
                UPDATE subscriptions
                SET status = ?
                WHERE subscription_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    subscription.getStatus().name()
            );

            statement.setInt(
                    2,
                    subscription.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating subscription.",
                    e
            );
        }
    }

    private Subscription mapToSubscription(
            ResultSet resultSet
    ) throws SQLException {

        return new Subscription(
                resultSet.getInt("subscription_id"),
                resultSet.getInt("member_id"),
                resultSet.getInt("plan_id"),
                resultSet.getBigDecimal("agreed_price"),
                resultSet.getDate(
                        "start_date"
                ).toLocalDate(),
                resultSet.getDate(
                        "end_date"
                ).toLocalDate(),
                SubscriptionStatus.valueOf(
                        resultSet.getString("status")
                )
        );
    }
}