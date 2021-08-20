package comradepizza.ordercomrade.repos;

import comradepizza.ordercomrade.entities.PizzaOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<PizzaOrder, Integer> {
    PizzaOrder getById(int id);
}
