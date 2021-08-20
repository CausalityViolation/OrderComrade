package comradepizza.ordercomrade.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PizzaOrder {

    @GeneratedValue
    @Id
    Integer id;
    String messageFromCustomer;
    String orderInfo;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public PizzaOrder() {
    }

    public PizzaOrder(String messageFromCustomer, String orderInfo) {
        this.messageFromCustomer = messageFromCustomer;
        this.orderInfo = orderInfo;
    }

    public String getMessageFromCustomer() {
        return messageFromCustomer;
    }

    public void setMessageFromCustomer(String messageFromCustomer) {
        this.messageFromCustomer = messageFromCustomer;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String order) {
        this.orderInfo = order;
    }


}
