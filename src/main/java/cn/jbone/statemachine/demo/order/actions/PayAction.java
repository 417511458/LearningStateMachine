package cn.jbone.statemachine.demo.order.actions;

import cn.jbone.statemachine.demo.order.OrderEvents;
import cn.jbone.statemachine.demo.order.OrderStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class PayAction implements Action<OrderStates, OrderEvents> {
    @Override
    public void execute(StateContext<OrderStates, OrderEvents> context) {
        System.out.println("【Action】【PayAction】" + context.getMessageHeader("order") + "执行支付Action " + context.getEvent());
    }
}
