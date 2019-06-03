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
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value="/SendOrder",method= RequestMethod.GET)
    public boolean sendOrder(@RequestBody Order order, HttpServletRequest request) throws Exception {
        boolean result = orderService.sendOrder(order, request);
        return result;
    }

}
