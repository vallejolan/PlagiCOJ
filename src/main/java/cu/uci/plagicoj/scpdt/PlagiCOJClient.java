/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.scpdt;

import cu.uci.plagicoj.scpdt.SCPDT;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import cu.uci.plagicoj.utils.DetectionPriority;

/**
 * @author Leandro
 */
public class PlagiCOJClient implements Runnable {

    //This client will wait for this time for something to process if nothing it will finish.
    public static final int MAX_WAITING_TIME = 2000;
    public static final int MAX_ORDERS_COUNT = 10000;
    private int amortizedWaitingTime;
    
    private Connection connection;
    private Channel channel;
    private String replyQueueName;
    private QueueingConsumer consumer;
    private BlockingQueue<Delivery> deliveries;
    private String routingKey = SCPDT.LOW_PRIORITY_QUEUE_NAME;

    private DictumReceivedCallback  dictumReceivedCallback;
 
    private AtomicInteger remainingDictums;
    
    
    
    public PlagiCOJClient init(DictumReceivedCallback  dictumReceivedCallback) throws Exception {
        this.dictumReceivedCallback =dictumReceivedCallback;
        
        return init();        
    }
    
    public PlagiCOJClient init(DictumReceivedCallback  dictumReceivedCallback, DetectionPriority detectionPriority) throws Exception {
        this.routingKey = getRoutingKey(detectionPriority);
        
        return init(dictumReceivedCallback);        
    }
    
    public PlagiCOJClient init() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        /*factory.setHost("10.55.9.119");*/
        factory.setHost("127.0.0.1");
        factory.setPassword("cojrabbit123*-+");
        
        connection = factory.newConnection();
        channel = connection.createChannel();
        deliveries = new LinkedBlockingDeque();
        remainingDictums = new AtomicInteger(0);
        
        amortizedWaitingTime = MAX_WAITING_TIME;
        
        return this;
    }

    public PlagiCOJClient setupConsumer()
            throws Exception {

        setReplyQueueName(channel.queueDeclare().getQueue());        
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(getReplyQueueName(), true, consumer);
        return this;
    }

    private BasicProperties getRequestProperties() {
        return new BasicProperties.Builder()
                .replyTo(getReplyQueueName())
                .build();
    }
    
    private int amortize(int value,int objetive, int processed ){        
        if (processed == objetive)
            return value/2;
        return objetive*value/processed;
        //goal -> objetive
        //value -> processed
    }
    public void sendOrder(String message) throws Exception {
        if (remainingDictums.get() >= MAX_ORDERS_COUNT){
            Thread.sleep(amortizedWaitingTime);
            
            amortizedWaitingTime = amortize(amortizedWaitingTime,MAX_ORDERS_COUNT, MAX_ORDERS_COUNT - remainingDictums.get());           
        }
        
        channel.basicPublish(
                "pcojexch",
                routingKey,
                getRequestProperties(),
                message.getBytes());
        
        remainingDictums.incrementAndGet();
        
        System.out.println("Sent plagicoj detection order.");
    }
    
    private String getRoutingKey(DetectionPriority detectionPriority){
        switch (detectionPriority) {
            case High:
                return  SCPDT.HIGH_PRIORITY_QUEUE_NAME;
            case Medium:
                return SCPDT.MEDIUM_PRIORITY_QUEUE_NAME;
            default:
                return SCPDT.LOW_PRIORITY_QUEUE_NAME;
        }        
    }
    
    public void sendOrder(String message, DetectionPriority detectionPriority) throws Exception {
        while (remainingDictums.get() >= MAX_ORDERS_COUNT){
            Thread.sleep(amortizedWaitingTime);
            
            amortizedWaitingTime = amortize(amortizedWaitingTime,MAX_ORDERS_COUNT, MAX_ORDERS_COUNT - remainingDictums.get());           
        }
        
        String customRoutingKey = getRoutingKey(detectionPriority);
        
        channel.basicPublish(
                "pcojexch",
                customRoutingKey,
                getRequestProperties(),
                message.getBytes());
        
        remainingDictums.incrementAndGet();
        
        System.out.println("Sent plagicoj detection order.");
    }

    public String nextDelivery() throws InterruptedException, UnsupportedEncodingException{
        Delivery delivery = deliveries.take();
      
        
        return new String(delivery.getBody(), "UTF-8");
    }
    
    private void startReceiving() throws InterruptedException, UnsupportedEncodingException, IOException {
        int waitingTime = 100;
        
        //@Lan - If no dictum to process wait 2 seconds and try again for last time.
        while (connection.isOpen() && waitingTime < MAX_WAITING_TIME) {     
            
                if (remainingDictums.get() == 0) {
                    Thread.sleep(waitingTime);
                    waitingTime*=2;//Double waiting time for next iteration                
                }
                if (remainingDictums.get() > 0) {                    
                    Delivery delivery = consumer.nextDelivery();

                    remainingDictums.decrementAndGet();

                    deliveries.add(delivery);

                    if (dictumReceivedCallback != null) {                        
                        dictumReceivedCallback.dictumReceived(new String(delivery.getBody(), "UTF-8"), replyQueueName);
                    }
                    waitingTime = 100;
                }
        }
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void run() {
        try {
            startReceiving();
        } catch (InterruptedException ex) {
            Logger.getLogger(PlagiCOJClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PlagiCOJClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlagiCOJClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            closeConnection();
        }
    }

    /**
     * @return the replyQueueName
     */
    public String getReplyQueueName() {
        return replyQueueName;
    }

    /**
     * @param replyQueueName the replyQueueName to set
     */
    public void setReplyQueueName(String replyQueueName) {
        this.replyQueueName = replyQueueName;
    }

    /**
     * @return the remainingDictums
     */
    public int getRemainingDictums() {
        return remainingDictums.get();
    }

    /**
     * @param remainingDictums the remainingDictums to set
     */
    public void setRemainingDictums(int remainingDictums) {
        this.remainingDictums.set(remainingDictums);
    }
    
    public interface DictumReceivedCallback{
        public void dictumReceived(String dictum,String fromQueueName);
    }
}
