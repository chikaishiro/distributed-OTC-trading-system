package com.trading.brokergateway.DAO;

import com.trading.brokergateway.Entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ResultService")
public class ResultSaving {
    private ResultRepository resultRepository;

    @Autowired
    public ResultSaving(ResultRepository resultRepository){
        this.resultRepository = resultRepository;
    }

    public void saveTradeResult(Result result){
        resultRepository.save(result);
    }

}
