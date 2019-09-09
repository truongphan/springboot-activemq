package com.springboot.activemq.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
@ComponentScan(basePackages = "com.springboot.activemq")
public class JmsConfig {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String BROKER_USERNAME = "admin";
    private static final String BROKER_PASSWORD = "admin";

    /*
    ActiveMQConnectionFactory: Dùng để tạo các connection.
    Mặc định, để kết nối đến ActiveMQ, chúng ta sẽ sử dụng url tcp://localhost:61616,
    admin url: http://localhost:8161/admin/ với username và password: admin và admin
     */
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setUserName(BROKER_USERNAME);
        connectionFactory.setPassword(BROKER_PASSWORD);

        return connectionFactory;
    }

    /*
    JmsTemplate: Là một helper class giúp đơn giản hóa việc gửi và nhận tin nhắn thông qua JMS.
     */
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());

        return template;
    }

    /*
    DefaultJmsListenerContainerFactory: Tạo bộ lắng nghe trong gửi nhận tin.
    factory.setPubSubDomain(boolean): Truyền vào false nếu muốn sử dụng queue để gửi nhận tin,
    true nếu là topic.
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("3-10");
        // true: using jms topic, false: using jms queue
        factory.setPubSubDomain(false);

        return factory;
    }

    /*
    @EnableJms: cho phép phát hiện các annotation @JmsListener trong source code mà chúng ta sẽ sử dụng để gửi và nhận tin.
    Mặc định, Spring Boot hỗ trợ kết nối và gửi nhận tin nhắn thông qua Queue.
    Để có thể sử dụng Topic để gửi nhận tin, bạn cần thêm cấu hình spring.jms.pub-sub-domain=true ở application.properties
    (Nếu bạn đang sử dụng JmsConfig class, cần truyền vào tham số true ở phương thức setPubSubDomain)
     */
}
