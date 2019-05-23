package com.trading.brokergateway.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class Test2 {
    @RequestMapping("/set")
    public String get(){
        Jedis jedis = new Jedis("localhost");
        jedis.set("key","val");
        return "ok";
    }
}
