package com.trading.brokergateway.DAO;

import com.trading.brokergateway.Entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service("ResultService")
public class ResultService {
    private static ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository){
        this.resultRepository = resultRepository;
    }

    public static void saveTradeResult(Result result){
        resultRepository.save(result);
    }



    public static void main(String[] args){
        Result result = new Result();


    }

}
