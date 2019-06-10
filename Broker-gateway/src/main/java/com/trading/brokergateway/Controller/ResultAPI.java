package com.trading.brokergateway.Controller;

import com.google.gson.Gson;
import com.trading.brokergateway.DAO.ResultService;
import com.trading.brokergateway.Entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.UUID;

@SpringBootApplication
@RestController
@CrossOrigin(origins = {"http://localhost:63342", "null"})
public class ResultAPI {
    private ResultService resultService;

    @Autowired
    public ResultAPI(ResultService resultService) {
        this.resultService = resultService;
    }
    @RequestMapping(value = "/Result",method = RequestMethod.GET)
    public String getResult(HttpServletRequest request) throws Exception{
        Gson gson = new Gson();
        String method = request.getParameter("method");
        if(method == null){
            return gson.toJson(this.resultService.findAll());
        }
        if(method.equals("ByOrderID")){
            String orderID = request.getParameter("id");
            return gson.toJson(this.resultService.findByOrderId(orderID));
        }
        else if(method.equals("ByTime")){
            String start = request.getParameter("start");
            if(start.equals("today")){
                return gson.toJson(this.resultService.findToday());
            }
            else if(start.equals("week")){
                return gson.toJson(this.resultService.findToweek());
            }
            else if(start.equals("month")){
                return gson.toJson(this.resultService.findTomonth());
            }
        }
        else if(method.equals("ByPrice")){
            Double price = Double.valueOf(request.getParameter("price"));
            String futureId = request.getParameter("id");
            return gson.toJson(this.resultService.findByPrice(price, futureId));
        }
        else if(method.equals("ByFutureID")){
            String futureID = request.getParameter("id");
            return gson.toJson(this.resultService.findByFutureId(futureID));
        }
        else if(method.equals("ByTraderID")){
            String traderID = request.getParameter("id");
            return gson.toJson(this.resultService.findByTraderId(traderID));
        }
        return null;
    }

}
