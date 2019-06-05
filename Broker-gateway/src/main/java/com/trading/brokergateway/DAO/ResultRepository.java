package com.trading.brokergateway.DAO;

import com.trading.brokergateway.Entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ResultRepository")
public interface ResultRepository extends JpaRepository<Result, String> {
    Result findResultByResultID(String resultID);
    List findResultsByPriceAndFutureIDOrderByFinishTimeDesc(double price, String futureId);
    List findResultsByOrderID1OrOrderID2OrderByFinishTimeDesc(String orderId1, String orderId2);
    List findAllByOrderByFinishTimeDesc();
    List findResultsByFutureIDOrderByFinishTime(String futureId);
    List findResultsByFinishTimeBetweenOrderByFinishTimeDesc(long start, long end);
    List findResultsByTraderID1OrTraderID2OrderByFinishTimeDesc(String traderId1,String traderId2);
}
