package comradepizza.ordercomrade.controllers;

import comradepizza.ordercomrade.entities.PizzaOrder;
import comradepizza.ordercomrade.repos.OrderRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    public StringBuffer getMenu() throws IOException {

        URL url = new URL("http://localhost:9000/comrade");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content;
    }

    @PostMapping("/order/{orderDetails}")
    public String placeOrder(@PathVariable String orderDetails) throws IOException {

        StringBuffer content = getMenu();

        String availablePizzas = String.valueOf(content);
        String[] splitOrder;
        String message;
        if (orderDetails.contains("&")) {
            splitOrder = orderDetails.split("[&]");
            message = splitOrder[1];
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        String[] pizzasOrderedArray = splitOrder[0].split("[$]");
        String pizzasOrderedString = "";

        int pizzaFound = 0;
        for (int i = 0; i < pizzasOrderedArray.length; i++) {
            try {
                int amount = Integer.parseInt(pizzasOrderedArray[i]);
                pizzasOrderedString += amount;
            } catch (NumberFormatException e) {
                if (availablePizzas.contains("\"name\":\"" + pizzasOrderedArray[i] + "\"")) {
                    pizzasOrderedString += pizzasOrderedArray[i];
                    pizzaFound++;
                }
            }
        }
        if (pizzaFound == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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

    @GetMapping("/order")
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

    @GetMapping("/order/menu")
    public StringBuffer showMenu() throws IOException{
        return getMenu();
    }
    //order/PIZZA$ANTAL$PIZZA$ANTAL#MEDDELANDE


    public static void main(String[] args) throws IOException {


    }

}
