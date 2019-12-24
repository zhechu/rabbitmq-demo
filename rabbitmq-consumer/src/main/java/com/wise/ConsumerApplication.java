package com.wise;

import com.rabbitmq.client.*;
import com.wise.entity.Order;
import com.wise.util.JacksonUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author lingyuwang
 * @date 2019/12/24 11:01
 **/
public class ConsumerApplication {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.163.21");
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("123456");
            factory.setVirtualHost("/dev");

            // 创建 TCP 连接
            connection = factory.newConnection();
            // 创建管道
            channel = connection.createChannel();

            // 声明交换机,topic类型，持久化,非自动删除
            String exchangeName = "order-exchange";
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, null);
            // 声明队列
            String queueName = "order-queue";
            channel.queueDeclare(queueName, true, false, false, null);
            // 交换机和队列绑定
            channel.queueBind(queueName, exchangeName, "one.*");

            channel.basicConsume(queueName, true, new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String json = new String(body, "UTF-8");
                    System.out.println("body:" + JacksonUtil.readValue(json, Order.class));
                }

            });

            TimeUnit.HOURS.sleep(1);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(channel != null) {
                channel.close();
            }
            if(connection != null) {
                connection.close();
            }
        }
    }

}
