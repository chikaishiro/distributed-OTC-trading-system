package com.trading.tradergateway.Service.Implement;


import com.trading.tradergateway.Entity.Order;
import com.trading.tradergateway.JWT.JwtTokenUtil;
import com.trading.tradergateway.Repository.OrderRepository;
import com.trading.tradergateway.Repository.TraderRepository;
import com.trading.tradergateway.Service.Interface.OrderService;
import com.trading.tradergateway.Util.FIX;
import com.trading.tradergateway.Util.HttpRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private JwtTokenUtil jwtTokenUtil;
    private OrderRepository orderRepository;
    private TraderRepository traderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TraderRepository traderRepository, JwtTokenUtil jwtTokenUtil) {
        this.orderRepository = orderRepository;
        this.traderRepository = traderRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String sendOrder(Order order, HttpServletRequest request) throws Exception{
        /*String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return "Authorization failed";*/
        //order.setTraderId(traderRepository.findTraderByTraderName(username).getTraderID());
        order.setOrderID(String.valueOf(UUID.randomUUID()));
        Long now = Calendar.getInstance().getTimeInMillis();
        order.setTimeStamp(now);
        HttpRequest htp = new HttpRequest();
        String status = htp.sendPost("http://192.168.0.114:8080/Order",FIX.toFIX(order));
        String[] params = status.split(",");
        status  = params[0];
        if (status.equals("failed")) return "Order failed";
        order.setStatus(status);
        if (status.equals("PARTLY")) {
            int amount = Integer.parseInt(params[1]);
            order.setAmount(amount);
            orderRepository.save(order);
            return "This order has been partially done with number " + amount;
        }
        else{
            orderRepository.save(order);
            return status;
        }
    }

    @Override
    public String cancelOrder(String orderId, HttpServletRequest request) throws Exception{
        /*String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return "Authorization failed";*/
        Order order = orderRepository.findOrderByOrderID(orderId);
        order.setType('C');
        HttpRequest htp = new HttpRequest();
        String status = htp.sendPost("http://192.168.0.114:8080/Order",FIX.toFIX(order));
        String[] params = status.split(",");
        status  = params[0];
        if (status.equals("failed")) return "Order failed";
        Order orderC = new Order();
        BeanUtils.copyProperties(orderC,order);
        orderC.setStatus(status);
        orderC.setOrderID(String.valueOf(UUID.randomUUID()));
        Long now = Calendar.getInstance().getTimeInMillis();
        orderC.setTimeStamp(now);
        if (status.equals("PARTLY")) {
            int amount = Integer.parseInt(params[1]);
            orderC.setAmount(amount);
            orderRepository.save(orderC);
            return "This order has been partially done with number " + amount;
        }
        else{
            orderRepository.save(orderC);
            return status;
        }
    }

    @Override
    public List findOrdersByTraderName(String traderId){
        return orderRepository.findOrdersByTraderId(traderId);
    }
}
