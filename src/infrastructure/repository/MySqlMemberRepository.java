package infrastructure.repository;

import domain.member.Member;
import domain.member.MemberRepository;
import domain.member.MemberStatus;
import domain.member.Member;
import domain.member.MemberStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import domain.member.Member;
import domain.member.MemberRepository;
import infrastructure.database.DatabaseConnection;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlMemberRepository implements MemberRepository {

    @Override
    public Member save(Member member) {

        String sql = """
                INSERT INTO members
                (name, phone, registration_date, status)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, member.getName());
            statement.setString(2, member.getPhone());
            statement.setDate(
                    3,
                    Date.valueOf(member.getRegistrationDate())
            );
            statement.setString(
                    4,
                    member.getStatus().name()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    int generatedId =
                            generatedKeys.getInt(1);

                    return new Member(
                            generatedId,
                            member.getName(),
                            member.getPhone(),
                            member.getRegistrationDate(),
                            member.getStatus()
                    );
                }
            }

            throw new SQLException(
                    "Failed to retrieve generated member ID."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error saving member.",
                    e
            );
        }
    }
    @Override
    public Optional<Member> findById(Integer id) {

        String sql = """
            SELECT member_id,
                   name,
                   phone,
                   registration_date,
                   status
            FROM members
            WHERE member_id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Member member = new Member(
                            resultSet.getInt("member_id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getDate("registration_date").toLocalDate(),
                            domain.member.MemberStatus.valueOf(
                                    resultSet.getString("status")
                            )
                    );

                    return Optional.of(member);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding member by ID.",
                    e
            );
        }
    }

    @Override
    public List<Member> findByName(String name) {

        String sql = """
            SELECT member_id,
                   name,
                   phone,
                   registration_date,
                   status
            FROM members
            WHERE name LIKE ?
            ORDER BY member_id
            """;

        List<Member> members = new java.util.ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Member member = new Member(
                            resultSet.getInt("member_id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getDate("registration_date").toLocalDate(),
                            domain.member.MemberStatus.valueOf(
                                    resultSet.getString("status")
                            )
                    );

                    members.add(member);
                }
            }

            return members;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding members by name.",
                    e
            );
        }
    }
    private Member mapToMember(ResultSet resultSet)
            throws SQLException {

        return new Member(
                resultSet.getInt("member_id"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getDate("registration_date")
                        .toLocalDate(),
                MemberStatus.valueOf(
                        resultSet.getString("status")
                )
        );
    }

    @Override
    public List<Member> findAll() {

        String sql = """
            SELECT member_id,
                   name,
                   phone,
                   registration_date,
                   status
            FROM members
            ORDER BY member_id
            """;

        List<Member> members = new java.util.ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Member member = new Member(
                        resultSet.getInt("member_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getDate("registration_date").toLocalDate(),
                        domain.member.MemberStatus.valueOf(
                                resultSet.getString("status")
                        )
                );

                members.add(member);
            }

            return members;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding all members.",
                    e
            );
        }
    }
    @Override
    public void update(Member member) {

        String sql = """
            UPDATE members
            SET name = ?,
                phone = ?,
                status = ?
            WHERE member_id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, member.getName());
            statement.setString(2, member.getPhone());
            statement.setString(3, member.getStatus().name());
            statement.setInt(4, member.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException(
                        "Member not found with ID: " + member.getId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating member.",
                    e
            );
        }
    }
    @Override
    public List<Member> findMembersEligibleForDeactivation() {

        String sql = """
            SELECT m.*
            FROM members m
            JOIN (
                SELECT member_id, MAX(end_date) AS last_end_date
                FROM subscriptions
                GROUP BY member_id
            ) s
            ON m.member_id = s.member_id
            WHERE m.status = 'ACTIVE'
              AND s.last_end_date < DATE_SUB(CURRENT_DATE, INTERVAL 2 MONTH)
            """;

        List<Member> members = new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                members.add(
                        mapToMember(resultSet)
                );
            }

            return members;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding inactive members.",
                    e
            );
        }
    }
}