package com.trading.tradergateway.Service.Implement;


import com.trading.tradergateway.Entity.Order;
import com.trading.tradergateway.JWT.JwtTokenUtil;
import com.trading.tradergateway.Repository.BrokerRepository;
import com.trading.tradergateway.Service.Interface.OrderService;
import com.trading.tradergateway.Util.FIX;
import com.trading.tradergateway.Util.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import com.trading.tradergateway.Util.DateUtil;


@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private JwtTokenUtil jwtTokenUtil;
    private BrokerRepository brokerRepository;

    @Autowired
    public OrderServiceImpl(BrokerRepository brokerRepository, JwtTokenUtil jwtTokenUtil) {
        this.brokerRepository = brokerRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean sendOrder(Order order, HttpServletRequest request) throws Exception{
        /*String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return false;*/
        //order.setOrderID(String.Valueof(UUID.randomUUID()));
        order.setOrderID("86b2bedd-1b3b-404d-af1f-f9b5783cc857");
        Long now = Calendar.getInstance().getTimeInMillis();
        order.setTimeStamp(now);
        System.out.println(FIX.toFIX(order));
        HttpRequest htp = new HttpRequest();
        htp.sendPost("http://192.168.0.114:8080/Order",FIX.toFIX(order));
        return true;
    }

    @Override
    public Map getOrders(String futuresID, String status, String page, HttpServletRequest request) {
        return null;
    }
}
