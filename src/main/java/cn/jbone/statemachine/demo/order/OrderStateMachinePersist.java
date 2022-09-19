package cn.jbone.statemachine.demo.order;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的订单状态机持久化器
 */
@Component
public class OrderStateMachinePersist implements StateMachinePersist<OrderStates,OrderEvents,Order> {


    @Override
    public void write(StateMachineContext<OrderStates, OrderEvents> stateMachineContext, Order order) throws Exception {
//        orderMap.put(orderId,stateMachineContext.getState());
    }

    @Override
    public StateMachineContext<OrderStates, OrderEvents> read(Order order) throws Exception {
        //重置状态机状态
        return new DefaultStateMachineContext<>(order.getState(), null, null, null, null);
    }
}
