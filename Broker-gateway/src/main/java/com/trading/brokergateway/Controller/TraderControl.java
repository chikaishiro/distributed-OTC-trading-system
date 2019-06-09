package com.trading.brokergateway.Controller;

import com.google.gson.Gson;
import com.trading.brokergateway.Methods.StoreUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/Trader",method = RequestMethod.GET)
    public String TraderList(){
        Gson gson  = new Gson();
        try
        {
            HashSet<String> set = StoreUtil.GetSet("traders");
            return gson.toJson(set);
        }
        catch (Exception e){
            return "ERR";
        }
    }

}
