package com.trading.brokergateway.Controller;

import com.trading.brokergateway.Methods.StoreUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class OrderStatAPI {
    @RequestMapping(value = "OrderStat",method = RequestMethod.GET)
    public String getStat(HttpServletRequest req){
        String uuid = req.getParameter("id");
        return StoreUtil.getOrderStat(uuid);

    }
}
