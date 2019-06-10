package com.trading.tradergateway.Controller;

import com.trading.tradergateway.Entity.Order;
import com.trading.tradergateway.Service.Interface.OrderService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SpringBootApplication
@RestController
@CrossOrigin(origins={"http://localhost:63342","null"})
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/GetOrder")
    public JSONArray getOrder(HttpServletRequest request){
        List list = orderService.getOrder(request);
        return JSONArray.fromObject(list);
    }

    @PostMapping("/SendOrder")
    public String sendOrder(@RequestBody Order order, HttpServletRequest request) throws Exception {
        String result = null;
        if (order.getAmount() >= 5000){
            if (order.getType() == 'C'){
                System.out.println("Failed iceberg cancel.");
                return "Order Request Failed";
            }
            result = orderService.icebergOrder(order,request);
            return result;
        }
        if (order.getType() == 'C'){
            result = orderService.cancelOrder(order.getOrderID(),request);
            System.out.println("C: " + result);
        }
        else {
            result = orderService.sendOrder(order, request);
            System.out.println("not C: " + result);
        }
        return result;
    }

    @GetMapping("/tid")
    public List getOrdersByTid(@RequestParam("traderId")String traderId) {
        return orderService.findOrdersByTraderId(traderId);
    }

    @GetMapping("/fid")
    public List getOrdersByFid(HttpServletRequest request) {
        String futureId = request.getParameter("futureId");
        System.out.println(futureId);
        try{
            String price = request.getParameter("price");
            return orderService.findOrdersByFutureIdAndPrice(futureId,Double.valueOf(price));
        }
        catch (Exception e){
            return orderService.findOrdersByFutureId2(futureId);
        }
    }

    @GetMapping("/today")
    public List getOrdersByToday() {
        return orderService.findOrdersByToday();
    }

    @GetMapping("/week")
    public List getOrdersByWeek(){
        return orderService.findOrdersByWeek();
    }

    @GetMapping("/month")
    public List getOrdersByMonth(){
        return orderService.findOrdersByMonth();
    }

    @GetMapping("/stat")
    public List getOrdersByStat(@RequestParam("status")String status){
        return orderService.findOrdersByStatus(status);
    }

    @GetMapping("/oid")
    public List getOrdersByOid(@RequestParam("orderId")String orderId){
        return orderService.findOrdersByOrderId(orderId);

    }
    @GetMapping("")
    public List getAllOrders(){
        return orderService.getAllOrders();
    }
}
