package cn.jbone.statemachine.demo.order.listener;

import cn.jbone.statemachine.demo.order.Order;
import cn.jbone.statemachine.demo.order.OrderEvents;
import cn.jbone.statemachine.demo.order.OrderService;
import cn.jbone.statemachine.demo.order.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class MyStateMachineListener extends StateMachineListenerAdapter<OrderStates, OrderEvents> {
    @Override
    public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
        System.out.println("【Listener】stateChanged，State from " + (from == null ? "" : from.getId()) + " to " + to.getId());
    }

    @Override
    public void stateEntered(State<OrderStates, OrderEvents> state) {
        System.out.println("【Listener】stateEntered：" + state.getId());
    }

    @Override
    public void stateExited(State<OrderStates, OrderEvents> state) {
        System.out.println("【Listener】stateExited：" + state.getId());
    }

    @Override
    public void eventNotAccepted(Message<OrderEvents> event) {
        System.out.println("【Listener】eventNotAccepted：" + event);
    }

    @Override
    public void transition(Transition<OrderStates, OrderEvents> transition) {
        System.out.println("【Listener】transition：" + transition.getSource().getId() + "~" + transition.getTarget().getId());
    }

    @Override
    public void transitionStarted(Transition<OrderStates, OrderEvents> transition) {
        System.out.println("【Listener】transitionStarted：" + transition.getSource().getId() + "~" + transition.getTarget().getId());
    }

    @Override
    public void transitionEnded(Transition<OrderStates, OrderEvents> transition) {
        System.out.println("【Listener】transitionEnded：" + transition.getSource().getId() + "~" + transition.getTarget().getId());
    }

    @Override
    public void stateMachineStarted(StateMachine<OrderStates, OrderEvents> stateMachine) {
        System.out.println("【Listener】stateMachineStarted。");
    }

    @Override
    public void stateMachineStopped(StateMachine<OrderStates, OrderEvents> stateMachine) {
        System.out.println("【Listener】stateMachineStopped。");
    }

    @Override
    public void stateMachineError(StateMachine<OrderStates, OrderEvents> stateMachine, Exception exception) {
        System.out.println("【Listener】stateMachineError：" + stateMachine.getId());
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        System.out.println("【Listener】extendedStateChanged：key=" + key + ",value=" + value);
    }
    @Autowired
    OrderService orderService;
    @Override
    public void stateContext(StateContext<OrderStates, OrderEvents> stateContext) {
        System.out.println("【Listener】stateContext " + stateContext.getStage().name());
        if(stateContext.getStage() == StateContext.Stage.STATE_CHANGED){
            Order order = (Order) stateContext.getMessageHeader("order");
            order.setState(stateContext.getTarget().getId());
            orderService.updateOrder(order);
        }else if (stateContext.getStage() == StateContext.Stage.EVENT_NOT_ACCEPTED){
            System.out.println(stateContext);
        }
    }
}
