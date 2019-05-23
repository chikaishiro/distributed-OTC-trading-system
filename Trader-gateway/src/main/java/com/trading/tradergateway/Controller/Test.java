package com.trading.tradergateway.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class Test {
    @RequestMapping("")
    public String get(){
        return "2";
    }
}
