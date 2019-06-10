package com.trading.tradergateway.Service.Interface;

import com.trading.tradergateway.Entity.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface OrderService {

        String sendOrder(Order order, HttpServletRequest request) throws Exception;
        String cancelOrder(String orderId, HttpServletRequest request) throws Exception;
        List getOrder(HttpServletRequest request);
        List findOrdersByTraderId(String orderId);
        List findOrdersByFutureId(String futureId);
        List findOrdersByToday();
        List findOrdersByWeek();
        List findOrdersByMonth();
        List getAllOrders();
        List findOrdersByFutureId2(String futureId);
        List findOrdersByFutureIdAndPrice(String futureId,double price);
        List findOrdersByOrderId(String orderId);
        List findOrdersByStatus(String status);
        void updateOrders(List<Order> orders);
}
