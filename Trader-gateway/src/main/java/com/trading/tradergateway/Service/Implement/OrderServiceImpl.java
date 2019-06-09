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
    private static String brokerAddress = "localhost:8081";
    private static final String processing = "PROCESSING";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TraderRepository traderRepository, JwtTokenUtil jwtTokenUtil) {
        this.orderRepository = orderRepository;
        this.traderRepository = traderRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String sendOrder(Order order, HttpServletRequest request) throws Exception{
        String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return "Authorization failed";
        order.setTraderId(String.valueOf(traderRepository.findTraderByTraderName(username).getTraderID()));
        order.setOrderID(String.valueOf(UUID.randomUUID()));
        Long now = Calendar.getInstance().getTimeInMillis();
        order.setTimeStamp(now);
        HttpRequest htp = new HttpRequest();
        String status = htp.sendPost("http://"+brokerAddress+"/Order",FIX.toFIX(order));
        String[] params = status.split(",");
        status  = params[0];
        if (status.equals("failed")) return "Order failed";
        if (status.equals("PARTLY")) {
            int amount = Integer.parseInt(params[1]);
            status = params[0] + ":" + String.valueOf(amount);
        }
        order.setStatus(status);
        orderRepository.save(order);
        return "Order processing";
    }

    @Override
    public String cancelOrder(String orderId, HttpServletRequest request) throws Exception{
        String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return "Authorization failed";
        Order order = orderRepository.findOrderByOrderID(orderId);
        order.setType('C');
        HttpRequest htp = new HttpRequest();
        String status = htp.sendPost("http://"+brokerAddress+"/Order",FIX.toFIX(order));
        String[] params = status.split(",");
        status  = params[0];
        if (status.equals("failed")) return "Order failed";
        Order orderC = new Order();
        BeanUtils.copyProperties(orderC,order);
        orderC.setOrderID(String.valueOf(UUID.randomUUID()));
        Long now = Calendar.getInstance().getTimeInMillis();
        orderC.setTimeStamp(now);
        if (status.equals("PARTLY")) {
            int amount = Integer.parseInt(params[1]);
            status = params[0] + ":" + String.valueOf(amount);
        }
        orderC.setStatus(status);
        orderRepository.save(orderC);
        return "Order processing";
    }

    @Override
    public List findOrdersByTraderId(String traderId){
        List<Order> orders = orderRepository.findOrdersByTraderIdAndStatus(traderId, processing);
        return updateOrders(orders);
    }

    @Override
    public List findOrdersByFutureId(String futureId){
        List<Order> orders = orderRepository.findOrdersByFutureIDAndStatus(futureId, processing);
        return updateOrders(orders);
    }

    @Override
    public List findOrdersByToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); //当天0点
        long start = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        long end =  calendar.getTimeInMillis();
        List<Order> orders = orderRepository.findOrdersByStatusAndTimeStampBetween(processing, start, end);
        return updateOrders(orders);
    }


    @Override
    public List getOrder(HttpServletRequest request){
        String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return null;
        String traderId = String.valueOf(traderRepository.findTraderByTraderName(username).getTraderID());
        return findOrdersByTraderId(traderId);
    }

    @Override
    public List updateOrders(List<Order> orders){
        List<Order> results = new ArrayList<>();
        for (Order order : orders){
            HttpRequest htp = new HttpRequest();
            String status = htp.sendGet("http://"+brokerAddress+"/OrderStat","id=" + order.getOrderID());
            order.setStatus(status);
            orderRepository.save(order);
            results.add(order);
        }
        return results;
    }

}
