package com.trading.tradergateway.Service.Implement;

import com.trading.tradergateway.Entity.Future;
import com.trading.tradergateway.JWT.JwtTokenUtil;
import com.trading.tradergateway.Repository.FutureRepository;
import com.trading.tradergateway.Service.Interface.FutureService;
import com.trading.tradergateway.Util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("FutureService")
public class FutureServiceImpl implements FutureService {
    private FutureRepository futureRepository;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public FutureServiceImpl(FutureRepository futureRepository, JwtTokenUtil jwtTokenUtil) {
        this.futureRepository = futureRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public List findAllFutures(){
        return futureRepository.findAll();
    }

    @Override
    public Future findFutureByFutureID(String futureID){
        return futureRepository.findFutureByFutureID(futureID);
    }

    @Override
    public List findFuturesByFutureName(String futureName){
        return futureRepository.findFuturesByFutureName(futureName);
    }

    @Override
    public String addFuture(Future future){
        future.setListTime(DateUtil.getDay());
        future.setExpired(DateUtil.addDay(new Date(),30));
        futureRepository.save(future);
        return "OK";
    }
}
