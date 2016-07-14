package com.boxfishedu.online.order.app.configure;

/**
 * Created by lauzhihao on 2016/04/15.
 */
public class RabbitMQConstant {

    public static final String NOTIFICATION_TASK_EXCHANGE = "com.boxfishedu.order.exchange";

    public static final String PRE_UPDATE_ORDER_QUEUE = "com.boxfishedu.order.updating.queue";

    public static final String PRE_UPDATE_ORDER_TEMPLATE = "updateOrderRabbitTemplate";

    public static final String PRE_CREATE_SERVICE_QUEUE = "com.boxfishedu.order.service.queue";

    public static final String PRE_CREATE_SERVICE_TEMPLATE = "createTeachingServiceRabbitTemplate";

    public static final String NOTIFY_FROM_TEACHING_QUEUE = "boxfish.fishcard.notifyorder.queue";

    public static final String EXCEPTION_DATA_QUEUE = "com.boxfishedu.order.exception.queue";

    public static final String EXCEPTION_DATA_TEMPLATE = "exceptionDataRabbitTemplate";
}
