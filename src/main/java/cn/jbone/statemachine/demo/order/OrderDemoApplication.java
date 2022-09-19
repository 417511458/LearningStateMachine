package cn.jbone.statemachine.demo.order;

import cn.jbone.statemachine.demo.order.interceptor.MyStateMachineInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SpringBootApplication
public class OrderDemoApplication implements CommandLineRunner {

    @Autowired
    private StateMachineFactory<OrderStates, OrderEvents> stateMachineFactory;
    @Autowired
    private StateMachinePersister stateMachinePersister;
    @Autowired
    private OrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(OrderDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0;i < 1;i++ ) {
            String orderId = "order:"+ i;
            System.out.println(orderId + "--------------------------------------start");
            createOrder(orderId);
            payOrder(orderId);
            deliverOrder(orderId);
            receiveOrder(orderId);
            evalOrder(orderId);
            System.out.println(orderId + "--------------------------------------end");
        }
    }

    /**
     * 创建订单
     */
    public void createOrder(String orderId){
        Order order = new Order();
        order.setId(orderId);
        order.setState(OrderStates.WAIT_PAY);
        orderService.createOrder(order);
    }

    /**
     * 支付订单，发送OrderEvents.PAY事件
     */
    public void payOrder(String orderId) throws Exception {
        sendEvent(OrderEvents.PAY,orderId);
    }

    /**
     * 发货，发送OrderEvents.DELIVER事件
     * @param orderId
     * @throws Exception
     */
    public void deliverOrder(String orderId) throws Exception {
        sendEvent(OrderEvents.DELIVER,orderId);
    }

    /**
     * 收货，发送OrderEvents.RECEIVE事件
     */
    public void receiveOrder(String orderId) throws Exception {
        sendEvent(OrderEvents.RECEIVE,orderId);
    }

    /**
     * 评价，发送OrderEvents.EVALUATE事件
     * @param orderId
     * @throws Exception
     */
    public void evalOrder(String orderId) throws Exception {
        sendEvent(OrderEvents.EVALUATE,orderId);
    }


    /**
     * 统一的发送事件驱动状态机的方法
     * 1.加载订单
     * 2.创建状态机实例
     * 3.重置状态机状态
     * 4.发送事件驱动
     * 5.订阅结果
     */
    public StateMachine<OrderStates, OrderEvents> sendEvent(OrderEvents event,String orderId) throws Exception {
        System.out.println("【SendEvent】" + orderId + event + " start ----------");
        //1.加载订单
        Order order = orderService.getOrder(orderId);
        //2.创建状态机实例
        StateMachine<OrderStates, OrderEvents> stateMachine = stateMachineFactory.getStateMachine();
        //3.重置状态机状态
        stateMachinePersister.restore(stateMachine,order);
//        MyStateMachineUtils.setCurrentState(stateMachine,order.getState());
        //使用sendEvent(Message<E>)发送事件，只能通过stateMachine.getExtendedState()传送返回值
//        stateMachine.sendEvent(createMessage(event,order));
        //使用stateMachine.sendEvent(Mono<Message>)发送事件
//        stateMachine.getStateMachineAccessor().withRegion().addStateMachineInterceptor(myStateMachineInterceptor);
        //4.发送事件驱动
        Flux<StateMachineEventResult<OrderStates, OrderEvents>> results = stateMachine.sendEvent(Mono.just(createMessage(event,order)));
        //5.订阅结果
        results.subscribe(new Consumer<StateMachineEventResult<OrderStates, OrderEvents>>() {
            @Override
            public void accept(StateMachineEventResult<OrderStates, OrderEvents> orderStatesOrderEventsStateMachineEventResult) {
                System.out.println("results.subscribe.consumer.accept：" + orderStatesOrderEventsStateMachineEventResult);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                System.out.println("results.subscribe.errorConsumer.accept：" + throwable);
            }
        }, new Runnable() {
            @Override
            public void run() {
                System.out.println("results.subscribe.complete.runnable：" + "complete");
            }
        });
        System.out.println("【SendEvent】" + orderId + event + " end ----------");
        return stateMachine;
    }


    /**
     * 创建Message，将Order传入头信息
     */
    public Message<OrderEvents> createMessage(OrderEvents event, Order order){
        Map<String,Object> headers = new HashMap<>();
        headers.put("order",order);
        return MessageBuilder.createMessage(event,new MessageHeaders(headers));
    }

    @Autowired
    MyStateMachineInterceptor myStateMachineInterceptor;

}
