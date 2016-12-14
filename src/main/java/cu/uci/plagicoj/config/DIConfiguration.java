/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import cu.uci.plagicoj.scpdt.Consumer;
import cu.uci.plagicoj.services.EmailService;
import cu.uci.plagicoj.services.MessageService;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author leandro
 */
@Configuration
@ComponentScan(value = {"cu.uci.plagicoj"})
@PropertySource("classpath:/cu/uci/plagicoj/config/app.properties")
public class DIConfiguration {

    @Autowired
    protected Environment env;

    @Bean
    public MessageService getMessageService() {
        return new EmailService();
    }

    @Bean
    public Semaphore semaphore() {
        return new Semaphore(0);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(env.getProperty("amqp.server"));        
        factory.setPassword("cojrabbit123*-+");
        return factory;
    }
  
    @Bean
    public Jedis jedis() {
        return new JedisPool(new JedisPoolConfig(), env.getProperty("redis.server")).getResource();
    }
    
    @Bean
    public EntityManager entityManager(){
     
        return Persistence.createEntityManagerFactory(env.getProperty("persistence.unit")).createEntityManager();
    }

    @Bean
    @Scope("prototype")
    public Consumer consumer() throws IOException {
        return new Consumer();
    }

    @Bean
    public Consumer lowPriorityConsumer() throws IOException {
        return new Consumer(env.getProperty("amqp.queue.priority.low"), semaphore());
    }

    @Bean
    public Consumer meddiumPriorityConsumer() throws IOException {
        return new Consumer(env.getProperty("amqp.queue.priority.medium"), semaphore());
    }

    @Bean
    public Consumer highPriorityConsumer() throws IOException {
        return new Consumer(env.getProperty("amqp.queue.priority.high"), semaphore());
    }
}
