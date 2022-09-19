package cn.jbone.statemachine.demo.order.interceptor;

import cn.jbone.statemachine.demo.order.OrderEvents;
import cn.jbone.statemachine.demo.order.OrderStates;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class MyStateMachineInterceptor extends StateMachineInterceptorAdapter<OrderStates, OrderEvents> {
    public MyStateMachineInterceptor() {
        super();
    }

    @Override
    public Message<OrderEvents> preEvent(Message<OrderEvents> message, StateMachine<OrderStates, OrderEvents> stateMachine) {
        System.out.println("【Interceptor】preEvent");
        return super.preEvent(message, stateMachine);
    }

    @Override
    public void preStateChange(State<OrderStates, OrderEvents> state, Message<OrderEvents> message, Transition<OrderStates, OrderEvents> transition, StateMachine<OrderStates, OrderEvents> stateMachine, StateMachine<OrderStates, OrderEvents> rootStateMachine) {
        System.out.println("【Interceptor】preStateChange");
        super.preStateChange(state, message, transition, stateMachine, rootStateMachine);
    }

    @Override
    public void postStateChange(State<OrderStates, OrderEvents> state, Message<OrderEvents> message, Transition<OrderStates, OrderEvents> transition, StateMachine<OrderStates, OrderEvents> stateMachine, StateMachine<OrderStates, OrderEvents> rootStateMachine) {
        System.out.println("【Interceptor】postStateChange");
        super.postStateChange(state, message, transition, stateMachine, rootStateMachine);
    }

    @Override
    public StateContext<OrderStates, OrderEvents> preTransition(StateContext<OrderStates, OrderEvents> stateContext) {
        System.out.println("【Interceptor】preTransition");
        return super.preTransition(stateContext);
    }

    @Override
    public StateContext<OrderStates, OrderEvents> postTransition(StateContext<OrderStates, OrderEvents> stateContext) {
        System.out.println("【Interceptor】postTransition");
        return super.postTransition(stateContext);
    }

    @Override
    public Exception stateMachineError(StateMachine<OrderStates, OrderEvents> stateMachine, Exception exception) {
        System.out.println("【Interceptor】stateMachineError");
        return super.stateMachineError(stateMachine, exception);
    }
}
