package infrastructure.repository;

import domain.statistics.StatisticsRepository;
import infrastructure.database.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlStatisticsRepository
        implements StatisticsRepository {

    @Override
    public int totalMembers() {

        String sql = """
                SELECT COUNT(*)
                FROM members
                """;

        return executeCount(sql);
    }

    @Override
    public int activeMembers() {

        String sql = """
                SELECT COUNT(*)
                FROM members
                WHERE status = 'ACTIVE'
                """;

        return executeCount(sql);
    }

    @Override
    public int expiredMembers() {

        String sql = """
                SELECT COUNT(*)
                FROM members
                WHERE status = 'INACTIVE'
                """;

        return executeCount(sql);
    }

    @Override
    public int newMembersThisMonth() {

        String sql = """
                SELECT COUNT(*)
                FROM members
                WHERE registration_date >=
                      DATE_FORMAT(CURRENT_DATE, '%Y-%m-01')
                """;

        return executeCount(sql);
    }

    @Override
    public BigDecimal paymentsToday() {

        String sql = """
                SELECT COALESCE(SUM(paid_amount), 0)
                FROM payments
                WHERE DATE(payment_date) = CURRENT_DATE
                  AND status = 'COMPLETED'
                """;

        return executeAmount(sql);
    }

    @Override
    public BigDecimal paymentsThisMonth() {

        String sql = """
                SELECT COALESCE(SUM(paid_amount), 0)
                FROM payments
                WHERE payment_date >=
                      DATE_FORMAT(CURRENT_DATE, '%Y-%m-01')
                  AND status = 'COMPLETED'
                """;

        return executeAmount(sql);
    }

    @Override
    public int expiringThisWeek() {

        String sql = """
                SELECT COUNT(*)
                FROM subscriptions
                WHERE status = 'ACTIVE'
                  AND end_date BETWEEN
                      CURRENT_DATE
                      AND DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY)
                """;

        return executeCount(sql);
    }

    @Override
    public int expiredSubscriptions() {

        String sql = """
                SELECT COUNT(*)
                FROM subscriptions
                WHERE end_date < CURRENT_DATE
                """;

        return executeCount(sql);
    }

    @Override
    public int activeSubscriptions() {

        String sql = """
                SELECT COUNT(*)
                FROM subscriptions
                WHERE status = 'ACTIVE'
                """;

        return executeCount(sql);
    }

    @Override
    public int renewals() {

        String sql = """
                SELECT COUNT(*)
                FROM subscriptions s
                WHERE EXISTS (
                    SELECT 1
                    FROM subscriptions previous
                    WHERE previous.member_id = s.member_id
                      AND previous.subscription_id <> s.subscription_id
                      AND previous.end_date < s.start_date
                )
                """;

        return executeCount(sql);
    }

    private int executeCount(String sql) {

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error executing statistics query.",
                    e
            );
        }
    }

    private BigDecimal executeAmount(String sql) {

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getBigDecimal(1);
            }

            return BigDecimal.ZERO;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error executing amount query.",
                    e
            );
        }
    }
}