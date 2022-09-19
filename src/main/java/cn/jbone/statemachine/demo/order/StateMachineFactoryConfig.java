package cn.jbone.statemachine.demo.order;

import cn.jbone.statemachine.demo.order.actions.*;
import cn.jbone.statemachine.demo.order.guards.MyGuard;
import cn.jbone.statemachine.demo.order.listener.MyStateMachineListener;
import cn.jbone.statemachine.demo.order.monitor.MyStateMachineMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.*;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineFactoryConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config)
            throws Exception {
        //配置监听Listener
        config
                .withConfiguration()
                .autoStartup(false)
                .listener(stateMachineListener);
        //配置监视Monitor
        config.withMonitoring().monitor(stateMachineMonitor);
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states)
            throws Exception {
        //配置初始状态、终态和状态机状态集合
        states
                .withStates()
                .initial(OrderStates.WAIT_PAY).end(OrderStates.SUCCESS)
                .states(EnumSet.allOf(OrderStates.class));

        //配置状态的Action
        //此处和下面设置的action互斥，既只有一组生效
        //当配置EntryAction和ExitAction时
        //在STATE_ENTRY（进入State）时调用EntryAction
        //在STATE_EXIT（离开State）时调用ExitAction
        states.withStates().state(OrderStates.WAIT_DELIVER,entryAction,exitAction);
        states.withStates().state(OrderStates.WAIT_RECEIVE,entryAction,exitAction);
        states.withStates().state(OrderStates.WAIT_EVALUATE,entryAction,exitAction);
        states.withStates().state(OrderStates.SUCCESS,entryAction,exitAction);
        //当这两组同时配置时，只有stateAction生效
        // 在STATE_ENTRY（进入State）时调用StateAction
//        states.withStates().state(OrderStates.WAIT_SHIP,stateAction);
//        states.withStates().state(OrderStates.WAIT_RECEIVE,stateAction);
//        states.withStates().state(OrderStates.WAIT_EVALUATE,stateAction);
//        states.withStates().state(OrderStates.SUCCESS,stateAction);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions)
            throws Exception {
        //配置流转
        transitions
                //当发生OrderEvents.PAY时，执行myGuard，并从OrderStates.WAIT_PAY流转至OrderStates.WAIT_DELIVER，在流转过程中执行payAction，发生异常时执行exceptionAction
                .withExternal()
                .source(OrderStates.WAIT_PAY).target(OrderStates.WAIT_DELIVER).event(OrderEvents.PAY).action(payAction,exceptionAction).guard(myGuard)
                .and()
                //当发生OrderEvents.DELIVER时，从OrderStates.WAIT_DELIVER流转至OrderStates.WAIT_RECEIVE，在流转过程中执行deliverAction，发生异常时执行exceptionAction
                .withExternal()
                .source(OrderStates.WAIT_DELIVER).target(OrderStates.WAIT_RECEIVE).event(OrderEvents.DELIVER).action(deliverAction,exceptionAction)
                .and()
                //当发生OrderEvents.RECEIVE时，从OrderStates.WAIT_RECEIVE流转至OrderStates.WAIT_EVALUATE，在流转过程中执行receiveAction，发生异常时执行exceptionAction
                .withExternal()
                .source(OrderStates.WAIT_RECEIVE).target(OrderStates.WAIT_EVALUATE).event(OrderEvents.RECEIVE).action(receiveAction,exceptionAction)
                .and()
                //当发生OrderEvents.EVALUATE时，从OrderStates.WAIT_EVALUATE流转至OrderStates.SUCCESS，在流转过程中执行evalAction，发生异常时执行exceptionAction
                .withExternal()
                .source(OrderStates.WAIT_EVALUATE).target(OrderStates.SUCCESS).event(OrderEvents.EVALUATE).action(evalAction,exceptionAction)
        ;
    }


    @Autowired
    OrderStateMachinePersist orderStateMachinePersist;
    @Bean
    public StateMachinePersister<OrderStates, OrderEvents, Order> stateMachinePersist() {
        //状态机持久化器
        return new DefaultStateMachinePersister<>(orderStateMachinePersist);
    }
    @Autowired
    PayAction payAction;
    @Autowired
    DeliverAction deliverAction;
    @Autowired
    ReceiveAction receiveAction;
    @Autowired
    EvalAction evalAction;

    @Autowired
    EntryAction entryAction;
    @Autowired
    ExitAction exitAction;
    @Autowired
    StateAction stateAction;
    @Autowired
    ExceptionAction exceptionAction;

    @Autowired
    MyGuard myGuard;

    @Autowired
    MyStateMachineListener stateMachineListener;
    @Autowired
    MyStateMachineMonitor stateMachineMonitor;
}
