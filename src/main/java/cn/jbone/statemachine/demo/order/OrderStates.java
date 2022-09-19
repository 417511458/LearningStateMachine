package cn.jbone.statemachine.demo.order;

/**
 * 订单状态枚举
 */
public enum OrderStates {
    WAIT_PAY,//待支付
    WAIT_DELIVER,//待发货
    WAIT_RECEIVE,//待收货
    WAIT_EVALUATE,//待评价
    SUCCESS //交易成功
}
