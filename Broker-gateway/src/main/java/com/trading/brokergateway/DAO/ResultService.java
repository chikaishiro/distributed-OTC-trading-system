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

    public static List<Result> findByPrice(double price){
        return resultRepository.findResultsByPriceOrderByFinishTimeDesc(price).subList(0,20);
    }

    public static List<Result> findByOrderId(String orderId){
        return resultRepository.findResultsByOrderID1OrOrderID2OrderByFinishTimeDesc(orderId, orderId).subList(0,20);
    }

    public static List<Result> findall(){
        return resultRepository.findallOrderByFinishTimeDesc().subList(0,20);
    }

    public static List<Result> findByFutureId(String futureId){
        return resultRepository.findResultsByFutureIDOrderByFinishTime(futureId).subList(0,20);
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
        return resultRepository.findResultsByFinishTimeBetweenOrderByFinishTimeDesc(start, end).subList(0, 20);

    }

    public static void main(String[] args){
        Result result = new Result();


    }

}
