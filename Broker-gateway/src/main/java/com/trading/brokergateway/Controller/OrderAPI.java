package com.trading.brokergateway.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class OrderAPI {
    @RequestMapping(value = "/Order",method = RequestMethod.POST)
    public void orderPost(){
        String fix;
    }

    @RequestMapping(value = "/Order",method = RequestMethod.GET)
    public void orderGet(){
    }
}