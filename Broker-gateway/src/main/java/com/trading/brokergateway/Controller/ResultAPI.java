package com.trading.brokergateway.Controller;

import com.trading.brokergateway.Entity.Result;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class ResultAPI {
    @RequestMapping(value = "/Result",method = RequestMethod.GET)
    public String getResult(HttpServletRequest request){
        String id = request.getParameter("id");
        if(id == null){
            return null;
        }
        else{
            return null;
        }
    }
}
