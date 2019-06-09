package com.trading.tradergateway.Repository;

import com.trading.tradergateway.Entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("OrderRepository")
public interface OrderRepository extends JpaRepository<Order, String> {
    Order findOrderByOrderID(String orderID);
    List<Order> findOrdersByTraderIdOrderByTimeStampDesc(String traderId);
    List<Order> findOrdersByTraderIdAndStatus(String traderId, String status);
    List<Order> findOrdersByTimeStampBetweenOrderByTimeStampDesc(Long start, Long end);
    List<Order> findOrdersByStatusAndTimeStampBetween(String status,Long start, Long end);
    List<Order> findOrdersByFutureIDOrderByTimeStampDesc(String futureId);
    List<Order> findOrdersByFutureIDAndStatus(String futureId, String status);
    List<Order>
}
