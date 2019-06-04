package com.trading.brokergateway.Controller;

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
@RequestMapping("/result")
public class ResultAPI {
    private ResultService resultService;

    @Autowired
    public ResultAPI(ResultService resultService) {
        this.resultService = resultService;
    }
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

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String glt(HttpServletRequest request){
        Result result = new Result();
        result.setAmount(20);
        result.setFutureID("SB");
        result.setFinishTime(Calendar.getInstance().getTimeInMillis());
        result.setPrice(2.1);
        result.setResultID(String.valueOf(UUID.randomUUID()));
        result.setOrderID1(String.valueOf(UUID.randomUUID()));
        result.setOrderID2(String.valueOf(UUID.randomUUID()));
        result.setTraderID1("yry");
        result.setTraderID2("yty");
        resultService.saveTradeResult(result);
        return "ok";
    }
}
