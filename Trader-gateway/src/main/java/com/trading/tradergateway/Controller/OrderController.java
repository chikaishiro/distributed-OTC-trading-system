package com.trading.tradergateway.Controller;

import com.trading.tradergateway.Entity.Order;
import com.trading.tradergateway.Service.Interface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@RestController
@CrossOrigin(origins={"http://localhost:63342","null"})
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value="/SendOrder",method= RequestMethod.POST)
    public String sendOrder(@RequestBody Order order, HttpServletRequest request) throws Exception {
        String result = null;
        if (order.getType() == 'C'){
            result = orderService.cancelOrder(order.getOrderID(),request);
            System.out.println("C: " + result);
        }
        else {
            result = orderService.sendOrder(order, request);
            System.out.println("not C: " + result);
        }
        return result;
    }

}
