package infrastructure.repository;

import domain.plan.AccessType;
import domain.plan.Plan;
import domain.plan.PlanRepository;
import domain.plan.PlanStatus;
import infrastructure.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlPlanRepository implements PlanRepository {

    @Override
    public Plan save(Plan plan) {

        String sql = """
                INSERT INTO plans
                (name, price, duration_days, access_type, status)
                VALUES (?, ?, ?, ?, ?)
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

            statement.setString(1, plan.getName());

            statement.setBigDecimal(
                    2,
                    plan.getPrice()
            );

            statement.setInt(
                    3,
                    plan.getDurationDays()
            );

            statement.setString(
                    4,
                    plan.getAccessType().name()
            );

            statement.setString(
                    5,
                    plan.getStatus().name()
            );

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {

                    int id = keys.getInt(1);

                    return new Plan(
                            id,
                            plan.getName(),
                            plan.getPrice(),
                            plan.getDurationDays(),
                            plan.getAccessType(),
                            plan.getStatus()
                    );
                }
            }

            throw new SQLException(
                    "Failed to retrieve generated plan ID."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error saving plan.",
                    e
            );
        }
    }

    @Override
    public Optional<Plan> findById(Integer id) {

        String sql = """
                SELECT plan_id,
                       name,
                       price,
                       duration_days,
                       access_type,
                       status
                FROM plans
                WHERE plan_id = ?
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
                            mapToPlan(resultSet)
                    );
                }
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding plan by ID.",
                    e
            );
        }
    }

    @Override
    public Optional<Plan> findByName(String name) {

        String sql = """
                SELECT plan_id,
                       name,
                       price,
                       duration_days,
                       access_type,
                       status
                FROM plans
                WHERE name = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, name);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return Optional.of(
                            mapToPlan(resultSet)
                    );
                }
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding plan by name.",
                    e
            );
        }
    }

    @Override
    public List<Plan> findAll() {

        String sql = """
                SELECT plan_id,
                       name,
                       price,
                       duration_days,
                       access_type,
                       status
                FROM plans
                ORDER BY plan_id
                """;

        List<Plan> plans = new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                plans.add(
                        mapToPlan(resultSet)
                );
            }

            return plans;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding all plans.",
                    e
            );
        }
    }

    @Override
    public void update(Plan plan) {

        String sql = """
                UPDATE plans
                SET name = ?,
                    price = ?,
                    duration_days = ?,
                    access_type = ?,
                    status = ?
                WHERE plan_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, plan.getName());

            statement.setBigDecimal(
                    2,
                    plan.getPrice()
            );

            statement.setInt(
                    3,
                    plan.getDurationDays()
            );

            statement.setString(
                    4,
                    plan.getAccessType().name()
            );

            statement.setString(
                    5,
                    plan.getStatus().name()
            );

            statement.setInt(
                    6,
                    plan.getId()
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {

                throw new RuntimeException(
                        "Plan not found with ID: "
                                + plan.getId()
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating plan.",
                    e
            );
        }
    }

    private Plan mapToPlan(ResultSet resultSet)
            throws SQLException {

        return new Plan(
                resultSet.getInt("plan_id"),

                resultSet.getString("name"),

                resultSet.getBigDecimal("price"),

                resultSet.getInt("duration_days"),

                AccessType.valueOf(
                        resultSet.getString("access_type")
                ),

                PlanStatus.valueOf(
                        resultSet.getString("status")
                )
        );
    }
}