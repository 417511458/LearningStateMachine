package cn.jbone.statemachine.demo.order;

/**
 * 订单事件枚举
 */
public enum OrderEvents {
    PAY, //支付
    DELIVER, //发货
    RECEIVE, //收货
    EVALUATE //评价
}
