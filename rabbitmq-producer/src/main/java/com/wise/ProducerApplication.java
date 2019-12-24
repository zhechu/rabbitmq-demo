package com.wise;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wise.entity.Order;
import com.wise.util.JacksonUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author lingyuwang
 * @date 2019/12/24 11:02
 **/
public class ProducerApplication {

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
            channel.exchangeDeclare(exchangeName, "topic", true, false, null);
            Order order = new Order();
            order.setOrderName("order1");
            order.setAmount(5000F);
            order.setStatus("new");
            String orderJson = JacksonUtil.writeValueAsString(order);
            channel.basicPublish(exchangeName, "one.odd", null, orderJson.getBytes("UTF-8"));

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
