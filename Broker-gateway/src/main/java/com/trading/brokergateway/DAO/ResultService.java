package com.trading.brokergateway.DAO;

import com.trading.brokergateway.Entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
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

    public static List<Result> findByPrice(double price, String futureId){
        List<Result> list = resultRepository.findResultsByPriceAndFutureIDOrderByFinishTimeDesc(price, futureId).subList(0,10);
        if(list.size()>10){
            return list.subList(0,10);
        }
        else{
            return list;
        }
    }

    public static List<Result> findByOrderId(String orderId){
        List<Result> list = resultRepository.findResultsByOrderID1OrOrderID2OrderByFinishTimeDesc(orderId, orderId);
        if(list.size()>10){
            return list.subList(0,10);
        }
        else{
            return list;
        }
    }

    public static List<Result> findByTraderId(String traderId){
        List<Result> list = resultRepository.findResultsByTraderID1OrTraderID2OrderByFinishTimeDesc(traderId,traderId);
        if(list.size()>10){
            return list.subList(0,10);
        }
        else{
            return list;
        }
    }

    public static List<Result> findAll(){
        List<Result> list = resultRepository.findAllByOrderByFinishTimeDesc().subList(0,10);
        if(list.size()>10){
            return list.subList(0,10);
        }
        else{
            return list;
        }
    }

    public static List<Result> findByFutureId(String futureId){
        List<Result> list = resultRepository.findResultsByFutureIDOrderByFinishTime(futureId).subList(0,10);
        if(list.size()>10){
            return list.subList(0,10);
        }
        else{
            return list;
        }
    }

    public static List<Result> findToday (){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); //当天0点
        long start = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        long end =  calendar.getTimeInMillis();
        List<Result> list = resultRepository.findResultsByFinishTimeBetweenOrderByFinishTimeDesc(start, end).subList(0, 10);
        if(list.size()>10){
            return list.subList(0,10);
        }
        else{
            return list;
        }

    }

    public static void main(String[] args){
        Result result = new Result();


    }

}
