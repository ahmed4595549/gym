package domain.plan;
import java.math.BigDecimal;




public class Plan {

    private final Integer id;

    private String name;

    private BigDecimal price;

    private int durationDays;

    private AccessType accessType;

    private PlanStatus status;

    public Plan(
            String name,
            BigDecimal price,
            int durationDays,
            AccessType accessType
    ) {

        validateName(name);
        validatePrice(price);
        validateDuration(durationDays);

        if (accessType == null) {
            throw new IllegalArgumentException(
                    "Access type cannot be null."
            );
        }

        this.id = null;
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.accessType = accessType;
        this.status = PlanStatus.ACTIVE;
    }

    public Plan(
            Integer id,
            String name,
            BigDecimal price,
            int durationDays,
            AccessType accessType,
            PlanStatus status
    ) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be positive."
            );
        }

        validateName(name);
        validatePrice(price);
        validateDuration(durationDays);

        if (accessType == null) {
            throw new IllegalArgumentException(
                    "Access type cannot be null."
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Status cannot be null."
            );
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.accessType = accessType;
        this.status = status;
    }
    public void changeName(String name) {

        validateName(name);

        this.name = name;
    }

    public void changePrice(BigDecimal price) {

        validatePrice(price);

        this.price = price;
    }

    public void deactivate() {
        this.status = PlanStatus.INACTIVE;
    }

    public void activate() {
        this.status = PlanStatus.ACTIVE;
    }

    private void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Plan name cannot be empty."
            );
        }
    }

    private void validatePrice(BigDecimal price) {

        if (price == null ||
                price.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Price cannot be negative."
            );
        }
    }

    private void validateDuration(int durationDays) {

        if (durationDays <= 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than zero."
            );
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public PlanStatus getStatus() {
        return status;
    }
}