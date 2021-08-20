package comradepizza.ordercomrade.controllers;

import comradepizza.ordercomrade.entities.PizzaOrder;
import comradepizza.ordercomrade.repos.OrderRepo;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
public class OrderController {

    public final OrderRepo repo;

    public OrderController(OrderRepo repo) {
        this.repo = repo;
    }

    @PostMapping("/order/{orderDetails}")
    public String placeOrder(@PathVariable String orderDetails) throws IOException {

        URL url = new URL("http://localhost:5050/comrade");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        String availablePizzas = String.valueOf(content);
        String[] splitOrder = orderDetails.split("[#]");
        String message = splitOrder[1];
        String[] pizzasOrderedArray = splitOrder[0].split("[$]");
        String pizzasOrderedString = "";

        for (int i = 0; i < pizzasOrderedArray.length; i++) {
            if (availablePizzas.contains("\"name\":\"" + pizzasOrderedArray[i] + "\"")) {
                pizzasOrderedString += pizzasOrderedArray[i] + "$" + pizzasOrderedArray[i + 1] + "$";
            }
        }

        PizzaOrder pizzaOrder = new PizzaOrder(message, pizzasOrderedString);
        repo.save(pizzaOrder);
        return "Order Placed";
    }

    @GetMapping("/order/{id}")
    public PizzaOrder getOrder(@PathVariable String id) {
        int idNumerical;
        try {
            idNumerical = Integer.parseInt(id);
        } catch (NumberFormatException num) {
            idNumerical = -1;
        }
        return repo.getById(idNumerical);
    }

    @GetMapping("/order/")
    public List<PizzaOrder> getAllOrders() {
        List<PizzaOrder> allPizzaOrders = repo.findAll();
        return allPizzaOrders;
    }

    @Transactional
    @DeleteMapping("/order/{id}")
    public String removeOrder(@PathVariable String id) {
        int idNumerical;
        try {
            idNumerical = Integer.parseInt(id);
        } catch (NumberFormatException num) {
            return "Not Found";
        }
        if (repo.findById(idNumerical).isPresent()) {
            repo.deleteById(idNumerical);
            return "Order Removed";
        }
        return "Not Found";
    }
    //order/PIZZA$ANTAL$PIZZA$ANTAL#MEDDELANDE

}
