package cn.jbone.statemachine.demo.order.guards;

import cn.jbone.statemachine.demo.order.OrderEvents;
import cn.jbone.statemachine.demo.order.OrderStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class MyGuard implements Guard<OrderStates, OrderEvents> {
    @Override
    public boolean evaluate(StateContext<OrderStates, OrderEvents> context) {
        //true为通过，false为不通过
        return true;
    }
}
