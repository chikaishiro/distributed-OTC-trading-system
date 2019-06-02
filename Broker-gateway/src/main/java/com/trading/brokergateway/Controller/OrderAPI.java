package com.trading.brokergateway.Controller;

import com.google.gson.Gson;
import com.trading.brokergateway.Methods.OrderControl;
import com.trading.brokergateway.Methods.OrderQueue;
import com.trading.brokergateway.Methods.StoreUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import com.trading.brokergateway.Util.*;

import com.trading.brokergateway.Entity.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.PriorityQueue;
import java.util.UUID;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
@Scope("singleton")
public class OrderAPI {

    public OrderAPI(){
    }

    @RequestMapping(value = "/B",method = RequestMethod.GET)
    public String orderTest(){
        String ret = "";
        FIX fix = new FIX();
        OrderQueue odq = StoreUtil.GetQueue("SB");
        PriorityQueue<Order> sell = odq.getSellQueue();
        PriorityQueue<Order> buy = odq.getBuyQueue();
        while(sell.size() != 0){
            Order ord = sell.poll();
            String temp = fix.toFIX(ord);
            ret = ret + temp;
        }
        ret = ret + "||";
        while(buy.size() != 0){
            Order ord = buy.poll();
            String temp = fix.toFIX(ord);
            ret = ret + temp;
        }
        return ret;
    }

    @RequestMapping(value = "/A",method = RequestMethod.GET)
    public String re(HttpServletRequest req){


        UUID UID = UUID.randomUUID();
        double price = Double.valueOf(req.getParameter("price"));
        Order ord = new Order(UID,"SB",'M','S',price,1000,"2.4", Calendar.getInstance().getTimeInMillis(),"xxx");
        OrderQueue odq = StoreUtil.GetQueue(ord.getFutureID());
        odq.insertSell(ord);
        StoreUtil.SetQueue(odq,ord.getFutureID());
        return "O";
    }

    @RequestMapping(value = "/C",method = RequestMethod.GET)
    public String res(HttpServletRequest req){


        UUID UID = UUID.randomUUID();
        double price = Double.valueOf(req.getParameter("price"));
        Order ord = new Order(UID,"SB",'M','B',price,1000,"2.4", Calendar.getInstance().getTimeInMillis(),"xxx");
        OrderQueue odq = StoreUtil.GetQueue(ord.getFutureID());
        odq.insertBuy(ord);
        StoreUtil.SetQueue(odq,ord.getFutureID());
        return "O";
    }
    @RequestMapping(value = "/Order",method = RequestMethod.POST)
    public void orderPost(){
        String fix;
        UUID uid = UUID.randomUUID();
        System.out.println(uid);
        Order ord1 = new Order(uid, "SB", 'S', 'S', 3.2, 200, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Gson gs = new Gson();

        int i = OrderControl.OrderDeal(gs.toJson(ord1));
        System.out.println(i);

    }

    @RequestMapping(value = "/Order",method = RequestMethod.GET)
    public void orderGet(){
        String fix;
        UUID uid = UUID.randomUUID();
        System.out.println(uid);
        Order ord1 = new Order(uid, "SB", 'S', 'S', 3.2, 100, "2.4",
                Calendar.getInstance().getTimeInMillis(), "xxx");
        Gson gs = new Gson();

        int i = OrderControl.OrderDeal(gs.toJson(ord1));
        System.out.println(i);
    }
}