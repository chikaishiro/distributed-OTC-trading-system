package com.trading.tradergateway.Repository;

import com.trading.tradergateway.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("OrderRepository")
public interface OrderRepository extends JpaRepository<Order, String> {
    Order findOrderByOrderID(String orderID);
    List<Order> findOrdersByTraderIdAndStatus(String traderId, String status);
    List<Order> findOrdersByStatusAndTimeStampBetween(String status,Long start, Long end);
    List<Order> findOrdersByFutureIDAndStatus(String futureId, String status);
}
