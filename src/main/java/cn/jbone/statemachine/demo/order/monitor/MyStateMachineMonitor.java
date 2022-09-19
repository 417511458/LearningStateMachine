package cn.jbone.statemachine.demo.order.monitor;

import cn.jbone.statemachine.demo.order.OrderEvents;
import cn.jbone.statemachine.demo.order.OrderStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.monitor.StateMachineMonitor;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class MyStateMachineMonitor implements StateMachineMonitor<OrderStates, OrderEvents> {
    @Override
    public void transition(StateMachine<OrderStates, OrderEvents> stateMachine, Transition<OrderStates, OrderEvents> transition, long duration) {
        System.out.println("【Monitor】监听transition:" + transition.getSource().getId() + " to " + transition.getTarget().getId() + " used " + duration + "ms");
    }

    @Override
    public void action(StateMachine<OrderStates, OrderEvents> stateMachine, Function<StateContext<OrderStates, OrderEvents>, Mono<Void>> action, long duration) {
        System.out.println("【Monitor】监听action" + action + " used " + duration + " ms");
    }
}
