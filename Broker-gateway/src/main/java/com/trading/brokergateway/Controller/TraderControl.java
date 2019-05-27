package com.trading.brokergateway.Controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.trading.brokergateway.Methods.OrderControl;
import com.trading.brokergateway.Methods.OrderQueue;
import com.trading.brokergateway.Methods.SerializeUtil;
import com.trading.brokergateway.Methods.StoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import com.trading.brokergateway.Protocol.*;

import com.trading.brokergateway.Entity.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class TraderControl {
    @RequestMapping(value = "/Trader",method = RequestMethod.POST)
    public String TraderRegister(HttpServletRequest request){
        String traderID = request.getParameter("id");
        try{
            HashSet<String> set = StoreUtil.GetSet("traders");
            if(set.contains(traderID)){
                return "EXIST";
            }
            else{
                set.add(traderID);
                StoreUtil.SetSet(set);
                return "OK";
            }
        }
        catch (Exception e){
            return "ERR";
        }
    }

    @RequestMapping(value="Trader",method = RequestMethod.GET)
    public String TraderList(){
        String ret = "";
        try{
            HashSet<String> set = StoreUtil.GetSet("traders");
            for(String i:set){
                ret = ret + i + "||";
            }
            return ret;
        }
        catch (Exception e){
            return "ERR";
        }
    }

}
