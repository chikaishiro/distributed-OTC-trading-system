package com.trading.tradergateway.Service.Implement;


import com.trading.tradergateway.Entity.Order;
import com.trading.tradergateway.Service.Interface.OrderService;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import com.trading.tradergateway.Util.DateUtil;


@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Override
    public boolean sendOrder(Order order, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");
        if (username == null) return false;
        order.setOrderID(UUID.randomUUID());
        String now = DateUtil.getDate("yyyy-MM-dd HH:mm:ss");
        order.setTimeStamp(DateUtil.getDateByStr(now));
        return true;
    }

    @Override
    public Map getOrders(String futuresID, String status, String page, HttpServletRequest request) {
        return null;
    }
}
