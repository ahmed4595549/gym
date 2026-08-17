package domain.member;

import java.time.LocalDate;

public class Member {

    private final Integer id;
    private String name;
    private String phone;
    private final LocalDate registrationDate;
    private MemberStatus status;

    // New Member
    public Member(
            String name,
            String phone,
            LocalDate registrationDate
    ) {
        validateName(name);
        validateRegistrationDate(registrationDate);

        this.id = null;
        this.name = name;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.status = MemberStatus.ACTIVE;
    }

    // Existing Member from Database
    public Member(
            Integer id,
            String name,
            String phone,
            LocalDate registrationDate,
            MemberStatus status
    ) {
        validateName(name);
        validateRegistrationDate(registrationDate);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid member ID");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Member name is required"
            );
        }
    }

    private void validateRegistrationDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Registration date is required"
            );
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void activate() {
        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = MemberStatus.INACTIVE;
    }
}
