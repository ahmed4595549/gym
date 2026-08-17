package domain.plan;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    Plan save(Plan plan);

    Optional<Plan> findById(Integer id);

    Optional<Plan> findByName(String name);

    List<Plan> findAll();

    void update(Plan plan);

}