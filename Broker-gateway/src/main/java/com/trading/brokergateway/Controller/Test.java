package com.trading.brokergateway.Controller;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class Test {
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public String parseUsername(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        String tokenHeader = "Bearer ";
        String authToken = authHeader.substring(tokenHeader.length());
        System.out.println(authToken);
        return "ok";
    }

    @RequestMapping(value = "/put",method = RequestMethod.GET)
    public String ad(){
        return "ok";
    }
}
