package cn.jbone.statemachine.demo.order;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单服务，模拟订单的创建、获取和更新
 */
@Service
public class OrderService {
    //用一个map，模拟数据库的存储
    Map<String,Order> map = new HashMap<>();

    /**
     * 创建订单
     */
    public void createOrder(Order order){
        map.put(order.getId(),order);
    }

    /**
     * 根据订单ID获取订单
     */
    public Order getOrder(String orderId){
        return map.get(orderId);
    }

    /**
     * 更新订单
     */
    public void updateOrder(Order order){
        map.put(order.getId(),order);
    }
}
