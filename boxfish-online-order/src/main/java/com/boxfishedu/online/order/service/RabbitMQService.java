package com.boxfishedu.online.order.service;

import com.boxfishedu.online.order.entity.OrderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.toJsonNoException;
import static com.boxfishedu.online.order.app.configure.RabbitMQConstant.*;

/**
 * Created by malu on 16/7/14.
 */
@Service
public class RabbitMQService {
    @Autowired
    OrderService orderService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private
    @Qualifier(PRE_UPDATE_ORDER_TEMPLATE)
    RabbitTemplate updateOrderRabbitTemplate;

    @Autowired
    private
    @Qualifier(PRE_CREATE_SERVICE_TEMPLATE)
    RabbitTemplate createTeachingServiceRabbitTemplate;

    @Autowired
    private
    @Qualifier(EXCEPTION_DATA_TEMPLATE)
    RabbitTemplate exceptionDataTemplate;

    private Logger logger = LoggerFactory.getLogger(RabbitMQService.class);

    /**
     * 将订单加入准备更新状态的队列
     */
    public void putIntoUpdateQueue(final OrderForm orderForm) {
        try{
            this.updateOrderRabbitTemplate.convertAndSend(orderForm);
            logger.info("订单[order_code = {}]已进入消息队列[queue = {}]", orderForm.getOrderCode(), PRE_UPDATE_ORDER_QUEUE);
        }catch (Exception e){
            sendErrorData(orderForm, e.toString());
            logger.error("订单[order_code = {}]未进入消息队列[queue = {}]", orderForm.getOrderCode(), PRE_UPDATE_ORDER_QUEUE);
        }
    }

    public void preUpdateStatus(OrderForm orderForm){

    }

    private void sendErrorData(final OrderForm orderForm, String errorMsg) {
        String message = "异常数据:" + toJsonNoException(orderForm);
        message += ("异常信息:" + errorMsg);
        logger.error(errorMsg);
        this.exceptionDataTemplate.convertAndSend(message);
    }
}
