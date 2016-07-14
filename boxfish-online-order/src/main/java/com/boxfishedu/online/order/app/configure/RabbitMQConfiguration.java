package com.boxfishedu.online.order.app.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.util.DigestUtils;

@Configuration
@EnableRabbit
@SuppressWarnings("all")
public class RabbitMQConfiguration {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RabbitMQProperties properties;


    @Bean(name = RabbitMQConstant.NOTIFICATION_TASK_EXCHANGE)
    public Exchange directExchange() {
        return new DirectExchange(RabbitMQConstant.NOTIFICATION_TASK_EXCHANGE, true, false);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory, Exchange exchange) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareExchange(directExchange());

        Queue updateOrderQueue = new Queue(RabbitMQConstant.PRE_UPDATE_ORDER_QUEUE, true);
        rabbitAdmin.declareQueue(updateOrderQueue);
        Binding updateOrderQueueBinding = BindingBuilder.bind(updateOrderQueue).to(exchange)
                .with(RabbitMQConstant.PRE_UPDATE_ORDER_QUEUE).noargs();
        rabbitAdmin.declareBinding(updateOrderQueueBinding);

        Queue updateOrderReplyQueue = new Queue(RabbitMQConstant.PRE_UPDATE_ORDER_QUEUE + ".reply", true);
        rabbitAdmin.declareQueue(updateOrderReplyQueue);
        Binding updateOrderReplyBinding = BindingBuilder.bind(updateOrderReplyQueue).to(exchange)
                .with(RabbitMQConstant.PRE_CREATE_SERVICE_QUEUE + ".reply").noargs();
        rabbitAdmin.declareBinding(updateOrderReplyBinding);

        Queue createServiceQueue = new Queue(RabbitMQConstant.PRE_CREATE_SERVICE_QUEUE, true);
        rabbitAdmin.declareQueue(createServiceQueue);
        Binding createServiceQueueBinding = BindingBuilder.bind(createServiceQueue).to(exchange)
                .with(RabbitMQConstant.PRE_CREATE_SERVICE_QUEUE).noargs();
        rabbitAdmin.declareBinding(createServiceQueueBinding);

        Queue exceptionDataQueue = new Queue(RabbitMQConstant.EXCEPTION_DATA_QUEUE, true);
        rabbitAdmin.declareQueue(exceptionDataQueue);
        Binding exceptionDataQueueBinding = BindingBuilder.bind(exceptionDataQueue).to(exchange)
                .with(RabbitMQConstant.EXCEPTION_DATA_QUEUE).noargs();
        rabbitAdmin.declareBinding(exceptionDataQueueBinding);

        return rabbitAdmin;
    }

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public StatefulRetryOperationsInterceptor statefulRetryOperationsInterceptorFactoryBean(RabbitTemplate rabbitTemplate) {
        StatefulRetryOperationsInterceptorFactoryBean factoryBean = new StatefulRetryOperationsInterceptorFactoryBean();
        factoryBean.setMessageRecoverer(new MessageRecover(rabbitTemplate));
        factoryBean.setMessageKeyGenerator(message -> DigestUtils.md5DigestAsHex(message.getBody()));

        return factoryBean.getObject();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            StatefulRetryOperationsInterceptor interceptor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor("order.notifier"));
        factory.setConnectionFactory(this.connectionFactory);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setMessageConverter(messageConverter());
        factory.setPrefetchCount(1);
        factory.setAdviceChain(interceptor);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

        return factory;
    }

    private RabbitTemplate getRabbitTemplate(String queueName) {
        RabbitTemplate template = new RabbitTemplate(this.connectionFactory);
        template.setExchange(RabbitMQConstant.NOTIFICATION_TASK_EXCHANGE);
        template.setQueue(queueName);
        template.setRoutingKey(queueName);
        template.setMessageConverter(messageConverter());
        template.setMandatory(true);

        return template;
    }

    @Primary
    @Bean(name = RabbitMQConstant.PRE_UPDATE_ORDER_TEMPLATE)
    public RabbitTemplate updateOrderRabbitTemplate() {
        RabbitTemplate template = getRabbitTemplate(RabbitMQConstant.PRE_UPDATE_ORDER_QUEUE);

        return template;
    }


    @Bean(name = RabbitMQConstant.PRE_CREATE_SERVICE_TEMPLATE)
    public RabbitTemplate createTeachingServiceRabbitTemplate() {
        RabbitTemplate template = getRabbitTemplate(RabbitMQConstant.PRE_CREATE_SERVICE_QUEUE);

        return template;
    }

    @Bean(name = RabbitMQConstant.EXCEPTION_DATA_TEMPLATE)
    public RabbitTemplate exceptionDataRabbitTemplate() {
        RabbitTemplate template = getRabbitTemplate(RabbitMQConstant.EXCEPTION_DATA_QUEUE);

        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    private static class MessageRecover extends RejectAndDontRequeueRecoverer {
        private final RabbitTemplate rabbitTemplate;

        public MessageRecover(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }

        @Override
        public void recover(Message message, Throwable cause) {
            logger.error(message, cause);
            rabbitTemplate.send(message);
            super.recover(message, cause);
        }
    }
}
