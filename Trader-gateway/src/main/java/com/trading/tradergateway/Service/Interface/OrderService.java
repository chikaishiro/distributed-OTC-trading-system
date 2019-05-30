package com.trading.tradergateway.Service.Interface;

import com.trading.tradergateway.Entity.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface OrderService {

        boolean sendOrder(Order order, HttpServletRequest request) throws Exception;
        Map getOrders(String futuresID, String status, String page, HttpServletRequest request);

}
