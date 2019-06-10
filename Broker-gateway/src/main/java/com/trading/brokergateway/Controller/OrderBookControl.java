package com.trading.brokergateway.Controller;

import com.google.gson.Gson;
import com.trading.brokergateway.Entity.Order;
import com.trading.brokergateway.Entity.OrderBook;
import com.trading.brokergateway.Methods.OrderQueue;
import com.trading.brokergateway.Methods.StoreUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class OrderBookControl {
    @RequestMapping(value = "/OrderBook",method = RequestMethod.GET)
    public String getOrderBook(HttpServletRequest req){
        String id = req.getParameter("futureID");
        OrderQueue orderQueue = StoreUtil.GetQueue(id);
        Gson gs = new Gson();
        OrderBook ODB = new OrderBook(orderQueue,true);
        String to_string = gs.toJson(ODB);
        return to_string;

    }
}
