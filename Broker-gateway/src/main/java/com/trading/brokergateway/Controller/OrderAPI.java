package com.trading.brokergateway.Controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.trading.brokergateway.Methods.OrderControl;
import com.trading.brokergateway.Methods.OrderQueue;
import com.trading.brokergateway.Methods.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import com.trading.brokergateway.Protocol.*;

import com.trading.brokergateway.Entity.Order;

import java.util.PriorityQueue;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
@Scope("singleton")
public class OrderAPI {

    public OrderAPI(){
    }

    @RequestMapping(value = "/B",method = RequestMethod.GET)
    public String orderTest(){
        Jedis jedis = new Jedis("localhost");
        String ret = "";
        FIX fix = new FIX();
        OrderQueue odq;
        try{
           odq =(OrderQueue) SerializeUtil.unserialize(jedis.get("order".getBytes()));
        }
        catch (Exception e){
            odq = new OrderQueue();
        }


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
    public String re(){
        Order ord = new Order(3.3,"SB",'S');
        Jedis jedis = new Jedis("localhost");
        byte[] tp = jedis.get("order".getBytes());
        OrderQueue odq;
        if(tp == null){
            odq = new OrderQueue();
        }
        else {
            odq = (OrderQueue) SerializeUtil.unserialize(tp);
        }

        odq.insertSell(ord);
        jedis.set("order".getBytes(), SerializeUtil.serialize(odq));
        return "O";
    }
    @RequestMapping(value = "/Order",method = RequestMethod.POST)
    public void orderPost(){
        String fix;
    }

    @RequestMapping(value = "/Order",method = RequestMethod.GET)
    public void orderGet(){
    }
}