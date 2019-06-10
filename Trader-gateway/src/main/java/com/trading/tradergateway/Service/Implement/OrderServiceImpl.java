package com.trading.tradergateway.Service.Implement;


import com.trading.tradergateway.Entity.Order;
import com.trading.tradergateway.JWT.JwtTokenUtil;
import com.trading.tradergateway.Repository.OrderRepository;
import com.trading.tradergateway.Repository.TraderRepository;
import com.trading.tradergateway.Service.Interface.OrderService;
import com.trading.tradergateway.Util.DateUtil;
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
        String status = htp.sendPost("http://"+order.getBrokerIp()+":8081/Order",FIX.toFIX(order));
        String[] params = status.split(",");
        status  = params[0];
        if (status.equals("NOK") || status.equals("FAILED")) return "Order Request Failed";
        if (status.equals("PARTLY")) {
            int amount = Integer.parseInt(params[1]);
            status = params[0] + ":" + String.valueOf(amount);
        }
        order.setStatus(status);
        orderRepository.save(order);
        return "Order Request Processing";
    }

    @Override
    public String cancelOrder(String orderId, HttpServletRequest request) throws Exception{
        String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return "Authorization failed";
        Order order = orderRepository.findOrderByOrderID(orderId);
        char type = order.getType();
        order.setType('C');
        HttpRequest htp = new HttpRequest();
        String status = htp.sendPost("http://"+order.getBrokerIp()+":8081/Order",FIX.toFIX(order));
        String[] params = status.split(",");
        status  = params[0];
        if (status.equals("FAILED")) return "Cancel Order Failed(Already done)";
        String addon,stat;
        Order orderC = new Order();
        BeanUtils.copyProperties(orderC,order);
        orderC.setOrderID(String.valueOf(UUID.randomUUID()));
        Long now = Calendar.getInstance().getTimeInMillis();
        orderC.setTimeStamp(now);
        if (status.equals("PARTLY")) {
            int amount = Integer.parseInt(params[1]);
            status = params[0] + ":" + String.valueOf(amount);
            addon = " Partly canceled with amount "+ String.valueOf(amount);
            stat = "PARTLY CANCELED";
        }
        else{
            addon = " Successfully canceled.";
            stat = "CANCELED";

        }
        orderC.setStatus(status);
        order.setStatus(stat);
        order.setType(type);
        orderRepository.save(orderC);
        orderRepository.save(order);
        return "Order Request Status:"+ addon;
    }

    @Override
    public String icebergOrder(Order order, HttpServletRequest request) throws Exception{
        String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return "Authorization failed";
        order.setTraderId(String.valueOf(traderRepository.findTraderByTraderName(username).getTraderID()));
        order.setOrderID(String.valueOf(UUID.randomUUID()));
        Long now = Calendar.getInstance().getTimeInMillis();
        order.setTimeStamp(now);
        HttpRequest htp = new HttpRequest();
        int amount = order.getAmount();
        int i;
        int times  = amount / (200);
        for (i = 0; i < times - 1; i++) {
            Thread.sleep(3 * 1000);
            Order orderI = new Order();
            BeanUtils.copyProperties(orderI, order);
            orderI.setOrderID(String.valueOf(UUID.randomUUID()));
            now = Calendar.getInstance().getTimeInMillis();
            orderI.setTimeStamp(now);
            orderI.setAmount(200);
            String status = htp.sendPost("http://" + orderI.getBrokerIp() + ":8081/Order", FIX.toFIX(orderI));
            orderI.setStatus(status);
            orderRepository.save(orderI);
        }
        int left = amount - times * 200;
        Order orderI = new Order();
        BeanUtils.copyProperties(orderI, order);
        orderI.setOrderID(String.valueOf(UUID.randomUUID()));
        now = Calendar.getInstance().getTimeInMillis();
        orderI.setTimeStamp(now);
        orderI.setAmount(left);
        String status = htp.sendPost("http://" + orderI.getBrokerIp() + ":8081/Order", FIX.toFIX(orderI));
        orderI.setStatus(status);
        orderRepository.save(orderI);
        order.setType('I');
        orderRepository.save(order);
        return "OK";
    }

    @Override
    public List findOrdersByTraderId(String traderId){
        List<Order> orders = orderRepository.findOrdersByTraderIdAndStatus(traderId, processing);
        updateOrders(orders);
        return orderRepository.findOrdersByTraderIdOrderByTimeStampDesc(traderId);
    }

    @Override
    public List findOrdersByFutureId(String futureId){
        List<Order> orders = orderRepository.findOrdersByFutureIDAndStatus(futureId, processing);
        updateOrders(orders);
        return orderRepository.findOrdersByFutureIDOrderByTimeStampDesc(futureId);
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
        updateOrders(orders);
        return orderRepository.findOrdersByTimeStampBetweenOrderByTimeStampDesc(start, end);
    }

    @Override
    public List getAllOrders(){
        return orderRepository.findAllByOrderByTimeStampDesc();
    }

    @Override
    public List findOrdersByFutureIdAndPrice(String futureId,double price){
        return orderRepository.findOrdersByPriceAndFutureIDOrderByTimeStampDesc(price,futureId);
    }

    @Override
    public List findOrdersByOrderId(String orderId){
        return orderRepository.findOrdersByOrderIDOrderByTimeStampDesc(orderId);
    }
    @Override
    public List findOrdersByStatus(String status){
        return orderRepository.findOrdersByStatusOrderByTimeStampDesc(status);
    }
    @Override
    public List findOrdersByFutureId2(String futureId){
        return orderRepository.findOrdersByFutureIDOrderByTimeStampDesc(futureId);
    }

    @Override
    public List findOrdersByWeek(){
        long start = DateUtil.getWeekStart();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); //当天0点
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        long end =  calendar.getTimeInMillis();
        return orderRepository.findOrdersByTimeStampBetweenOrderByTimeStampDesc(start, end);
    }

    @Override
    public List findOrdersByMonth(){
        long start = DateUtil.getMonthStart();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); //当天0点
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        long end =  calendar.getTimeInMillis();
        return orderRepository.findOrdersByTimeStampBetweenOrderByTimeStampDesc(start, end);
    }

    @Override
    public List getOrder(HttpServletRequest request){
        String username = jwtTokenUtil.parseUsername(request);
        if (username == null) return null;
        String traderId = String.valueOf(traderRepository.findTraderByTraderName(username).getTraderID());
        return findOrdersByTraderId(traderId);
    }

    @Override
    public void updateOrders(List<Order> orders){
        for (Order order : orders){
            HttpRequest htp = new HttpRequest();
            String bip = order.getBrokerIp();
            String status;
            if(bip.length()!=0){
                status = htp.sendGet("http://"+bip+":8081/OrderStat","id=" + order.getOrderID());
            }
            else{
                status = htp.sendGet("http://"+brokerAddress+"/OrderStat","id=" + order.getOrderID());
            }
            order.setStatus(status);
            orderRepository.save(order);
        }
    }


}
